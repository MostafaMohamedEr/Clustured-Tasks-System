/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Worker;

import LogicalSwitch.Task;
import java.rmi.Remote;

/**
 *
 * @author Mostafa
 */
public interface WorkerInterface extends Remote{
    public void setTaskToSolve(Task t);
    public Task getTask();
    public int getWorkerID();
    public boolean calculatePrime(int number);
    public int calculateSum(int start,int end,int increment);
    public int calculateNumberOfFactors(int number);
}
