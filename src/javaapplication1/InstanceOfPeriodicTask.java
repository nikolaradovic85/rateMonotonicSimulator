package javaapplication1;

import java.util.Comparator;
import java.util.ArrayList;

/**
 *
 * @author nikola
 */
public class InstanceOfPeriodicTask {
    
    private int id;
    private int taskPeriod;         // period
    private int phi;                // phase
    private int rActivationTime;    // activation time
    private int dAbsoluteDeadline;  // absolute deadline
    private int cExecutionTime;     // execution time
    //collects time when any part of instance started beeing executed
    private ArrayList<Integer> startOfExecutionTime;
    //collects time when any part of instance stopped beeing executed
    private ArrayList<Integer> endOfExecutionTime;

    /**
     * constructor
     *
     * @param pId
     * @param taskPeriod
     * @param phi
     * @param rActivationTime
     * @param dAbsoluteDeadline
     * @param cExecutionTime
     */
    public InstanceOfPeriodicTask(int pId, int taskPeriod, int phi,
            int rActivationTime, int dAbsoluteDeadline, int cExecutionTime) {
        this.id = pId;
        this.cExecutionTime = cExecutionTime;
        this.dAbsoluteDeadline = dAbsoluteDeadline;
        this.phi = phi;
        this.rActivationTime = rActivationTime;
        this.taskPeriod = taskPeriod;
        this.startOfExecutionTime = new ArrayList<>();
        this.endOfExecutionTime = new ArrayList<>();
    }

    public boolean checkIfStillBeingExecuted(){
        return this.startOfExecutionTime.size() == this.endOfExecutionTime.size() + 1;
    }
    
    public void removeRedundantTimesFromLists() {
        if (startOfExecutionTime.size() > 1 && startOfExecutionTime.size() == endOfExecutionTime.size()) {
            //for each start time except first
            for (int i = 1; i < startOfExecutionTime.size(); i++) {
                //check if is equal to previous end time of execution
                //which means at that time was no interruption
                //so these times are redundant in list
                if (startOfExecutionTime.get(i) == endOfExecutionTime.get(i - 1)) {
                    //remove redundant times
                    startOfExecutionTime.remove(i);
                    endOfExecutionTime.remove(i-1);
                }
            }
        }
    }

    //adds time to the end of list
    public void addStartTimeOfExecution(int time) {
        this.startOfExecutionTime.add(time);
    }

    //adds time to the end of list
    public void addEndTimeOfExecution(int time) {
        this.endOfExecutionTime.add(time);
    }

    public int getId() {
        return id;
    }
    
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
     * @return the cExecutionTime
     */
    public int getcExecutionTime() {
        return cExecutionTime;
    }

    /**
     * @param cExecutionTime the cExecutionTime to set
     */
    public void setcExecutionTime(int cExecutionTime) {
        this.cExecutionTime = cExecutionTime;
    }
    
    public static class Comparators {

        /**
         * Used for Rate Monotonic algorithm. This comparator compares two 
         * InstanceOfPeriodicTask objects using their taskPeriod property.
         */
        public static Comparator<InstanceOfPeriodicTask> TASK_PERIOD
                = new Comparator<InstanceOfPeriodicTask>() {
                    @Override
                    public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                        return o1.getTaskPeriod() - o2.getTaskPeriod();
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
                        return o1.getdAbsoluteDeadline() - o2.getdAbsoluteDeadline();
                    }
                };
        
        /**
         * Used for Deadline Monotonic algorithm.
         */
        public static Comparator<InstanceOfPeriodicTask> RELATIVE_DEADLINE
                = new Comparator<InstanceOfPeriodicTask>() {
                    @Override
                    public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                        return (o1.getdAbsoluteDeadline() - o1.getrActivationTime())
                        - (o2.getdAbsoluteDeadline() - o2.getrActivationTime());
                    }
                };
        
        /* We can add other comparators here */
    }

    @Override
    public String toString() {
        StringBuilder sbResult = new StringBuilder();
        
        sbResult.append(this.getId());
        sbResult.append(System.lineSeparator());
        
        sbResult.append(this.getrActivationTime());
        sbResult.append(System.lineSeparator());
        
        sbResult.append(this.getdAbsoluteDeadline());
        sbResult.append(System.lineSeparator());
        
        for (Integer i : this.startOfExecutionTime) {
            sbResult.append(i);
            sbResult.append(" ");
        }
        sbResult.append(System.lineSeparator());
    
        for (Integer i : this.endOfExecutionTime) {
            sbResult.append(i);
            sbResult.append(" ");
        }
        sbResult.append(System.lineSeparator());
        
        return sbResult.toString();
    }
}
