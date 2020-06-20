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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Mostafa
 */
public class Worker2 extends Thread {

    private static workerGui g2;
    private static LogicalWorkerInteraface l;
    private static WorkerImp w2;

    public static void main(String[] args) {
        // add shutdown hook to can call function remove Worker if the window is cloesd
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    l.RemoveWorker(w2);
                } catch (RemoteException ex) {
                    System.out.println(ex);
                }
            }
        }));
        //Boolean to ensure that only 1 task is loaded 
        boolean waitTillSolve = false;
        //numberOfTotalTasks is variable represents all number of tasks that worker is solved during running the program to show in gui
        int numberOfTotalTasks = 0;
        g2 = new workerGui();
        g2.putLables(2);
        g2.setVisible(true);
        Registry r;
        try {
            r = LocateRegistry.getRegistry("localhost", 2000);
            l = (LogicalWorkerInteraface) r.lookup("solvingTasks");
            //Creating Worker with ID 1
            int id = 2;
            w2 = new WorkerImp(id);
            //Registering to workers Array
            l.RegisteringWorker(w2);
            //Get tasks from Logical to solve
            Task TaskTOSolve;
            while (true) {
                g2.setStatues("Waiting for client to send tasks");
                //ASk logical for tasks untill it is sent from user
                do {
                    TaskTOSolve = l.workerGetTask(id);
                } while (TaskTOSolve == null && !waitTillSolve);
                g2.setStatues("Solving");
                waitTillSolve = true;
                //Check Task type and solve it
                if (TaskTOSolve.getTaskType() == 1) {
                    //if task from type 1 , it requires checking number is prime
                    //Worker solves the function and return bool value
                    boolean res = w2.calculatePrime(TaskTOSolve.getNumberToCalculate()[0]);
                    //Then convert boolean to integer to can store in taskResult if true=1 ,if false=0
                    int boolToIntege = res ? 1 : 0;
                    //Updating task result and mark it as solved
                    TaskTOSolve.setResult(boolToIntege);
                    TaskTOSolve.setSolved(true);
                } else if (TaskTOSolve.getTaskType() == 2) {
                    int totalOfSum = w2.calculateSum(TaskTOSolve.getNumberToCalculate()[0], TaskTOSolve.getNumberToCalculate()[1], TaskTOSolve.getNumberToCalculate()[2]);
                    //Updating task result and mark it as solved
                    TaskTOSolve.setResult(totalOfSum);
                    TaskTOSolve.setSolved(true);
                } else if (TaskTOSolve.getTaskType() == 3) {
                    int numberOfFactors = w2.calculateNumberOfFactors(TaskTOSolve.getNumberToCalculate()[0]);
                    //Updating task result and mark it as solved
                    TaskTOSolve.setResult(numberOfFactors);
                    TaskTOSolve.setSolved(true);
                }
                numberOfTotalTasks++;
                g2.putTasks(numberOfTotalTasks);
                l.returnSolvedTaskToLogical(TaskTOSolve);
                TaskTOSolve = null;
                waitTillSolve = false;
            }
            // g1.setStatues("Finished");

        } catch (RemoteException ex) {
            //ُException if worker is built before logical
            g2.setVisible(false);
            JOptionPane.showMessageDialog(null, "You Should run Logical Switch then Worker 1,2,3 then Client");
            System.exit(0);
        } catch (NotBoundException ex) {
            Logger.getLogger(Worker1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
