package TaskSchedulingSimulator;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * TraceTask class is used when doing statistical analysis of a trace file; it
 * is used to store all the data for one task (all instances of a task).
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class TraceTask {
    /**
     * Holds all response times and their number of occurrences.
     * Response time is key, number of occurrences is value.
     */
    private final HashMap<Integer, Integer> responseTimeFreqTable;
    private int minResponseTime;
    private int maxResponseTime;
    /**
     * Holds all jitter values and their number of occurrences.
     * Jitter is key, number of occurrences is value.
     */
    private final HashMap<Integer, Integer> jitterFreqTable;
    private int minJitter;
    private int maxJitter;
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
        this.responseTimeFreqTable = new HashMap<>();
        this.jitterFreqTable = new HashMap<>();
        this.minResponseTime = Integer.MAX_VALUE;
        this.maxResponseTime = 0;
        this.minJitter = Integer.MAX_VALUE;
        this.maxJitter = 0;
        this.executedCounter = 0;
        this.missedCounter = 0;
    }
    
    /**
     * Updates minResponseTime if newResponseTime is lower than current 
     * minResponseTime.
     * 
     * @param newResponseTime possible minimum response time
     */
    private void addPossibleMinResponseTime(int newResponseTime) {
        if (newResponseTime < this.minResponseTime) {
            this.minResponseTime = newResponseTime;
        }
    }
    
    /**
     * Updates maxResponseTime if newResponseTime is higher than current 
     * maxResponseTime.
     * 
     * @param newResponseTime
     */
    private void addPossibleMaxResponseTime(int newResponseTime) {
        if (newResponseTime > this.maxResponseTime) {
            this.maxResponseTime = newResponseTime;
        }
    }
    
    /**
     * Adds responseTime to responseTimeFreqTable.
     * 
     * @param responseTime response time to be added 
     */
    public void addResponseTime(int responseTime) {
        //if this is the first occurence of this response time
        if (!this.responseTimeFreqTable.containsKey(responseTime)) {
            //add the response time, and set the no of occurrences to 1
            this.responseTimeFreqTable.put(responseTime, 1);
            //and check if it is minimum or maximum
            this.addPossibleMinResponseTime(responseTime);
            this.addPossibleMaxResponseTime(responseTime);
        } else {
            //simply increment number of occurrences by 1
            this.responseTimeFreqTable.put(
                    responseTime, 
                    this.responseTimeFreqTable.get(responseTime) + 1);
        }
    }
    
    /**
     * Calculates average response time for the task (all instances of the task).
     * 
     * @return average response time
     */
    public double getAverageResponseTime() {
        
        double sum = 0;
        
        for (Entry<Integer, Integer> e : this.responseTimeFreqTable.entrySet()) {
            sum += e.getKey() * e.getValue();
        }
        
        if (executedCounter + missedCounter == 0) {
            return 0;
        }
        
        return sum / ((executedCounter + missedCounter) * 1.0);
    }
    
    /**
     * Returns minimum response time for this task (all instances of the task).
     * 
     * @return minimum response time
     */
    public int getMinResponseTime() {
        return minResponseTime;
    }

    /**
     * Returns maximum response time for this task (all instances of the task).
     * 
     * @return maximum response time
     */
    public int getMaxResponseTime() {
        return maxResponseTime;
    }
    
    /**
     * Updates minJitter if newJitter is lower than current minJitter.
     * 
     * @param newResponseTime possible minimum response time
     */
    private void addPossibleMinJitter(int newJitter) {
        if (newJitter < this.minJitter) {
            this.minJitter = newJitter;
        }
    }
    
    /**
     * Updates maxJitter if newJitter is higher than current maxJitter.
     * 
     * @param newResponseTime
     */
    private void addPossibleMaxJitter(int newJitter) {
        if (newJitter > this.maxJitter) {
            this.maxJitter = newJitter;
        }
    }
    
    /**
     * Adds jitter to jitterFreqTable.
     * 
     * @param jitter response time to be added 
     */
    public void addJitter(int jitter) {
        //if this is the first occurence of this jitter value
        if (!this.jitterFreqTable.containsKey(jitter)) {
            //add the jitter, and set the no of occurrences to 1
            this.jitterFreqTable.put(jitter, 1);
            //and check if it is minimum or maximum
            this.addPossibleMinJitter(jitter);
            this.addPossibleMaxJitter(jitter);
        } else {
            //simply increment number of occurrences by 1
            this.jitterFreqTable.put(
                    jitter, 
                    this.jitterFreqTable.get(jitter) + 1);
        }
    }
    
    /**
     * Calculates average jitter for the task (all instances of the task).
     * 
     * @return average jitter
     */
    public double getAverageJitter() {
        
        double sum = 0;
        
        for (Entry<Integer, Integer> e : this.jitterFreqTable.entrySet()) {
            sum += e.getKey() * e.getValue();
        }
        
        if (executedCounter + missedCounter == 0) {
            return 0;
        }
        
        return sum / ((executedCounter + missedCounter) * 1.0);
    }

    /**
     * Returns maximum jitter for this task (all instances of the task).
     * 
     * @return maximum jitter
     */
    public int getMinJitter() {
        return minJitter;
    }

    /**
     * Returns maximum jitter for this task (all instances of the task).
     * 
     * @return maximum jitter
     */
    public int getMaxJitter() {
        return maxJitter;
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
