/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicalSwitch;

import Worker.WorkerInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LogicalClientImp extends UnicastRemoteObject implements LogicalClientInterface, LogicalWorkerInteraface {

    public ArrayList<Task> tasksToImplement;
    public ArrayList<WorkerInterface> workers;

    LogicalClientImp() throws RemoteException {
        tasksToImplement = new ArrayList<Task>();
        workers = new ArrayList<WorkerInterface>();
    }

    //Functions of client Interface
    @Override
    //This Function is used by client to check if the 3 workers is connected
    public int checkWorkers() throws RemoteException {
        int counter = 0;
        boolean Checker = false;
        //ArrayList that checks if there are workers with same id in case of(we run same worker more than 1)
        ArrayList<Integer> workersIDChecker = new ArrayList<Integer>();
        for (WorkerInterface t : workers) {
            if (workersIDChecker.contains(t.getWorkerID())) {
                Checker = true;
                continue;
            } else {
                workersIDChecker.add(t.getWorkerID());
                counter++;
            }
        }
        if (Checker && counter == 3) {
            counter++;
        }
        return counter;
    }

    @Override
    //This function reads Tasks that sent from client
    public synchronized void TaskToLogical(Task t) throws RemoteException {
        tasksToImplement.add(t);
    }

    @Override
    //This function sent tasks after solving it to client
    public synchronized Task returnTaskResultToClient() throws RemoteException {
        Task Returned = null;
        for (Task t : tasksToImplement) {
            if (t.Solved) {
                Returned = t;
                break;
            }
        }
        if (Returned != null) {
            tasksToImplement.remove(Returned);
        }
        return Returned;
    }

// Functions of worker interface
    @Override
    //Adding the worker to worker arrayList
    public synchronized void RegisteringWorker(WorkerInterface worker) throws RemoteException {
        workers.add(worker);
    }

    @Override
    //Function to Remove Worker if it closed
    public void RemoveWorker(WorkerInterface matched) throws RemoteException {
        int i = -1;
        for (WorkerInterface w : workers) {
            if (w.getWorkerID() == matched.getWorkerID()) {
                i = workers.indexOf(w);
            }
        }
        if (i != -1) {
            workers.remove(i);
        }
    }

    @Override
    //Function to distribute tasks among workers 
    public synchronized Task workerGetTask(int workerID) throws RemoteException {
        //Waiting for tasks to come
        if (tasksToImplement.size() == 0) {
            return null;
        }
        Task taskTOSent = null;
        //search for first task that is didn't solved and in order of round robin manner
        for (Task t : tasksToImplement) {
            //if worker id is 1 then it adds tasks of id 1,4,7 and so on
            if ((t.getTaskID() - 1) % 3 == workerID - 1) {
                //checks if tasks is already sent to worker , then return null not to sent queue more than one time
                if (t.isGoneToWorker() || t.Solved) {
                    continue;
                }
                t.setGoneToWorker(true);
                taskTOSent = t;
                return t;
            }
        }
        return null;
    }

    @Override
    //Function to assign solved task in the array
    // After solving the task ,worker sent it to logcial one by one
    public synchronized void returnSolvedTaskToLogical(Task t) throws RemoteException {
        int i = -1;
        //get the index of solved task in arrayList
        for (Task search : tasksToImplement) {
            if (search.getTaskID() == t.getTaskID()) {
                i = tasksToImplement.indexOf(search);
                break;
            }
        }
        //replace the task with  task that contain the result 
        tasksToImplement.set(i, t);
    }

    // Functions of logical GUI
    //Function to check number of connected workers
    public String setGuiWorkerFiled() {
        if (workers.size() == 0) {
            return "No workers is connected";
        } else {
            String t = "";
            for (WorkerInterface wo : workers) {
                t += "Worker " + (workers.indexOf(wo) + 1) + " is connected \n";
            }
            return t;
        }
    }

}
