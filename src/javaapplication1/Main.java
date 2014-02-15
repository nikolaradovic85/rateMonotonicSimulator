package javaapplication1;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author nikola
 */
public class Main {
    
    static int endOfTime = 0;
    
    private static void printNotFeasible(InstanceOfPeriodicTask instance) {
        System.out.println(
                "NOT FEASIBLE! Task " +  
                instance.getId() + 
                " missed deadline at " + 
                instance.getdAbsoluteDeadline() + 
                ".");
    }
    
    private static int getMinPhi(ArrayList<PeriodicTask> input) {
        int minPhi = input.get(0).getPhi();
        for (PeriodicTask temp : input) {
            if (temp.getPhi() < minPhi) {
                minPhi = temp.getPhi();
            }
        }
        return minPhi;
    }
    
    /**
     * adds instances of periodic task that activate at time = time to readyQ,
     * and sorts instances in readyQ by priority
     *
     * @param time
     * @param readyQ
     * @param input
     */
    private static void updateReadyQ(
            int time,
            ArrayList<InstanceOfPeriodicTask> readyQ,
            ArrayList<PeriodicTask> input) {

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

    /**
     * Search for next activation time of ANY task (both higher and lower
     * priority)
     *
     * @return next time of activation of any instance from input after time
     * @param input set of periodic tasks
     * @param time
     */
    private static int getNextActivationTime(ArrayList<PeriodicTask> input, int time) {
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
     * Simulates periodic task scheduling.
     *
     * @param input - input array list of periodic tasks
     * @param endOfTimePeriod - end of simulation period, implied begin of
     * simulation = 0
     * @param comparator Use appropriate comparator for the chosen algorithm
     * from InstanceOfPeriodicTask's inner class Comparators
     * @param logger SimulatorLogger object that takes care of logging
     * @return true if input is feasible in time interval [0,endOfPeriodicTask]
     */
    public static boolean simulation(
            int endOfTimePeriod, 
            ArrayList<PeriodicTask> input, 
            Comparator<InstanceOfPeriodicTask> comparator,
            SimulatorLogger logger) {
        
        //sorting input by priority (highest priority first)
        Collections.sort(input);

        //create empty readyQ
        ArrayList<InstanceOfPeriodicTask> readyQ = new ArrayList<>();

        //set start time to the first phase
        int time = getMinPhi(input);

        //repeat until time the end of simulation
        while (time < endOfTimePeriod) {

            //activate all tasks that need to be activated at current time
            updateReadyQ(time, readyQ, input);

            //sort readyQ with appropriate comparator for the chosen algorithm
            Collections.sort(readyQ, comparator);
            
            // find next activation of ANY task
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);

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
                    
                    //log current instance and end the simulation
                    logger.log(highestPriorityInstance);
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
                    //correct me if i'm wrong, but TODO nothing at all, everythig is allready done
                    
                    //end simulation
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
        
        System.out.println("FEASIBLE!");
        return true;
    }
    
    public static void main(String[] args) {
        /* userInput(input);            //for user input
         File f = new File(args[0]);     //for command-line file name input */
        
        ArrayList<PeriodicTask> input = new ArrayList<>();
        Parser.simpleReadInputFromFile(input, new File("inputUp1.txt"));
        
        SimulatorLogger loggerRM  = new SimulatorLogger("trace/rm.log");
        SimulatorLogger loggerEDF = new SimulatorLogger("trace/edf.log");
        SimulatorLogger loggerRM2 = new SimulatorLogger("trace/rm2.log");
        
        boolean feasibilityTestRM  = simulation(endOfTime, input, InstanceOfPeriodicTask.Comparators.TASK_PERIOD, loggerRM);
        boolean feasibilityTestEDF = simulation(endOfTime, input, InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE, loggerEDF);
        boolean feasibilityTestRM2 = simulation(endOfTime, input, InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE, loggerRM2);
        
        loggerRM.saveLogToFile();
        loggerEDF.saveLogToFile();
        loggerRM2.saveLogToFile();
    }
    
}
