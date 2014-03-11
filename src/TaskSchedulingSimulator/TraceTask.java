package TaskSchedulingSimulator;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class TraceTask {
    private final HashMap<Integer, Integer> responseTimeFreqTable;
    private int minResponseTime;
    private int maxResponseTime;
    private int executedCounter;
    private int missedCounter;

    public TraceTask() {
        this.responseTimeFreqTable = new HashMap<>();
        this.minResponseTime = Integer.MAX_VALUE;
        this.maxResponseTime = 0;
        this.executedCounter = 0;
        this.missedCounter = 0;
    }
    
    public void addPossibleMinResponseTime(int newResponseTime) {
        if (newResponseTime < this.minResponseTime) {
            this.minResponseTime = newResponseTime;
        }
    }
    
    public void addPossibleMaxResponseTime(int newResponseTime) {
        if (newResponseTime > this.maxResponseTime) {
            this.maxResponseTime = newResponseTime;
        }
    }
    
    public void addResponseTimeToFreqTable(int responseTime) {
        if (!this.responseTimeFreqTable.containsKey(responseTime)) {
            this.responseTimeFreqTable.put(responseTime, 1);
        } else {
            this.responseTimeFreqTable.put(
                    responseTime, 
                    this.responseTimeFreqTable.get(responseTime) + 1);
        }
    }
    
    public double getDeadlineMissProbability() {
        return  (this.missedCounter * 1.0) / 
                ((this.executedCounter + this.missedCounter) * 1.0);
    }
    
    public double getAverageResponseTime() {
        
        double sum = 0;
        int totalNoOfResponseTimes = 0;
        
        for (Entry<Integer, Integer> e : this.responseTimeFreqTable.entrySet()) {
            sum += e.getKey() * e.getValue();
            totalNoOfResponseTimes += e.getValue();
        }
        
        if (totalNoOfResponseTimes == 0) {
            return 0;
        }
        
        return sum / (totalNoOfResponseTimes * 1.0);
    }
    
    public void incrementExecutedCounter() {
        this.executedCounter += 1;
    }
    
    public void incrementMissedCounter() {
        this.missedCounter += 1;
    }

    public int getMinResponseTime() {
        return minResponseTime;
    }

    public int getMaxResponseTime() {
        return maxResponseTime;
    }

    public int getExecutedCounter() {
        return executedCounter;
    }

    public int getMissedCounter() {
        return missedCounter;
    }
}
