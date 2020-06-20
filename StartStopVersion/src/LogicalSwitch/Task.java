/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicalSwitch;

import java.io.Serializable;

public class Task implements Serializable {

    public int taskID;
    public int taskType;
    public int numberToCalculate[];
    //goneToWorker is a flag to pervent tasks to go to worker twice
    public boolean goneToWorker;
    //solved is a flag to ensure that task is solved
    public boolean Solved;
    public int result;

    public Task(int taskID, int taskType, int arr[]) {
        this.taskID = taskID;
        this.taskType = taskType;
        numberToCalculate = arr;
        Solved = false;
        goneToWorker=false;
        result = -1;
    }

    @Override
    public String toString() {
        if (result == -1) {
            return "Task ID is " + taskID + " TaskType is " + taskType + " The array is " + java.util.Arrays.toString(numberToCalculate) + "\n";
        } else {
            if (taskType == 1) {
                if (result == 1) {
                    return "Task ID is " + taskID + " TaskType is Prime Checking" + " The array is " + java.util.Arrays.toString(numberToCalculate) + " The Result is True\n";
                } else {
                    return "Task ID is " + taskID + " TaskType is Prime Checking" + taskType + " The array is " + java.util.Arrays.toString(numberToCalculate) + " The Result is False\n";
                }
            } else if (taskType == 2) {
                return "Task ID is " + taskID + " TaskType is Calculating Sum The array is " + java.util.Arrays.toString(numberToCalculate) + " The Result is "+result+"\n";
            } else {
                return "Task ID is " + taskID + " TaskType is viewing NumberOfFactors The array is " + java.util.Arrays.toString(numberToCalculate) + " The Result is " + result + "\n";
            }
        }
    }

    public void setNumberToCalculate(int[] numberToCalculate) {
        this.numberToCalculate = numberToCalculate;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setSolved(boolean Solved) {
        this.Solved = Solved;
    }

    public void setGoneToWorker(boolean goneToWorker) {
        this.goneToWorker = goneToWorker;
    }
    

    public int getTaskType() {
        return taskType;
    }

    public int getTaskID() {
        return taskID;
    }

    public int[] getNumberToCalculate() {
        return numberToCalculate;
    }

    public boolean isSolved() {
        return Solved;
    }

    public boolean isGoneToWorker() {
        return goneToWorker;
    }
    
    

}
