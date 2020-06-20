/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import LogicalSwitch.LogicalClientInterface;
import LogicalSwitch.Task;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class ClientSide {

    private static Gui clientGui;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        clientGui = new Gui();
        clientGui.setVisible(true);
    }
}
