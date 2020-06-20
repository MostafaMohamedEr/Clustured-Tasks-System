/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicalSwitch;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class LogicalSwitchMain {

    /**
     * @param args the command line arguments
     */
    private static LogicalGui g;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        LogicalClientImp obj = new LogicalClientImp();
        g = new LogicalGui();
        g.setVisible(true);
        Registry reg = LocateRegistry.createRegistry(2000);
        reg.bind("solvingTasks", obj);
        while (true) {
            g.setWorkers(obj.setGuiWorkerFiled());
        }
    }

}
