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
    public void TasksArrayToLogical(ArrayList<Task> arr) throws RemoteException {
        tasksToImplement = arr;
    }

    @Override
    //This function sent tasks after solving it to client
    public ArrayList<Task> returnTaskResultToClient() throws RemoteException {
        boolean Checker = true;
        //Checks all tasks if there is a task still did not been solved returns null , untill all taks is solved
        for (Task t : tasksToImplement) {
            if (t.isSolved() == false) {
                Checker = false;
                break;
            }
        }
        if (Checker) {
            ArrayList<Task> solvedTasks = tasksToImplement;
            tasksToImplement = new ArrayList<Task>();
            return solvedTasks;
        } else {
            return null;
        }
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
    public synchronized Queue<Task> workerGetTasks(int workerID) throws RemoteException {
        //Waiting for tasks to come
        if (tasksToImplement.size() == 0) {
            return null;
        }
        Queue<Task> taskQueueu = new LinkedList<Task>();
        //Creates queue of tasks and distrubite task based on worker id
        for (Task t : tasksToImplement) {
            //if worker id is 1 then it adds tasks of id 1 ,4,7 and so on
            if ((t.getTaskID() - 1) % 3 == workerID - 1) {
                //checks if tasks is already sent to worker , then return null not to sent queue more than one time
                if (t.isGoneToWorker()) {
                    return null;
                }
                t.setGoneToWorker(true);
                taskQueueu.add(t);
            }
        }
        return taskQueueu;
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

    //Function to update statues of logical Gui
    public String[] setClientConnectionStatus() {
        if (tasksToImplement.size() == 0) {
            String y = "No Tasks exists";
            String s = "waiting for new connection from client";
            String arr[] = {s, y};
            return arr;
        } else {
            String y = tasksToImplement.size() + " Tasks are being solved";
            String s = "Clients is connected";
            String arr[] = {s, y};
            return arr;
        }
    }

}
