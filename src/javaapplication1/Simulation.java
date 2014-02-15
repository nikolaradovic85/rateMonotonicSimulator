package javaapplication1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulation class contains all necessary data to perform a task scheduling
 * simulation for periodic tasks, using Rate Monotonic, Earliest Deadline First
 * or Deadline Monotonic (these three are implemented right now).
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class Simulation {
    
    private int endOfTimePeriod;
    private ArrayList<PeriodicTask> input;
    private final SimulatorLogger logger;
    private final Comparator<InstanceOfPeriodicTask> comparator;

    /**
     * Constructor for Simulation.
     * 
     * @param inputFileName Path to input file
     * @param outputFileName Path to output file
     * @param pComparator
     */
    public Simulation(
            String inputFileName, 
            String outputFileName, 
            Comparator<InstanceOfPeriodicTask> pComparator) {
        
        this.logger = new SimulatorLogger(outputFileName);
        this.comparator = pComparator;
        this.input = new ArrayList<>();
        simpleReadInputFromFile(new File(inputFileName));
    }
    
    /**
     * Search for next activation time of ANY task (both higher and lower
     * priority)
     *
     * @return next time of activation of any instance from input after time
     * @param input set of periodic tasks
     * @param time
     */
    private int getNextActivationTime(int time) {
        int nextActivationTime = time + 1;
        boolean found = false;
        while (!found) {
            for (PeriodicTask temp : input) {
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
     * checks if any task from readyQ missed own deadline in time period
     * [0,time]
     *
     * @param readyQ
     * @param time
     * @return false if every instance meets deadline, otherwise true
     */
    private static boolean checkForMissedDeadline(
            ArrayList<InstanceOfPeriodicTask> readyQ, int time) {
        
        for (InstanceOfPeriodicTask temp : readyQ) {
            if (temp.getdAbsoluteDeadline() < time) {
                
                //print that current instance is not feasible
                printNotFeasible(temp);

                //add the end time as the time of this instance's deadline
                temp.addEndTimeOfExecution(temp.getdAbsoluteDeadline());
                
                //TODO temp.setMissedDeadline();
                return true;
            }
        }
        return false;
    }
    
    /**
     * adds instances of periodic task that activate at time = time to readyQ,
     * and sorts instances in readyQ by priority
     *
     * @param time
     * @param readyQ
     * @param input
     */
    private void updateReadyQ(
            int time,
            ArrayList<InstanceOfPeriodicTask> readyQ) {

        //for each task from input set of periodic tasks
        for (PeriodicTask temp : input) {

            //check if instance should be activated
            if (time - temp.getPhi() >= 0
                    && (time - temp.getPhi()) % temp.getTaskPeriod() == 0) {

                //create instance
                InstanceOfPeriodicTask tempInstance;
                tempInstance = new InstanceOfPeriodicTask(
                        temp.getId(),
                        temp.getTaskPeriod(),
                        temp.getPhi(),
                        time,
                        temp.getTaskPeriod() + time,
                        temp.getcTaskExecutionTime());

                //add instance to readyQ 
                readyQ.add(tempInstance);
            }
        }
    }
    
    private static void printNotFeasible(InstanceOfPeriodicTask instance) {
        System.out.println(
                "NOT FEASIBLE! Task " +  
                instance.getId() + 
                " missed deadline at " + 
                instance.getdAbsoluteDeadline() + 
                ".");
    }
    
    private int getMinPhiFromInput() {
        int minPhi = input.get(0).getPhi();
        for (PeriodicTask temp : input) {
            if (temp.getPhi() < minPhi) {
                minPhi = temp.getPhi();
            }
        }
        return minPhi;
    }
    
    /**
     * Simulates periodic task scheduling.
     *
     * @return true if input is feasible in time interval [0,endOfPeriodicTask]
     */
    public boolean simulate() {
        
        //sorting input by priority (highest priority first)
        Collections.sort(input);

        //create empty readyQ
        ArrayList<InstanceOfPeriodicTask> readyQ = new ArrayList<>();

        //set start time to the first phase
        int time = getMinPhiFromInput();

        //repeat until time the end of simulate
        while (time < endOfTimePeriod) {

            //activate all tasks that need to be activated at current time
            updateReadyQ(time, readyQ);

            //sort readyQ with appropriate comparator for the chosen algorithm
            Collections.sort(readyQ, comparator);
            
            // find next activation of ANY task
            int timeOfNextInstanceActivation = getNextActivationTime(time);

            //if there are no active instances in readyQ at this time, 
            //jump to the next activation time
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else { //if there are some active instances in readyQ

                //get the highest priority instance (first in sorted readyQ)
                InstanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);

                //if instance with the highest priority misses its own deadline
                if (time + highestPriorityInstance.getcExecutionTime()
                        > highestPriorityInstance.getdAbsoluteDeadline()) {
                    
                    //print that the current instance is not feasible
                    printNotFeasible(highestPriorityInstance);
                    
                    //add start and end times to the instance
                    highestPriorityInstance.addStartTimeOfExecution(time);
                    highestPriorityInstance.addEndTimeOfExecution(highestPriorityInstance.getdAbsoluteDeadline());
                    
                    //TODO highestPriorityInstance.setMissedDeadline();
                    
                    //log current instance, end the simulate method unsuccessfully
                    //and save log to file
                    logger.log(highestPriorityInstance);
                    logger.saveLogToFile();
                    return false;
                }

                //if instance with highest priority can be executed before any 
                //other task activates
                if (time + highestPriorityInstance.getcExecutionTime()
                        <= timeOfNextInstanceActivation) {
                    
                    highestPriorityInstance.addStartTimeOfExecution(time);
                    
                    //execute task, set time to the end of execution
                    time += highestPriorityInstance.getcExecutionTime();
                    
                    highestPriorityInstance.addEndTimeOfExecution(time);
                    
                    //log current instance
                    logger.log(highestPriorityInstance);
                    
                    //and remove it from readyQ
                    readyQ.remove(0);
                } 
                //if instance with highest priority cannot be executed
                //before any other task activates
                else {
                    //execute task until activation of some other task
                    highestPriorityInstance.setcExecutionTime(highestPriorityInstance.getcExecutionTime()
                            - (timeOfNextInstanceActivation - time));
                    
                    //set times
                    highestPriorityInstance.addStartTimeOfExecution(time);
                    time = timeOfNextInstanceActivation;
                    highestPriorityInstance.addEndTimeOfExecution(time);
                }

                //check if some instance with lower priority missed deadline
                if (checkForMissedDeadline(readyQ, time) == true) {
                    
                    //unsuccessfully end simulate and save log to file
                    logger.saveLogToFile();
                    return false;
                }
            }
        }
        
        for (InstanceOfPeriodicTask temp : readyQ) {
            if (temp.checkIfStillBeingExecuted() == true) {
                temp.addEndTimeOfExecution(endOfTimePeriod);
            }
            logger.log(temp);
        }
        
        //successfully end simulate and save log to file
        logger.saveLogToFile();
        System.out.println("FEASIBLE!");
        return true;
    }
    
    /**
     * Manual user input. 
     */
    /*private void userInput() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Number of periodic tasks: ");
        final int numberOfPeriodicTasks = scan.nextInt();

        for (int i = 0; i < numberOfPeriodicTasks; i++) {
            System.out.println((i + 1) + ". periodic task:");
            System.out.print("Phase: ");
            int phi = scan.nextInt();
            System.out.print("Period: ");
            int taskPeriod = scan.nextInt();
            System.out.print("Execution Time: ");
            int cTaskExecutionTime = scan.nextInt();
            PeriodicTask temp = new PeriodicTask(i + 1, taskPeriod, phi, cTaskExecutionTime);
            input.add(temp);
        }

        System.out.print("End of time period: ");
        endOfTimePeriod = scan.nextInt();
    }*/
    
    /**
     * Reads a file and populates input, execution time calculated from uniform
     * distribution
     *
     * @param f file which is parsed
     */
    private void readInputFromFile(File f) {
        try {
            Scanner scan = new Scanner(f);

            int numberOfPeriodicTasks = scan.nextInt();

            for (int i = 0; i < numberOfPeriodicTasks; i++) {
                int id = scan.nextInt();
                int phi = scan.nextInt();
                int taskPeriod = scan.nextInt();
                int cMin = scan.nextInt();
                int cMax = scan.nextInt();
                int randomDistribution = scan.nextInt(); // not used
                //at this time uniform distribution is used - Math.random()
                int cTaskExecutionTime = cMin + (int) (Math.random() * (cMax - cMin + 1));
                System.out.println(cTaskExecutionTime);
                PeriodicTask temp = new PeriodicTask(id, taskPeriod, phi, cTaskExecutionTime);
                input.add(temp);
            }

            endOfTimePeriod = scan.nextInt();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        }

    }

    /**
     * Reads a file and populates input
     *
     * @param f file which is parsed
     */
    private void simpleReadInputFromFile(File f) {
        
        try {
            Scanner scan = new Scanner(f);

            int numberOfPeriodicTasks = scan.nextInt();

            for (int i = 0; i < numberOfPeriodicTasks; i++) {
                int id = scan.nextInt();
                int phi = scan.nextInt();
                int taskPeriod = scan.nextInt();
                int cTaskExecutionTime = scan.nextInt();
                PeriodicTask temp = new PeriodicTask(id, taskPeriod, phi, cTaskExecutionTime);
                input.add(temp);
            }

            endOfTimePeriod = scan.nextInt();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        }
    }
}