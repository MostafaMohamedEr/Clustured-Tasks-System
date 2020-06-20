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
import javax.swing.JOptionPane;

public class ClientSide {

    private static Gui clientGui;
    public static boolean generates = false;
    public static int taskCounter = 0;
    public static String solvedTasksString = "";

    public static void main(String[] args) throws RemoteException, NotBoundException {
        int recivedTasks = 0;
        Random numberGenrator = new Random();
        clientGui = new Gui();
        clientGui.setVisible(true);
        Registry reg;
        try {
            //Getting logicalSwitchInterface
            reg = LocateRegistry.getRegistry("localhost", 2000);
            LogicalClientInterface Cli = (LogicalClientInterface) reg.lookup("solvingTasks");
            while (true) {
                //Check if the three workers is connected
                if (Cli.checkWorkers() < 3) {
                    clientGui.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Three workers must be connected then run client again");
                    System.exit(0);
                } else if (Cli.checkWorkers() > 3) {
                    clientGui.setVisible(false);
                    JOptionPane.showMessageDialog(null, "There is worker that is built twice , please close one of them");
                    System.exit(0);
                }
                //Generating tasks that required to implement
                //Genrating tasks runs when pressing at button generator 
                while (generates) {
                    Task t = null;
                    //Generate Type between {0,1,2} than adds 1 to be in range{1,2,3}
                    int type = numberGenrator.nextInt(3) + 1;
                    //Then Genrating numbers that will be solved in the function
                    //If type 2 then function requires 3 numbers
                    if (type == 2) {
                        int start = numberGenrator.nextInt(1000);
                        int end = numberGenrator.nextInt(1000);
                        //conditions to ensure that end is bigger than start, so it won't inifinte loop
                        while (end < 10) {
                            end = numberGenrator.nextInt(1000);
                        }
                        while (start == end || start > end) {
                            start = numberGenrator.nextInt(1000);
                        }
                        int incremental = numberGenrator.nextInt(10);
                        //Condition to ensure that incremntal >0
                        while (incremental <= 0) {
                            incremental = numberGenrator.nextInt(10);
                        }
                        int arr[] = {start, end, incremental};
                        t = new Task(taskCounter + 1, type, arr);
                        taskCounter++;
                    } else {
                        //In case type 1 or 3 function requires only 1 number
                        int number = numberGenrator.nextInt(1000);
                        int arr[] = {number};
                        t = new Task(taskCounter + 1, type, arr);
                        taskCounter++;
                    }
                    clientGui.setNumberOfGeneratedTasks(taskCounter);
                    Cli.TaskToLogical(t);
                    //waiting all tasks to be solved and sent from logical
                    Task updatedTask;
                    do {
                        updatedTask = Cli.returnTaskResultToClient();
                    } while (updatedTask == null);
                    //Then adding task result to print
                    solvedTasksString += updatedTask.toString();
                    clientGui.setResult(solvedTasksString);
                    recivedTasks++;
                }
            }
        } catch (RemoteException ex) {
            //Showing error window in case of running Client before logical switch
            clientGui.setVisible(false);
            JOptionPane.showMessageDialog(null, "You Should run Logical Switch then Worker 1,2,3 then Client");
            System.exit(0);
        } catch (NotBoundException ex) {
            System.out.println(ex);
        }

    }

}
