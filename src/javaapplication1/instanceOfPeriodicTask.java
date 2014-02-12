package javaapplication1;

import java.util.Comparator;

/**
 *
 * @author nikola
 */
public class instanceOfPeriodicTask {

    private int taskPeriod;         // period
    private int phi;                // phase
    private int rActivationTime;    // activation time
    private int dAbsoluteDeadline;  // absolute deadline
    private int cExecutionTime;     // execution time

    /**
    *constructor 
     * @param taskPeriod
     * @param phi
     * @param rActivationTime
     * @param dAbsoluteDeadline
     * @param cExecutionTime
    */
    public instanceOfPeriodicTask(int taskPeriod, int phi,
            int rActivationTime, int dAbsoluteDeadline, int cExecutionTime) {
        this.cExecutionTime = cExecutionTime;
        this.dAbsoluteDeadline = dAbsoluteDeadline;
        this.phi = phi;
        this.rActivationTime = rActivationTime;
        this.taskPeriod = taskPeriod;
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
        
        //This comparator compares two instanceOfPeriodicTask objects
        //using their taskPeriod property.
        public static Comparator<instanceOfPeriodicTask> TASK_PERIOD = 
                new Comparator<instanceOfPeriodicTask>() {
            @Override
            public int compare(instanceOfPeriodicTask o1, instanceOfPeriodicTask o2) {
                return o1.getTaskPeriod() - o2.getTaskPeriod();
            }
        };
        
        public static Comparator<instanceOfPeriodicTask> ABSOLUTE_DEADLINE = 
                new Comparator<instanceOfPeriodicTask>(){
            @Override
            public int compare(instanceOfPeriodicTask o1, instanceOfPeriodicTask o2) {
                return o1.getdAbsoluteDeadline() - o2.getdAbsoluteDeadline();
            }
        };
        /* We can add other comparators here */
    }
    
}
