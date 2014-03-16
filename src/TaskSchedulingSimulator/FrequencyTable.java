package TaskSchedulingSimulator;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class FrequencyTable {
    /**
     * Holds time values and their number of occurrences.
     * Time is key, number of occurrences is value.
     */
    private final TreeMap<Integer, Integer> FreqencyTableMap;
    private int minimum;
    private int maximum;

    public FrequencyTable() {
        this.FreqencyTableMap = new TreeMap<>();
        this.minimum = Integer.MAX_VALUE;
        this.maximum = 0;
    }
    
    /**
     * Adds time to FreqencyTableMap.
     * 
     * @param time time to be added 
     */
    public void addTime(int time) {
        //if this is the first occurence of this time
        if (!this.FreqencyTableMap.containsKey(time)) {
            //add the time, and set the no of occurrences to 1
            this.FreqencyTableMap.put(time, 1);
            //and check if it is minimum or maximum
            this.addPossibleMinimum(time);
            this.addPossibleMaximum(time);
        } else {
            //simply increment number of occurrences by 1
            this.FreqencyTableMap.put(
                    time, 
                    this.FreqencyTableMap.get(time) + 1);
        }
    }
    
    /**
     * Calculates average time in FreqencyTableMap
     * 
     * @return average time
     */
    public double getAverageTime() {
        
        double sum = 0;
        int noOfInstances = 0;
        
        for (Map.Entry<Integer, Integer> e : this.FreqencyTableMap.entrySet()) {
            sum += e.getKey() * e.getValue();
            noOfInstances += e.getValue();
        }
        
        //if table is empty
        if (noOfInstances == 0) { return 0; }
        
        //return average
        return sum / ((noOfInstances) * 1.0);
    }
    
    /**
     * Updates minimum if newTime is lower than current minimum.
     * 
     * @param newTime possible minimum time
     */
    private void addPossibleMinimum(int newTime) {
        if (newTime < this.minimum) {
            this.minimum = newTime;
        }
    }
    
    /**
     * Updates maximum if newTime is higher than current maximum.
     * 
     * @param newTime possible maximum response time
     */
    private void addPossibleMaximum(int newTime) {
        if (newTime > this.maximum) {
            this.maximum = newTime;
        }
    }
    
    /**
     * Getter for minimum.
     * 
     * @return minimum
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Getter for maximum.
     * 
     * @return maximum
     */
    public int getMaximum() {
        return maximum;
    }
}
