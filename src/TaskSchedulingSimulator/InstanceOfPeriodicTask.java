package TaskSchedulingSimulator;

import java.util.ArrayList;
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
    private final int totalExecutionTime;
    private int missedDeadline;
    //collects time when any part of instance started beeing executed
    private ArrayList<Integer> startOfExecutionTime;
    //collects time when any part of instance stopped beeing executed
    private ArrayList<Integer> endOfExecutionTime;

    /**
     * Constructor.
     *
     * @param pId
     * @param taskPeriod
     * @param phi
     * @param rActivationTime
     * @param dAbsoluteDeadline
     * @param pcExecutionTime
     */
    public InstanceOfPeriodicTask(PeriodicTask task, int pRActivationTime) {
        this.rActivationTime = pRActivationTime;
        this.id = task.getId();
        this.cExecutionTime = this.totalExecutionTime = task.getcTaskExecutionTime();      
        this.dAbsoluteDeadline = task.getDeadline() + rActivationTime;
        this.phi = task.getPhi();
        this.taskPeriod = task.getTaskPeriod();
        this.missedDeadline = -1;
        this.startOfExecutionTime = new ArrayList<>();
        this.endOfExecutionTime = new ArrayList<>();
    }

    public boolean missedDeadline(){
        if(missedDeadline == -1)
            return false;
        else 
            return true;
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
                if (startOfExecutionTime.get(i).equals(endOfExecutionTime.get(i - 1))) {
                    //remove redundant times
                    startOfExecutionTime.remove(i);
                    endOfExecutionTime.remove(i-1);
                    i--;//best fix ever
                }
            }
        }
    }

    private int getLastEndOfExecutionTime() {
        if (this.endOfExecutionTime.isEmpty()) {
            return 0;
        } else {
            return this.endOfExecutionTime.get(this.endOfExecutionTime.size() - 1);
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

    public void setMissedDeadline(int missedDeadline) {
        this.missedDeadline = missedDeadline;
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
        sbResult.append(this.totalExecutionTime);
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
