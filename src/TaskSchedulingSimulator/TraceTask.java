package TaskSchedulingSimulator;

/**
 * TraceTask class is used when doing statistical analysis of a trace file; it
 * is used to store all the data for one task (every instances of the task).
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class TraceTask {
    /**
     * Holds all response times and their number of occurrences.
     * Response time is key, number of occurrences is value.
     */
    public final FrequencyTable responseTimeFT;
    /**
     * Holds all jitter values and their number of occurrences.
     * Jitter is key, number of occurrences is value.
     */
    public final FrequencyTable jitterFT;
    /**
     * Number of instances which executed successfully (before their deadline).
     */
    private int executedCounter;
    /**
     * Number of instances which couldn't be finished before their deadline).
     */
    private int missedCounter;

    /**
     * Constructor. Sets initial values.
     */
    public TraceTask() {
        this.responseTimeFT = new FrequencyTable();
        this.jitterFT = new FrequencyTable();
        this.executedCounter = 0;
        this.missedCounter = 0;
    }
    
    /**
     * Calculates deadline miss probability for the task (all instances of the 
     * task).
     * 
     * @return deadline miss probability
     */
    public double getDeadlineMissProbability() {
        return  (this.missedCounter * 1.0) / 
                ((this.executedCounter + this.missedCounter) * 1.0);
    }
    
    /**
     * Increments number of executed instances by one (the ones that finished 
     * before their deadline).
     */
    public void incrementExecutedCounter() {
        this.executedCounter += 1;
    }
    
    /**
     * Increments number of instances which missed their deadline by one.
     */
    public void incrementMissedCounter() {
        this.missedCounter += 1;
    }

    /**
     * Returns number of executed instances of the task (the ones that finished
     * before their deadline).
     * 
     * @return number of executed instances
     */
    public int getExecutedCounter() {
        return executedCounter;
    }

    /**
     * Returns number of instances of the task which missed their deadline.
     * 
     * @return number of instances which missed their deadline
     */
    public int getMissedCounter() {
        return missedCounter;
    }
}
