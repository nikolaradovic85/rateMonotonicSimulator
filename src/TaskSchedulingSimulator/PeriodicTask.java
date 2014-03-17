package TaskSchedulingSimulator;

/**
 * PeriodicTask class is used to make a "task set" (called input in Simulation).
 * 
 * @author nikola
 */

public abstract class PeriodicTask implements Comparable <PeriodicTask>{
    private final String id;
    private final int taskPeriod;
    private final int phi;
    private final int deadline;

    /**
     * Constructor.
     * 
     * @param pId task id
     * @param taskPeriod task period
     * @param deadline task deadline
     * @param phi task phase
     */
    public PeriodicTask(String pId, int taskPeriod, int deadline, int phi){
        this.id = pId;
        this.phi = phi;
        this.taskPeriod = taskPeriod;
        this.deadline = deadline;
    }

    /**
     * Getter for id.
     * 
     * @return the id of the task
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the taskPeriod
     */
    public int getTaskPeriod() {
        return taskPeriod;
    }

    /**
     * @return the phi
     */
    public int getPhi() {
        return phi;
    }

    public int getDeadline(){
        return this.deadline;
    }

    /**
     * Each implementation of PeriodicTask should have its own implementation
     * of this method (e.g. PeriodicTaskMinMaxUniform needs to calculate a 
     * random value between min and max and return this as the execution time.
     * 
     * @return the cTaskExecutionTime
     */
    public abstract int getcTaskExecutionTime();

    @Override
    public int compareTo(PeriodicTask other){
        return this.taskPeriod - other.getTaskPeriod();
    }
}