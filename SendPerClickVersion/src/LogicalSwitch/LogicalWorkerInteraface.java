/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicalSwitch;

import Worker.WorkerInterface;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Queue;

/**
 *
 * @author Mostafa
 */
public interface LogicalWorkerInteraface extends Remote{
    public void RegisteringWorker(WorkerInterface worker) throws RemoteException; 
    public void RemoveWorker(WorkerInterface worker)throws RemoteException;
    public Queue<Task> workerGetTasks(int workerID) throws RemoteException;
    public void returnSolvedTaskToLogical(Task t)throws RemoteException;
}
