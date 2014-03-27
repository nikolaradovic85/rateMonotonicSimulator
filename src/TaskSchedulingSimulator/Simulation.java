package TaskSchedulingSimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    private Map<String,Integer> activationTimes;

    /**
     * Constructor for Simulation.
     *
     * @param threadName Name of the thread, passed to super constructor
     * @param inputType Simulation type
     * @param inputFileName Path to input file
     * @param outputFileName Path to output file
     * @param pComparator
     * input (array of periodic tasks) and activationTimes are populated in readInputFromFile()
     * activationTimes in beginning consists phase of each periodic task.
     * time is set on minimum value from activationTimes, so it's set on minimum phase of all periodic tasks,
     * which is first activation of any periodic task. During simulation activation times
     * updates whenever we create new instance of periodic task. We set activation time of that periodic task 
     * that "produces" this new instance on finish time of instance. This is reason why activationTimes is implemented
     * trough HashMap(String, Integer) where key string stores periodic task id, and integer value stores end of last added
     * instance time period.
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
        this.activationTimes = new HashMap<>();
        readInputFromFile(new File(inputFileName));
        time = getNextActivationTime();
    }

    /**
     * Search for next activation time of ANY task (both higher and lower
     * priority). Scans whole activationTime map and returns minimum value, which is 
     * next activation time.
     *
     * @return next time of activation of any instance from input after time
     * @param input set of periodic tasks
     * @param time
     */
    private int getNextActivationTime() {
        int nextActivation = Integer.MAX_VALUE;
        for(Map.Entry<String,Integer> entry : activationTimes.entrySet()){
            if(entry.getValue().compareTo(nextActivation) < 0 ){
                nextActivation = entry.getValue();
            }
        }
        return nextActivation;
    }

    /**
     * adds instances of periodic task that activate at time = time to readyQ,
     * and sorts instances in readyQ by priority. Scans activationTime map and 
     * creates instances of periodic tasks if activation time of that periodic task
     * is equal time. Also, after instance insertion , changes activation time
     * of periodic task to the end of time period of inserted instance.
     *
     * @param time
     * @param readyQ
     * @param input
     */
    private void updateReadyQandActivationTimes() {
        //for every periodic task
        for(Map.Entry<String,Integer> entry : activationTimes.entrySet()){
            //check if should be activated in this moment
            if(entry.getValue().equals(time) ){
                //create instance from periodic task
                InstanceOfPeriodicTask temp = new InstanceOfPeriodicTask(getPeriodicTaskByID(entry.getKey()),time);
                //add instance to readyQ
                readyQ.add(temp);
                //set activation time for periodic task to the end of instance time period 
                entry.setValue(temp.getrActivationTime()+temp.getTaskPeriod());
            }
        }
        //sort readyQ with appropriate comparator for the chosen algorithm
        Collections.sort(readyQ, comparator);
    }

    /**
     * 
     * @param id - id of periodic task
     * @return periodic task with that id
     */
    private PeriodicTask getPeriodicTaskByID(String id){
        for(PeriodicTask temp : this.input){
            if(temp.getId().equals(id))
                return temp;
        }
        return null;
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


    /**
     * Simulates periodic task scheduling.
     */
    @Override
    public void run() {

        //repeat until the end of simulation time
        while (time < endOfTimePeriod) {

            //activate all tasks that need to be activated at current time
            updateReadyQandActivationTimes();

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
            if (checkForMissedDeadlineInReadyQ() == true) {
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
     * Reads a file and populates input and activationTime
     *
     * @param f file which is parsed
     */
    private void readInputFromFile(File f) {
        try {
            Scanner scan = new Scanner(f);

            int numberOfPeriodicTasks = scan.nextInt();

            for (int i = 0; i < numberOfPeriodicTasks; i++) {
                String id = scan.next();
                int phi = scan.nextInt();
                //set phi as first activation time of periodic task
                activationTimes.put(id,phi);
                String taskPeriodType = scan.next();
                Map<Integer, Integer> periodMap = new TreeMap<>();
                
                switch (taskPeriodType) {
                    case "FIXED":
                        int cTaskExecutionTime = scan.nextInt();
                        periodMap.put(1, cTaskExecutionTime);
                        break;
                    case "MIN_MAX_UNIFORM":
                        int cMin = scan.nextInt();
                        int cMax = scan.nextInt();
                        int randomDistribution = scan.nextInt(); // not used
                        periodMap.put(1,cMin);
                        periodMap.put(2,cMax);
                        periodMap.put(3,randomDistribution);
                        break;
                    case "FREQUENCY_TABLE":
                        int noOfEntries = scan.nextInt();
                        int cumulativeProbability = 0;

                        /* populate hashmap with the frequency table 
                         with cumulative probabilies, e.g., probabilities 
                         for a group of three tasks are 10%, 50%, 40%, but the 
                         hashmap contains 10,60,100 */
                        for (int iCount = 0; iCount < noOfEntries; iCount++) {
                            int execTime = scan.nextInt();
                            cumulativeProbability += scan.nextInt();
                            periodMap.put(execTime, cumulativeProbability);
                        }
                        break;
                }
                
                int deadline = scan.nextInt();
                int cTaskExecutionTime = 0;
                String executionTimeType = scan.next();
                Map<Integer, Integer> executionMap = new TreeMap<>();
                switch (executionTimeType) {
                    case "FIXED":
                        cTaskExecutionTime = scan.nextInt();
                        executionMap.put(1,cTaskExecutionTime);
                        break;
                    case "MIN_MAX_UNIFORM":
                        int cMin = scan.nextInt();
                        int cMax = scan.nextInt();
                        int randomDistribution = scan.nextInt(); // not used
                        executionMap.put(1,cMin);
                        executionMap.put(2,cMax);
                        executionMap.put(3,randomDistribution);
                        break;
                    case "FREQUENCY_TABLE":
                        int noOfEntries = scan.nextInt();
                        int cumulativeProbability = 0;
                        /* populate hashmap with the frequency table 
                         with cumulative probabilies, e.g., probabilities 
                         for a group of three tasks are 10%, 50%, 40%, but the 
                         hashmap contains 10,60,100 */
                        for (int iCount = 0; iCount < noOfEntries; iCount++) {
                            int execTime = scan.nextInt();
                            cumulativeProbability += scan.nextInt();
                            executionMap.put(execTime, cumulativeProbability);
                        }

                        break;
                }
                PeriodicTask temp = new PeriodicTask(id, phi, taskPeriodType,
                        periodMap, deadline, executionTimeType,executionMap );
                input.add(temp);
            }

            endOfTimePeriod = scan.nextInt();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        } catch (InputMismatchException e){
            System.out.println("Input mismatch!");}
    }

    private boolean checkForMissedDeadlineInReadyQ() {
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
