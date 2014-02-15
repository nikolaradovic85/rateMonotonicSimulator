package javaapplication1;

/**
 * PeriodicTask class is used to make a "task set" (called input in Simulation).
 * 
 * @author nikola
 */

public class PeriodicTask implements Comparable <PeriodicTask>{
    private int id;
    private int taskPeriod;         // period
    private int phi;                // phase
    private int cTaskExecutionTime; // execution time

    /**
     * Constructor.
     * 
     * @param pId
     * @param taskPeriod
     * @param phi
     * @param cTaskExecutionTime
     */
    public PeriodicTask(int pId, int taskPeriod, int phi, int cTaskExecutionTime){
        this.id = pId;
        this.cTaskExecutionTime = cTaskExecutionTime;
        this.phi = phi;
        this.taskPeriod = taskPeriod;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to be set
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
     * @return the cTaskExecutionTime
     */
    public int getcTaskExecutionTime() {
        return cTaskExecutionTime;
    }

    /**
     * @param cTaskExecutionTime the cTaskExecutionTime to set
     */
    public void setcTaskExecutionTime(int cTaskExecutionTime) {
        this.cTaskExecutionTime = cTaskExecutionTime;
    }
    
    @Override
    public int compareTo(PeriodicTask other){
        return this.taskPeriod - other.getTaskPeriod();
    }
}