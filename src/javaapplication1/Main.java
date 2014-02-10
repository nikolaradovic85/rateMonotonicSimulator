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

    public static void updateReadyQ(int time, ArrayList<instanceOfPeriodicTask> readyQ,
            ArrayList<periodicTask> input) {
        for (periodicTask temp : input) {
            if (time-temp.getPhi()>=0&& (time - temp.getPhi()) % temp.getTaskPeriod() == 0 ) {
                instanceOfPeriodicTask tempInstance = new instanceOfPeriodicTask(temp.getTaskPeriod(), temp.getPhi(), time,
                        temp.getTaskPeriod() + time, temp.getcTaskExecutionTime());

                readyQ.add(tempInstance);
            }
        }
        //sortiraj po periodi <=> za RM ekvivalentno prioritetu
        Collections.sort(readyQ);
    }

    public static int getNextActivationTime(ArrayList<periodicTask> input, int time) {
        int nextActivationTime = time + 1;
        boolean found = false;
        while (!found) {
            for (periodicTask temp : input) {
                if ((nextActivationTime - temp.getPhi()) % temp.getTaskPeriod() == 0) {
                    found = true;
                }
            }
            if(!found){
                   nextActivationTime++;
            }
        }
        return nextActivationTime;
    }

    public static boolean rmSimulation(int endOfTimePeriod, ArrayList<periodicTask> input) {
        Collections.sort(input);
        ArrayList<instanceOfPeriodicTask> readyQ = new ArrayList<instanceOfPeriodicTask>();
        int time = getMinPhi(input);
        while (time < endOfTimePeriod) {
            updateReadyQ(time, readyQ, input);
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else {
                instanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
                if (time + highestPriorityInstance.getcExecutionTime()
                        > highestPriorityInstance.getdAbsolutDeadline()) {
                    System.out.println("NOT FEASIBLE! ( deadline passed at: " 
                            + highestPriorityInstance.getdAbsolutDeadline() + " )");
                    return false;
                }
                if (time + highestPriorityInstance.getcExecutionTime()
                        <= timeOfNextInstanceActivation) {
                    //dodati log
                    time += highestPriorityInstance.getcExecutionTime();
                    readyQ.remove(0);
                } else {
                    highestPriorityInstance.setcExecutionTime(highestPriorityInstance.getcExecutionTime()
                            - (timeOfNextInstanceActivation - time));
                    time = timeOfNextInstanceActivation;
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
