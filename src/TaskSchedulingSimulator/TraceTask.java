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
     * responseTimeFreqTable hold all response times and their number of 
     * occurrence. Response time is key, and number of occurrences is value.
     */
    private final HashMap<Integer, Integer> responseTimeFreqTable;
    private int minResponseTime;
    private int maxResponseTime;
    private int executedCounter;
    private int missedCounter;

    /**
     * Constructor. Sets initial values.
     */
    public TraceTask() {
        this.responseTimeFreqTable = new HashMap<>();
        this.minResponseTime = Integer.MAX_VALUE;
        this.maxResponseTime = 0;
        this.executedCounter = 0;
        this.missedCounter = 0;
    }
    
    /**
     * Updates minResponseTime if newResponseTime is lower than current 
     * minResponseTime.
     * 
     * @param newResponseTime possible minimum response time
     */
    public void addPossibleMinResponseTime(int newResponseTime) {
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
    public void addPossibleMaxResponseTime(int newResponseTime) {
        if (newResponseTime > this.maxResponseTime) {
            this.maxResponseTime = newResponseTime;
        }
    }
    
    /**
     * Adds responseTime to responseTimeFreqTable.
     * 
     * @param responseTime response time to be added 
     */
    public void addResponseTimeToFreqTable(int responseTime) {
        //if this is the first occurence of this response time
        if (!this.responseTimeFreqTable.containsKey(responseTime)) {
            //add the response time, and set the no of occurrences to 1
            this.responseTimeFreqTable.put(responseTime, 1);
        } else {
            //simply increment number of occurrences by 1
            this.responseTimeFreqTable.put(
                    responseTime, 
                    this.responseTimeFreqTable.get(responseTime) + 1);
        }
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
     * Calculates average response time for the task (all instances of the task).
     * 
     * @return average response time
     */
    public double getAverageResponseTime() {
        
        double sum = 0;
        
        for (Entry<Integer, Integer> e : this.responseTimeFreqTable.entrySet()) {
            sum += e.getKey() * e.getValue();
        }
        
        if (executedCounter == 0) {
            return 0;
        }
        
        return sum / ((executedCounter + missedCounter) * 1.0);
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
