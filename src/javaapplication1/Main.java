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
     * adds instances of periodic task that activate at time = time to readyQ,
     * and sorts instances in readyQ by priority
     *
     * @param time
     * @param readyQ
     * @param input
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
     * checks if any task from readyQ missed own deadline in time period
     * [0,time]
     *
     * @param readyQ
     * @param time
     * @return false if every instance meets deadline, otherwise true
     */
    public static boolean checkForMissedDeadline(
            ArrayList<instanceOfPeriodicTask> readyQ, int time) {
        for (instanceOfPeriodicTask temp : readyQ) {
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
    public static boolean rmSimulation(int endOfTimePeriod, ArrayList<periodicTask> input) {
        //sorting input by priority (highest priority first)
        Collections.sort(input);
        ArrayList<instanceOfPeriodicTask> readyQ = new ArrayList<>();
        int time = getMinPhi(input);
        while (time < endOfTimePeriod) {
            updateReadyQ(time, readyQ, input);
            Collections.sort(readyQ, instanceOfPeriodicTask.Comparators.TASK_PERIOD);
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);
            //if there is no active instances, jump to time of next activation
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else {
                instanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
                //if instance with highest priority misses own deadline
                if (time + highestPriorityInstance.getcExecutionTime()
                        > highestPriorityInstance.getdAbsoluteDeadline()) {
                    System.out.println("RM: NOT FEASIBLE! ( deadline passed at: "
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
                    System.out.println("RM: NOT FEASIBLE! ( deadline passed at: "
                            + time + " )");
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
    public static boolean edfSimulation(int endOfTimePeriod, ArrayList<periodicTask> input) {
        //sorting input by priority (highest priority first)
        Collections.sort(input);
        ArrayList<instanceOfPeriodicTask> readyQ = new ArrayList<>();
        int time = getMinPhi(input);
        while (time < endOfTimePeriod) {
            updateReadyQ(time, readyQ, input);
            Collections.sort(readyQ, instanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
            int timeOfNextInstanceActivation = getNextActivationTime(input, time);
            //if there is no active instances, jump to time of next activation
            if (readyQ.isEmpty()) {
                time = timeOfNextInstanceActivation;
            } else {
                instanceOfPeriodicTask highestPriorityInstance = readyQ.get(0);
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
        
        ArrayList<periodicTask> inputRM = new ArrayList<>();
        Parser.simpleReadInputFromFile(inputRM, new File("simpleInput.txt"));
        boolean feasibilityTestRM = rmSimulation(endOfTime, inputRM);
        if (feasibilityTestRM == true) {
            System.out.println("RM: FEASIBLE!");
        }
        
        ArrayList<periodicTask> inputEDF = new ArrayList<>();
        Parser.simpleReadInputFromFile(inputEDF, new File("simpleInput.txt"));
        boolean feasibilityTestEDF = edfSimulation(endOfTime, inputEDF);
        if (feasibilityTestEDF == true) {
            System.out.println("EDF: FEASIBLE!");
        }
    }

}
