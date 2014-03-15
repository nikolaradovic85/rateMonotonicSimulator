package TaskSchedulingSimulator;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author nikola
 */
public class InstanceOfPeriodicTask {
    
    /**
     * Id of the task.
     */
    private int id;
    
    /**
     * Period of periodic task.
     */
    private int taskPeriod;
    
    /**
     * Phase of the task.
     */
    private int phi;
    
    /**
     * Time at which instance is created (and added to readyQ in Simulation.java)
     */
    private int rActivationTime;
    
    /**
     * Absolute deadline of the instance (time of activation + relative deadline
     * from input task set)
     */
    private int dAbsoluteDeadline;
    
    /**
     * Remaining time of execution (changes over time)
     */
    private int remainingExeTime;
    
    /**
     * Instance execution time (doesn't change over time)
     */
    private final int cExecutionTime;
    
    /**
     * Gets set to time at which instances misses its deadline;
     * -1 if instance doesn't miss deadline
     */
    private int missedDeadline;
    
    /**
     * Contains times of any part of instance started being executed
     */
    private final ArrayList<Integer> startOfExecutionTime;
    
    /**
     * Collects time when any part of instance stopped being executed
     */
    private final ArrayList<Integer> endOfExecutionTime;

    /**
     * Constructor.
     *
     * @param task Tells the instance which task it is an instance of.
     * @param pRActivationTime
     */
    public InstanceOfPeriodicTask(PeriodicTask task, int pRActivationTime) {
        this.rActivationTime = pRActivationTime;
        this.id = task.getId();
        this.remainingExeTime = this.cExecutionTime = task.getcTaskExecutionTime();      
        this.dAbsoluteDeadline = task.getDeadline() + rActivationTime;
        this.phi = task.getPhi();
        this.taskPeriod = task.getTaskPeriod();
        this.missedDeadline = -1;
        this.startOfExecutionTime = new ArrayList<>();
        this.endOfExecutionTime = new ArrayList<>();
    }

    /**
     * Checks if instance is finished.
     * 
     * @return true if remaining execution time is 0, false otherwise
     */
    public boolean isFinished() {
        return this.remainingExeTime == 0;
    }
    
    /**
     * missedDeadline is -1 if deadline isn't missed; if deadline is missed
     * missedDeadline contains absolute deadline of the instance
     * 
     * @return true if deadline is missed, false otherwise
     */
    public boolean missedDeadline(){
        return missedDeadline != -1;
    }
    
    /**
     * If ArrayList startOfExecutionTime is one item longer, that means that
     * instance is currently being executed.
     * 
     * @return true if instance is currently being executed, false otherwise
     */
    public boolean checkIfStillBeingExecuted(){
        return this.startOfExecutionTime.size() == this.endOfExecutionTime.size() + 1;
    }
    
    /**
     * Sometimes, an instance is stopped, and it immediately get started again -
     * in these situations we get something like:
     * startOfExecutionTime = [3, 7], endOfExecutionTime = [7, 9];
     * removeRedundantTimesFromLists removes these duplicates so that we get:
     * startOfExecutionTime = [3], endOfExecutionTime = [9]
     */
    public void removeRedundantTimesFromLists() {
        if (startOfExecutionTime.size() > 1 && startOfExecutionTime.size() == endOfExecutionTime.size()) {
            //for each start time except first
            for (int i = 1; i < startOfExecutionTime.size(); i++) {
                //check if is equal to previous end time of execution
                //which means at that time was no interruption
                //so these times are redundant in list
                if (startOfExecutionTime.get(i).equals(endOfExecutionTime.get(i - 1))) {
                    //remove redundant times
                    startOfExecutionTime.remove(i);
                    endOfExecutionTime.remove(i-1);
                    i--;//best fix ever
                }
            }
        }
    }

    /**
     * Returns final end of execution.
     * 
     * @return last element from endOfExecutionTime ArrayList
     */
    private int getLastEndOfExecutionTime() {
        if (this.endOfExecutionTime.isEmpty()) {
            return 0;
        } else {
            return this.endOfExecutionTime.get(this.endOfExecutionTime.size() - 1);
        }
    }
    
    /**
     * Adds time to the list of execution start times
     * 
     * @param time time to be added
     */
    public void addStartTimeOfExecution(int time) {
        this.startOfExecutionTime.add(time);
    }

    /**
     * Adds time to the list of execution ending times
     * 
     * @param time time to be added
     */
    public void addEndTimeOfExecution(int time) {
        this.endOfExecutionTime.add(time);
    }

    /**
     * Subtracts length of time for which the instance was executed from
     * remaining time of execution
     * 
     * @param executedTimeUnits length of time to be subtracted
     */
    public void subtractFromRemainingExeTime(int executedTimeUnits) {
        this.remainingExeTime -= executedTimeUnits;
    }
    
    /**
     * If deadline isn't missed missedDeadline is -1; if it is missed
     * missedDeadline becomes absolute deadline
     */
    public void setMissedDeadline() {
        this.missedDeadline = this.dAbsoluteDeadline;
    }

    /**
     * Getter for id.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Setter for id.
     * 
     * @param id id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the taskPeriod
     */
    public int getTaskPeriod() {
        return taskPeriod;
    }

    /**
     * @param taskPeriod the taskPeriod to set
     */
    public void setTaskPeriod(int taskPeriod) {
        this.taskPeriod = taskPeriod;
    }

    /**
     * @return the phi
     */
    public int getPhi() {
        return phi;
    }

    /**
     * @param phi the phi to set
     */
    public void setPhi(int phi) {
        this.phi = phi;
    }

    /**
     * @return the rActivationTime
     */
    public int getrActivationTime() {
        return rActivationTime;
    }

    /**
     * @param rActivationTime the rActivationTime to set
     */
    public void setrActivationTime(int rActivationTime) {
        this.rActivationTime = rActivationTime;
    }

    /**
     * @return the dAbsoluteDeadline
     */
    public int getdAbsoluteDeadline() {
        return dAbsoluteDeadline;
    }

    /**
     * @param dAbsoluteDeadline the dAbsoluteDeadline to set
     */
    public void setdAbsoluteDeadline(int dAbsoluteDeadline) {
        this.dAbsoluteDeadline = dAbsoluteDeadline;
    }

    /**
     * @return the remainingExeTime
     */
    public int getRemainingExeTime() {
        return remainingExeTime;
    }

    /**
     * @param remainingExeTime the remainingExeTime to set
     */
    public void setRemainingExeTime(int remainingExeTime) {
        this.remainingExeTime = remainingExeTime;
    }
    
    /**
     * Inner static class, containing various comparator, by which a collection
     * of InstanceOfPeriodicTask objects can be sorted (e.g. readyQ in
     * Simulation.java)
     */
    public static class Comparators {

        /**
         * Used for Rate Monotonic algorithm. This comparator compares two 
         * InstanceOfPeriodicTask objects using their taskPeriod property.
         */
        public static Comparator<InstanceOfPeriodicTask> TASK_PERIOD
                = new Comparator<InstanceOfPeriodicTask>() {
                    @Override
                    public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                        int result = o1.getTaskPeriod() - o2.getTaskPeriod();

                        //if both objects have the same period, discriminate 
                        //by time of activation 
                        if (result == 0) {
                            result = o1.getrActivationTime() - o2.getrActivationTime();
                        }
                         //if both objects have the same period, discriminate 
                        //by the end of last execution
                        if (result == 0) {
                            result = (-1) * (o1.getLastEndOfExecutionTime() - o2.getLastEndOfExecutionTime());
                        }
                        return result;
                    }
                };
        
        /**
         * Used for Earliest Deadline First algorithm. This comparator compares
         * two InstanceOfPeriodicTask objects using their dAbsoluteDeadline property.
         */
        public static Comparator<InstanceOfPeriodicTask> ABSOLUTE_DEADLINE
                = new Comparator<InstanceOfPeriodicTask>() {
                    @Override
                    public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                        int result = o1.getdAbsoluteDeadline() - o2.getdAbsoluteDeadline();
                        
                        //if both objects have the same period, discriminate 
                        //by time of activation 
                        if (result == 0) {
                            result = o1.getrActivationTime() - o2.getrActivationTime();
                        }
                        //if both objects have the same abslolute deadline,  
                        //discriminate by the end of last execution 
                        if (result == 0) {
                            result = (-1) * (o1.getLastEndOfExecutionTime() - o2.getLastEndOfExecutionTime());
                        }
                        return result;
                    }
                };
        
        /**
         * Used for Deadline Monotonic algorithm.
         */
        public static Comparator<InstanceOfPeriodicTask> RELATIVE_DEADLINE
                = new Comparator<InstanceOfPeriodicTask>() {
                    @Override
                    public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                        int result = (o1.getdAbsoluteDeadline() - o1.getrActivationTime())
                        - (o2.getdAbsoluteDeadline() - o2.getrActivationTime());
                        
                        //if both objects have the same relative deadline,  
                        //discriminate by the end of last execution 
                        if (result == 0) {
                            result = (-1) * (o1.getLastEndOfExecutionTime() - o2.getLastEndOfExecutionTime());
                        }
                        return result;
                    }
                };
        
        /* We can add other comparators here */
    }

    @Override
    public String toString() {
        StringBuilder sbResult = new StringBuilder();
        
        //first line is task id
        sbResult.append(this.getId());
        sbResult.append(System.lineSeparator());
        
        //second line is activation time
        sbResult.append(this.getrActivationTime());
        sbResult.append(System.lineSeparator());
        
        //third line is absolute deadline
        sbResult.append(this.getdAbsoluteDeadline());
        sbResult.append(System.lineSeparator());
        
        //fourth line is total time needed to execute instance
        sbResult.append(this.cExecutionTime);
        sbResult.append(System.lineSeparator());
        
        //fifth line contains all the activation times.
        //if startOfExecutionTime is empty add -1 instead
        if (this.startOfExecutionTime.isEmpty()) {
            sbResult.append("-1");
        } else {
            for (Integer i : this.startOfExecutionTime) {
                sbResult.append(i);
                sbResult.append(" ");
            }
        }
        sbResult.append(System.lineSeparator());
    
        //fifth line contains all the activation times.
        //if endOfExecutionTime is empty add -1 instead
        if (this.endOfExecutionTime.isEmpty()) {
            sbResult.append("-1");
        } else {
            for (Integer i : this.endOfExecutionTime) {
                sbResult.append(i);
                sbResult.append(" ");
            }
        }
        sbResult.append(System.lineSeparator());
        
        //sixth line is -1 if the instance finished successfully.
        //if the instance missed its deadline it contains the deadline it missed
        sbResult.append(this.missedDeadline);
        sbResult.append(System.lineSeparator());
        
        return sbResult.toString();
    }
}
