/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicalSwitch;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Mostafa
 */
public interface LogicalClientInterface extends Remote{
    public int checkWorkers() throws RemoteException;
    public void TaskToLogical(Task arr) throws RemoteException;
    public Task returnTaskResultToClient ()throws RemoteException;
}
