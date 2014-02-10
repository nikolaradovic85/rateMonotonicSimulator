/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author nikola
 */
public class Main {

    public static void generateInput(ArrayList<periodicTask> input, int numberOfPeriodicTasks) {
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < numberOfPeriodicTasks; i++) {
            System.out.println((i + 1) + ". periodic task:");
            System.out.print("Phase: ");
            int phi = scan.nextInt();
            System.out.print("Period: ");
            int taskPeriod = scan.nextInt();
            System.out.print("Execution Time: ");
            int cTaskExecutionTime = scan.nextInt();
            periodicTask temp = new periodicTask(taskPeriod, phi, cTaskExecutionTime);
            input.add(temp);
        }
    }

    public static int getMinPhi(ArrayList<periodicTask> input) {
        int minPhi = input.get(0).getPhi();
        for (periodicTask temp : input) {
            if (temp.getPhi() < minPhi) {
                minPhi = temp.getPhi();
            }
        }
        return minPhi;
    }
    /**
     *adds instances of periodic task that activate at time = time to readyQ,
     * and sorts instances in readyQ by priority
     */
    public static void updateReadyQ(int time, ArrayList<instanceOfPeriodicTask> readyQ,
            ArrayList<periodicTask> input) {
        //for each task from input set of periodic tasks
        for (periodicTask temp : input) {
            //check if instance should be activated
            if (time - temp.getPhi() >= 0 && (time - temp.getPhi()) % temp.getTaskPeriod() == 0) {
                //create instance
                instanceOfPeriodicTask tempInstance = new instanceOfPeriodicTask(temp.getTaskPeriod(), temp.getPhi(), time,
                        temp.getTaskPeriod() + time, temp.getcTaskExecutionTime());
                //add instance to readyQ 
                readyQ.add(tempInstance);
            }
        }
        //at the end sort readyQ by priority(highest first,priority - rate)
        Collections.sort(readyQ, instanceOfPeriodicTask.Comparators.TASK_PERIOD);
    }
    /**
     * @return next time of activation of any instance from input after time
     * @param input set of periodic tasks 
     * @param time
     */
    public static int getNextActivationTime(ArrayList<periodicTask> input, int time) {
        int nextActivationTime = time + 1;
        boolean found = false;
        while (!found) {
            for (periodicTask temp : input) {
                if ((nextActivationTime - temp.getPhi()) % temp.getTaskPeriod() == 0) {
                    found = true;
                }
            }
            if (!found) {
                nextActivationTime++;
            }
        }
        return nextActivationTime;
    }

    /**
     * checks if any task from readyQ missed own deadline in time period [0,time]
     * @return false if every instance meets deadline, otherwise true
     */
    public static boolean checkForMissedDeadline(
            ArrayList<instanceOfPeriodicTask> readyQ, int time){
        for(instanceOfPeriodicTask temp : readyQ){
            if(temp.getdAbsolutDeadline()>time)
                return true;
        }
        return false;
    }
    /**
     * simulates rate monotonic scheduling
     *
     * @param input - input array list of periodic tasks
     * @param endOfTimePeriod - end of simulation period, implied begin of
     * simulation = 0
     * @return true if input is feasible in time interval [0,endOfPeriodicTask]
     */
    public static boolean rmSimulation(int endOfTimePeriod, ArrayList<periodicTask> input) {
        //sorting input by priority (highest priority first)
        Collections.sort(input);
        ArrayList<instanceOfPeriodicTask> readyQ = new ArrayList<instanceOfPeriodicTask>();
        int time = getMinPhi(input);
        while (time < endOfTimePeriod) {
            updateReadyQ(time, readyQ, input);
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);
            //if there is no active instances, jump to time of next activation
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else {
                instanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
                //if instance with highest priority misses own deadline
                if (time + highestPriorityInstance.getcExecutionTime()
                        > highestPriorityInstance.getdAbsolutDeadline()) {
                    System.out.println("NOT FEASIBLE! ( deadline passed at: "
                            + highestPriorityInstance.getdAbsolutDeadline() + " )");
                    return false;
                }
                //if  instance with highest priority can be executed before any 
                //other tasks activate
                if (time + highestPriorityInstance.getcExecutionTime()
                        <= timeOfNextInstanceActivation) {
                    //dodati log
                    //execute task, set time to the end of execution
                    time += highestPriorityInstance.getcExecutionTime();
                    //remove task from readyQ
                    readyQ.remove(0);
                } else {
                    //execute task untill activation of some other task
                    highestPriorityInstance.setcExecutionTime(highestPriorityInstance.getcExecutionTime()
                            - (timeOfNextInstanceActivation - time));
                    //set time
                    time = timeOfNextInstanceActivation;
                }
                //check if some instance with lower priority missed deadline
                if (checkForMissedDeadline(readyQ, time) == true) {
                    System.out.println("NOT FEASIBLE! ( deadline passed at: "
                            + time + " )");
                    return false;
                }
                

            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Number of periodic tasks: ");
        final int numberOfPeriodicTasks = scan.nextInt();
        ArrayList<periodicTask> input = new ArrayList<periodicTask>();
        generateInput(input, numberOfPeriodicTasks);
        System.out.print("End of time period: ");
        final int endOfTimePeriod = scan.nextInt();
        boolean feasibilityTest = rmSimulation(endOfTimePeriod, input);
        if (feasibilityTest == true) {
            System.out.println("FEASIBLE!");
        }
    }

}
