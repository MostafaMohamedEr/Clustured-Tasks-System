/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Worker;

import LogicalSwitch.Task;
import java.io.Serializable;

/**
 *
 * @author Mostafa
 */
public class WorkerImp implements WorkerInterface, Serializable {

    public Task taskToSolve;
    public int workerId;

    public WorkerImp(int id) {
        workerId = id;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    @Override
    public void setTaskToSolve(Task t) {
        taskToSolve = t;
    }

    @Override
    public Task getTask() {
        return taskToSolve;
    }

    @Override
    public int getWorkerID() {
        return workerId;
    }

    @Override
    public boolean calculatePrime(int number) {
        boolean primeChecker = true;
        if (number == 1 || number == 0) {
            primeChecker = false;
            return primeChecker;
        }
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                primeChecker = false;
                break;
            }
        }
        return primeChecker;
    }

    @Override
    public int calculateSum(int start, int end, int increment) {
        int sum = 0;
        for (int i = start; i <= end; i += increment) {
            sum += i;
        }
        return sum;
    }

    @Override
    public int calculateNumberOfFactors(int number) {
        if (number == 0) {
            return 0;
        }
        int counter = 0;
        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                counter++;
            }
        }
        return counter;
    }

}
