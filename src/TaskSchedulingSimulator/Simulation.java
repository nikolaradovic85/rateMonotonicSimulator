package TaskSchedulingSimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulation class contains all the necessary data to perform a task scheduling
 * simulation for periodic tasks, using Rate Monotonic, Earliest Deadline First
 * or Deadline Monotonic (these three are implemented right now).
 *
 * Simulation extends Thread so that multiple simulations could be run
 * simultaneously, and (hopefully) benefit from multi core CPUs.
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class Simulation extends Thread {

    public enum SimulationTypes {

        SOFT,
        HARD,
        HYBRID
    }

    private int endOfTimePeriod;
    private ArrayList<PeriodicTask> input;
    private final SimulatorLogger logger;
    private final Comparator<InstanceOfPeriodicTask> comparator;
    private final SimulationTypes typeOfSimulation;

    /**
     * Constructor for Simulation.
     *
     * @param threadName Name of the thread, passed to super constructor
     * @param inputType Simulation type
     * @param inputFileName Path to input file
     * @param outputFileName Path to output file
     * @param pComparator
     */
    public Simulation(
            String threadName,
            Simulation.SimulationTypes inputType,
            String inputFileName,
            String outputFileName,
            Comparator<InstanceOfPeriodicTask> pComparator) {

        super(threadName);

        this.typeOfSimulation = inputType;
        this.logger = new SimulatorLogger(outputFileName);
        this.comparator = pComparator;
        this.input = new ArrayList<>();
        readInputFromFile(new File(inputFileName));
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
     * [0,time] and removes these instances from readyQ
     *
     * @param readyQ
     * @param time
     * @return false if every instance meets deadline, otherwise true
     */
    private boolean checkForMissedDeadline(
            ArrayList<InstanceOfPeriodicTask> readyQ, int time) {

        boolean anyMissed = false;
        //check every instance in readyQ to see if some of them missed 
        //their deadlines
        for (InstanceOfPeriodicTask temp : readyQ) {
            
            //tests for less or equal - solves BUG(so far, only tested for less)
            //at moment when checkForMissedDeadline() is called in simulation
            //all instances in readyQ have remaining execution time > 0 (let's pray for this)
            //if temp.deadline == time and temp.remainingexecu...>0 => temp missed deadline
            if (temp.getdAbsoluteDeadline() <= time) {

                //at least one has missed its deadline
                anyMissed = true;

                //print that current instance is not feasible
                if (this.typeOfSimulation == SimulationTypes.HARD) {
                    printNotFeasible(temp);
                }

                //set the missedDeadline property of the instance to the time
                //and log the instance
                temp.setMissedDeadline(temp.getdAbsoluteDeadline());
                logger.log(temp);
                    
                //prevents for loop conditioning when last element(even if it is also only element)
                //of list is removed -- BUG fixed
                if (readyQ.get(readyQ.size() - 1).equals(temp)) {
                    readyQ.remove(temp);
                    return anyMissed;
                }
                readyQ.remove(temp);
            }
        }
        
        return anyMissed;
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

    private void printNotFeasible(InstanceOfPeriodicTask instance) {
        System.out.println(
                this.getName()
                + " NOT FEASIBLE! Task "
                + instance.getId()
                + " missed deadline at "
                + instance.getdAbsoluteDeadline()
                + ".");
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
     */
    @Override
    public void run() {

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

                    //add start and end times to the instance, and add time when
                    //the deadline was missed
                    highestPriorityInstance.addStartTimeOfExecution(time);
                    highestPriorityInstance.addEndTimeOfExecution(highestPriorityInstance.getdAbsoluteDeadline());
                    highestPriorityInstance.setMissedDeadline(time);

                    //log current instance, end the simulate method unsuccessfully
                    logger.log(highestPriorityInstance);

                    if (this.typeOfSimulation == SimulationTypes.HARD) {

                        //print that the current instance is not feasible
                        printNotFeasible(highestPriorityInstance);

                        //and save log to file
                        logger.saveLogToFile();
                        return; //return false;
                    }
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
                } //if instance with highest priority cannot be executed
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
                if (checkForMissedDeadline(readyQ, time) == true && typeOfSimulation == SimulationTypes.HARD) {

                    //unsuccessfully end simulate and save log to file
                    logger.saveLogToFile();
                    return; //return false;
                }
            }
        }

        // check if there are some instances left unfinished after time has elapsed
        for (InstanceOfPeriodicTask temp : readyQ) {
            if (temp.checkIfStillBeingExecuted() == true) {
                temp.addEndTimeOfExecution(endOfTimePeriod);
                logger.log(temp);
            }
        }

        //successfully end simulate and save log to file
        logger.saveLogToFile();
        if (typeOfSimulation == SimulationTypes.HARD) {
            System.out.println(this.getName() + " FEASIBLE!");
            //return true;
        }
    }

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
                int cTaskExecutionTime = 0;

                String executionTimeType = scan.next();

                switch (executionTimeType) {
                    case "FIXED":
                        cTaskExecutionTime = scan.nextInt();
                        break;
                    case "MIN_MAX_UNIFORM":
                        int cMin = scan.nextInt();
                        int cMax = scan.nextInt();
                        int randomDistribution = scan.nextInt(); // not used
                        //currently only uniform distribution is used - Math.random()
                        cTaskExecutionTime = cMin + (int) (Math.random() * (cMax - cMin + 1));
                        break;
                    case "FREQUENCY_TABLE":
                        int noOfEntries = scan.nextInt();
                        int cumulativeProbability = 0;
                        Map<Integer, Integer> freqTable = new HashMap<>();

                        /* populate hashmap with the frequency table 
                         with cumulative probabilies, e.g., probabilities 
                         for a group of three tasks are 10%, 50%, 40%, but the 
                         hashmap contains 10,60,100 */
                        for (int iCount = 0; iCount < noOfEntries; iCount++) {
                            int execTime = scan.nextInt();
                            cumulativeProbability += scan.nextInt();
                            freqTable.put(execTime, cumulativeProbability);
                        }

                        // find a random number [1,100] (uniform distribution)
                        int random100 = (int) Math.ceil(Math.random() * 100);

                        // find its match in the hashmap
                        for (Map.Entry<Integer, Integer> entry : freqTable.entrySet()) {
                            if (random100 <= entry.getValue()) {
                                cTaskExecutionTime = entry.getKey();
                            }
                        }
                        break;
                }

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
