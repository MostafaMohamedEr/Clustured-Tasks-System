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
        //numberOfTotalTasks is variable represents all number of tasks that worker is solved during running the program to show in gui
        int numberOfTotalTasks = 0;
        g2 = new workerGui();
        g2.putLables(2);
        g2.setVisible(true);
        try {
            Registry r = LocateRegistry.getRegistry("localhost", 2000);
            l = (LogicalWorkerInteraface) r.lookup("solvingTasks");
            //Creating Worker with ID 2
            int id = 2;
            w2 = new WorkerImp(id);
            //Registering to workers Array
            l.RegisteringWorker(w2);
            //Get tasks from Logical to solve
            Queue<Task> queueOfTasks;
            while (true) {
                queueOfTasks = null;
                do {
                    queueOfTasks = l.workerGetTasks(id);
                } while (queueOfTasks == null);
                numberOfTotalTasks += queueOfTasks.size();
                g2.setStatues("In Progress");
                Task headOfQueue;
                int oldNumberOfTasks = queueOfTasks.size();
                while (queueOfTasks.size() > 0) {
                    headOfQueue = queueOfTasks.remove();
                    g2.putTasks(queueOfTasks.size(), oldNumberOfTasks - queueOfTasks.size(), numberOfTotalTasks);
                    //Check Task type and solve it
                    if (headOfQueue.getTaskType() == 1) {
                        //if task from type 1 , it requires checking number is prime
                        //Worker solves the function and return bool value
                        boolean res = w2.calculatePrime(headOfQueue.getNumberToCalculate()[0]);
                        //Then convert boolean to integer to can store in taskResult if true=1 ,if false=0
                        int boolToIntege = res ? 1 : 0;
                        //Updating task result and mark it as solved
                        headOfQueue.setResult(boolToIntege);
                        headOfQueue.setSolved(true);
                    } else if (headOfQueue.getTaskType() == 2) {
                        int totalOfSum = w2.calculateSum(headOfQueue.getNumberToCalculate()[0], headOfQueue.getNumberToCalculate()[1], headOfQueue.getNumberToCalculate()[2]);
                        //Updating task result and mark it as solved
                        headOfQueue.setResult(totalOfSum);
                        headOfQueue.setSolved(true);
                    } else if (headOfQueue.getTaskType() == 3) {
                        int numberOfFactors = w2.calculateNumberOfFactors(headOfQueue.getNumberToCalculate()[0]);
                        //Updating task result and mark it as solved
                        headOfQueue.setResult(numberOfFactors);
                        headOfQueue.setSolved(true);
                    }
                    l.returnSolvedTaskToLogical(headOfQueue);
                }
                g2.setStatues("Finished");
            }
        } catch (RemoteException ex) {
            //ُException if worker is built before logical
            g2.setVisible(false);
            JOptionPane.showMessageDialog(null, "You Should run Logical Switch then Worker 1,2,3 then Client");
            System.exit(0);
        } catch (NotBoundException ex) {
            System.out.println(ex);
        }
    }
}
