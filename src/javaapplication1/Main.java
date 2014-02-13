package javaapplication1;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author nikola
 */
public class Main {

    static int endOfTime = 0;

    public static int getMinPhi(ArrayList<PeriodicTask> input) {
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
    public static void updateReadyQ(
            int time, 
            ArrayList<InstanceOfPeriodicTask> readyQ, 
            ArrayList<PeriodicTask> input) {
        
        //for each task from input set of periodic tasks
        for (PeriodicTask temp : input) {
            
            //check if instance should be activated
            if (time - temp.getPhi() >= 0 && 
                    (time - temp.getPhi()) % temp.getTaskPeriod() == 0) {
                
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
                
                //TODO-LOG log activation (or continuation) time for tempInstance
            }
        }
    }

    /**
     * Search for next activation time of ANY task (both higher and lower priority)
     * 
     * @return next time of activation of any instance from input after time
     * @param input set of periodic tasks
     * @param time
     */
    public static int getNextActivationTime(ArrayList<PeriodicTask> input, int time) {
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
    public static boolean checkForMissedDeadline(
            ArrayList<InstanceOfPeriodicTask> readyQ, int time) {
        for (InstanceOfPeriodicTask temp : readyQ) {
            if (temp.getdAbsoluteDeadline() < time) {
                return true;
            }
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
    public static boolean rmSimulation(int endOfTimePeriod, ArrayList<PeriodicTask> input) {
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
            
            //sort readyQ (lowest task period has the highest priority)
            Collections.sort(readyQ, InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
            
            // find next activation of ANY task
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);
            
            //if there are no active instances in readyQ at this time, 
            //jump to the next activation time
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else { //if there are some active tasks in readyQ
                
                //get the highest priority instance (first in sorted readyQ)
                InstanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
                
                //if instance with the highest priority misses its own deadline
                if (time + highestPriorityInstance.getcExecutionTime()
                        > highestPriorityInstance.getdAbsoluteDeadline()) {
                    System.out.println("RM: NOT FEASIBLE! ( deadline passed at: "
                            + highestPriorityInstance.getdAbsoluteDeadline() + " )");
                    return false;
                    //TODO-LOG close log and save to file
                }
                
                //if  instance with highest priority can be executed before any 
                //other task activates
                if (time + highestPriorityInstance.getcExecutionTime()
                        <= timeOfNextInstanceActivation) {
                    
                    //TODO-LOG log finish time for highestPriorityInstance
                    
                    //execute task, set time to the end of execution
                    time += highestPriorityInstance.getcExecutionTime();
                    //remove task from readyQ
                    readyQ.remove(0);
                }
                // if instance with highest priority cannot be executed
                //before any other task activates
                else {
                    //execute task untill activation of some other task
                    highestPriorityInstance.setcExecutionTime(highestPriorityInstance.getcExecutionTime()
                            - (timeOfNextInstanceActivation - time));
                    //set time
                    time = timeOfNextInstanceActivation;
                    
                    //TODO-LOG log time of interupt
                }
                
                //check if some instance with lower priority missed deadline
                if (checkForMissedDeadline(readyQ, time) == true) {
                    System.out.println("RM: NOT FEASIBLE! ( deadline passed at: "
                            + time + " )");
                    
                    //TODO-LOG close log and save to file
                    
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * simulates earliest deadline first scheduling
     *
     * @param input - input array list of periodic tasks
     * @param endOfTimePeriod - end of simulation period, implied begin of
     * simulation = 0
     * @return true if input is feasible in time interval [0,endOfPeriodicTask]
     */
    public static boolean edfSimulation(int endOfTimePeriod, ArrayList<PeriodicTask> input) {
        //sorting input by priority (highest priority first)
        Collections.sort(input);
        ArrayList<InstanceOfPeriodicTask> readyQ = new ArrayList<>();
        int time = getMinPhi(input);
        while (time < endOfTimePeriod) {
            updateReadyQ(time, readyQ, input);
            Collections.sort(readyQ, InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);
            //if there is no active instances, jump to time of next activation
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else {
                InstanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
                //if instance with highest priority misses own deadline
                if (time + highestPriorityInstance.getcExecutionTime()
                        > highestPriorityInstance.getdAbsoluteDeadline()) {
                    System.out.println("EDF: NOT FEASIBLE! ( deadline passed at: "
                            + highestPriorityInstance.getdAbsoluteDeadline() + " )");
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
                    System.out.println("EDF: NOT FEASIBLE! ( deadline passed at: "
                            + time + " )");
                    return false;
                }

            }
        }
        return true;
    }

    public static void main(String[] args) {
        /* userInput(input);            //for user input
        File f = new File(args[0]);     //for command-line file name input */
        
        ArrayList<PeriodicTask> inputRM = new ArrayList<>();
        Parser.simpleReadInputFromFile(inputRM, new File("simpleInput.txt"));
        boolean feasibilityTestRM = rmSimulation(endOfTime, inputRM);
        if (feasibilityTestRM == true) {
            System.out.println("RM: FEASIBLE!");
        }
        
        ArrayList<PeriodicTask> inputEDF = new ArrayList<>();
        Parser.simpleReadInputFromFile(inputEDF, new File("simpleInput.txt"));
        boolean feasibilityTestEDF = edfSimulation(endOfTime, inputEDF);
        if (feasibilityTestEDF == true) {
            System.out.println("EDF: FEASIBLE!");
        }
    }

}
