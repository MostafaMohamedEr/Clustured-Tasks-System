/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Workers;

import LogicalSwitch.LogicalWorkerInteraface;
import LogicalSwitch.Task;
import Worker.WorkerImp;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Mostafa
 */
public class Worker3 extends Thread {

    private static workerGui g3;
    private static LogicalWorkerInteraface l;
    private static WorkerImp w3;

    public static void main(String[] args) throws Exception {
        // add shutdown hook to can call function remove Worker if the window is cloesd
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    l.RemoveWorker(w3);
                } catch (RemoteException ex) {
                    System.out.println(ex);;
                }
            }
        }));
        //Boolean to ensure that only 1 task is loaded 
        boolean waitTillSolve = false;
        //numberOfTotalTasks is variable represents all number of tasks that worker is solved during running the program to show in gui
        int numberOfTotalTasks = 0;
        g3 = new workerGui();
        g3.putLables(3);
        g3.setVisible(true);
        Registry r;
        try {
            r = LocateRegistry.getRegistry("localhost", 2000);
            l = (LogicalWorkerInteraface) r.lookup("solvingTasks");
            //Creating Worker with ID 1
            int id = 3;
            w3 = new WorkerImp(id);
            //Registering to workers Array
            l.RegisteringWorker(w3);
            //Get tasks from Logical to solve
            Task TaskTOSolve;
            while (true) {
                //ASk logical for tasks untill it is sent from user
                do {
                    g3.setStatues("Waiting for client to send tasks");
                    TaskTOSolve = l.workerGetTask(id);
                } while (TaskTOSolve == null && !waitTillSolve);
                waitTillSolve = true;
                //Check Task type and solve it
                if (TaskTOSolve.getTaskType() == 1) {
                    //if task from type 1 , it requires checking number is prime
                    //Worker solves the function and return bool value
                    boolean res = w3.calculatePrime(TaskTOSolve.getNumberToCalculate()[0]);
                    //Then convert boolean to integer to can store in taskResult if true=1 ,if false=0
                    int boolToIntege = res ? 1 : 0;
                    //Updating task result and mark it as solved
                    TaskTOSolve.setResult(boolToIntege);
                    TaskTOSolve.setSolved(true);
                } else if (TaskTOSolve.getTaskType() == 2) {
                    int totalOfSum = w3.calculateSum(TaskTOSolve.getNumberToCalculate()[0], TaskTOSolve.getNumberToCalculate()[1], TaskTOSolve.getNumberToCalculate()[2]);
                    //Updating task result and mark it as solved
                    TaskTOSolve.setResult(totalOfSum);
                    TaskTOSolve.setSolved(true);
                } else if (TaskTOSolve.getTaskType() == 3) {
                    int numberOfFactors = w3.calculateNumberOfFactors(TaskTOSolve.getNumberToCalculate()[0]);
                    //Updating task result and mark it as solved
                    TaskTOSolve.setResult(numberOfFactors);
                    TaskTOSolve.setSolved(true);
                }
                numberOfTotalTasks++;
                g3.putTasks(numberOfTotalTasks);
                l.returnSolvedTaskToLogical(TaskTOSolve);
                TaskTOSolve = null;
                waitTillSolve = false;
            }

        } catch (RemoteException ex) {
            //ُException if worker is built before logical
            g3.setVisible(false);
            JOptionPane.showMessageDialog(null, "You Should run Logical Switch then Worker 1,2,3 then Client");
            System.exit(0);
        } catch (NotBoundException ex) {
           Logger.getLogger(Worker1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
