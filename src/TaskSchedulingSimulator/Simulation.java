package TaskSchedulingSimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
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
    private final ArrayList<PeriodicTask> input;
    private final SimulatorLogger logger;
    private final Comparator<InstanceOfPeriodicTask> comparator;
    private final SimulationTypes typeOfSimulation;
    private final ArrayList<InstanceOfPeriodicTask> readyQ;
    private int time;

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

        this.readyQ = new ArrayList<>();
        this.typeOfSimulation = inputType;
        this.logger = new SimulatorLogger(outputFileName);
        this.comparator = pComparator;
        this.input = new ArrayList<>();
        readInputFromFile(new File(inputFileName));
        time = getMinPhiFromInput();
    }

    /**
     * Search for next activation time of ANY task (both higher and lower
     * priority)
     *
     * @return next time of activation of any instance from input after time
     * @param input set of periodic tasks
     * @param time
     */
    private int getNextActivationTime() {
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
     * adds instances of periodic task that activate at time = time to readyQ,
     * and sorts instances in readyQ by priority
     *
     * @param time
     * @param readyQ
     * @param input
     */
    private void updateReadyQ() {

        //for each task from input set of periodic tasks
        for (PeriodicTask temp : input) {

            //check if instance should be activated
            if (time - temp.getPhi() >= 0
                    && (time - temp.getPhi()) % temp.getTaskPeriod() == 0) {

                //add instance to readyQ 
                readyQ.add(new InstanceOfPeriodicTask(temp, time));
            }
        }
        //sort readyQ with appropriate comparator for the chosen algorithm
        Collections.sort(readyQ, comparator);
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

        //repeat until the end of simulation time
        while (time < endOfTimePeriod) {

            //activate all tasks that need to be activated at current time
            updateReadyQ();

            //if there are no active instances in readyQ at this time, 
            //fast forward to the next activation time
            if (readyQ.isEmpty()) {
                time = getNextActivationTime();
            } 
            //if there are some active instances in readyQ
            else {
                //get the highest priority instance (first in sorted readyQ)
                InstanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
                
                //get the next event that simulation should fast forward to
                int nextStop = getNextActivationTime();
                
                //add start of execution time to current instance
                highestPriorityInstance.addStartTimeOfExecution(time);
                
                //time to be executed in this iteration:
                //if instance CAN be finished before next stop
                //execution length is all the remaining time of execution
                //for this instance; if it CAN'T than calculate how much 
                //can be executed
                int executionLength = 
                        time + highestPriorityInstance.getRemainingExeTime() <= nextStop ?
                        highestPriorityInstance.getRemainingExeTime() :
                        nextStop - time;
                
                //execute task, fast forward time to the end of execution
                time += executionLength;

                //subtract time executed from current instance execution time
                highestPriorityInstance.subtractFromRemainingExeTime(executionLength);

                //add end of execution time to current instance
                highestPriorityInstance.addEndTimeOfExecution(time);
 
                //check whatever
                if (time > highestPriorityInstance.getdAbsoluteDeadline()
                            && !highestPriorityInstance.missedDeadline()) {
                        
                        highestPriorityInstance.setMissedDeadline();

                        if (typeOfSimulation == SimulationTypes.HARD) {
                            logger.log(highestPriorityInstance);
                            printNotFeasible(highestPriorityInstance);
                            logger.saveLogToFile();
                            return;
                        }
                }
                
                if (highestPriorityInstance.isFinished()) {
                    //log current instance
                    logger.log(highestPriorityInstance);

                    //and remove it from readyQ
                    readyQ.remove(0);
                }
            }

            //check if some instance with lower priority missed deadline
            if (checkForMissedHardDeadline() == true) {
                //unsuccessfully end simulate and save log to file
                logger.saveLogToFile();
                return; //return false;
            }
        } //END OF SIMULATION

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
                int deadline = scan.nextInt();
                int cTaskExecutionTime = 0;
                String executionTimeType = scan.next();
                PeriodicTask temp = null;

                switch (executionTimeType) {
                    case "FIXED":
                        cTaskExecutionTime = scan.nextInt();
                        temp = new PeriodicTaskFixed(id, taskPeriod, deadline, phi, cTaskExecutionTime);
                        break;
                    case "MIN_MAX_UNIFORM":
                        int cMin = scan.nextInt();
                        int cMax = scan.nextInt();
                        int randomDistribution = scan.nextInt(); // not used
                        temp = new PeriodicTaskMinMaxUniform(id, taskPeriod, deadline, phi, cMin, cMax, randomDistribution);
                        break;
                    case "FREQUENCY_TABLE":
                        int noOfEntries = scan.nextInt();
                        int cumulativeProbability = 0;
                        Map<Integer, Integer> freqTable = new TreeMap<>();

                        /* populate hashmap with the frequency table 
                         with cumulative probabilies, e.g., probabilities 
                         for a group of three tasks are 10%, 50%, 40%, but the 
                         hashmap contains 10,60,100 */
                        for (int iCount = 0; iCount < noOfEntries; iCount++) {
                            int execTime = scan.nextInt();
                            cumulativeProbability += scan.nextInt();
                            freqTable.put(execTime, cumulativeProbability);
                        }

                        temp = new PeriodicTaskFrequencyTable(id, taskPeriod, deadline, phi, freqTable);
                        break;
                }

                //PeriodicTask temp = new PeriodicTask(id, taskPeriod, phi, cTaskExecutionTime);
                input.add(temp);
            }

            endOfTimePeriod = scan.nextInt();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        } catch (InputMismatchException e){
            System.out.println("Input mismatch!");}
    }

    private boolean checkForMissedHardDeadline() {
        boolean anyMissedHard = false;
        Iterator<InstanceOfPeriodicTask> it = readyQ.iterator();

        //check every instance in readyQ to see if some of them missed 
        //their deadlines
        while (it.hasNext()) {

            InstanceOfPeriodicTask temp = it.next();

            if (temp.getdAbsoluteDeadline() <= time) {

                temp.setMissedDeadline();

                //print that current instance is not feasible
                if (this.typeOfSimulation == SimulationTypes.HARD) {
                    logger.log(temp);
                    printNotFeasible(temp);
                    anyMissedHard = true;
                }

            }
        }

        return anyMissedHard;
    }
}
