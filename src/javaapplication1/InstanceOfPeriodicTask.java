package javaapplication1;

import java.util.Comparator;

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

    /**
    *constructor 
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
        
        //This comparator compares two InstanceOfPeriodicTask objects
        //using their taskPeriod property.
        public static Comparator<InstanceOfPeriodicTask> TASK_PERIOD = 
                new Comparator<InstanceOfPeriodicTask>() {
            @Override
            public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                return o1.getTaskPeriod() - o2.getTaskPeriod();
            }
        };
        
        public static Comparator<InstanceOfPeriodicTask> ABSOLUTE_DEADLINE = 
                new Comparator<InstanceOfPeriodicTask>(){
            @Override
            public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                return o1.getdAbsoluteDeadline() - o2.getdAbsoluteDeadline();
            }
        };
        
        public static Comparator<InstanceOfPeriodicTask> RELATIVE_DEADLINE = 
                new Comparator<InstanceOfPeriodicTask>(){
            @Override
            public int compare(InstanceOfPeriodicTask o1, InstanceOfPeriodicTask o2) {
                return (o1.getdAbsoluteDeadline()-o1.getrActivationTime()) - 
                        (o2.getdAbsoluteDeadline() - o2.getrActivationTime());
            }
        };
        /* We can add other comparators here */
    }
    
}
