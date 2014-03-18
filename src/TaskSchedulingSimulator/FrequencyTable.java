package TaskSchedulingSimulator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Used to store info about data such as response time of a task, or jitter.
 * Contains time as key (response time, jitter) and a Pair as its value - Pair
 * contains number of occurrences of this particular time (response time...) and
 * how many times did the task miss it's deadline when it had this time.
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class FrequencyTable {
    /**
     * Contains time values and their number of occurrences.
     * Time is key, number of occurrences is value.
     */
    private final TreeMap<Integer, Pair> FreqencyTableMap;
    
    /**
     * Contains maximum time.
     */
    private int minimum;
    
    /**
     * Contains minimum time.
     */
    private int maximum;

    /**
     * Constructor.
     */
    public FrequencyTable() {
        this.FreqencyTableMap = new TreeMap<>();
        this.minimum = Integer.MAX_VALUE;
        this.maximum = 0;
    }
    
    /**
     * Calculates average time in FreqencyTableMap
     * 
     * @return average time
     */
    public double getAverageTime() {
        
        double sum = 0;
        int noOfInstances = 0;
        
        for (Map.Entry<Integer, Pair> e : this.FreqencyTableMap.entrySet()) {
            sum += e.getKey() * e.getValue().noOfOccurences;
            noOfInstances += e.getValue().noOfOccurences;
        }
        
        //if table is empty
        if (noOfInstances == 0) { return 0; }
        
        //return average
        return sum / ((noOfInstances) * 1.0);
    }
    
    /**
     * Calculates standard deviation from frequency table.
     * 
     * @return standard deviation
     */
    public double getStandardDeviation() {
        
        double result = 0.0;
        double mean = this.getAverageTime();
        int noOfInstances = 0;
        
        for (Map.Entry<Integer, Pair> e : FreqencyTableMap.entrySet()) {
            result += Math.pow((e.getKey() - mean), 2) * e.getValue().noOfOccurences;
            noOfInstances += e.getValue().noOfOccurences;
        }
        
        //if table is empty
        if (noOfInstances == 0) { return 0; }
        
        return Math.sqrt((result / noOfInstances));
    }
    
    /**
     * Calculates variance from frequency table.
     * 
     * @return variance
     */
    public double getVariance() {
        return Math.pow(this.getStandardDeviation(), 2);
    }
    
    /**
     * Adds time to FreqencyTableMap.
     * 
     * @param time time to be added 
     * @param deadlineWasMissed was deadline missed?
     */
    public void addTime(int time, boolean deadlineWasMissed) {
        //if this is the first occurence of this time
        if (!this.FreqencyTableMap.containsKey(time)) {
            //add the time; if deadline is missed, incrementDeadlineMisses
            Pair newPair = new Pair();
            if (deadlineWasMissed) { newPair.incrementDeadlineMisses(); }
            this.FreqencyTableMap.put(time, newPair);
            //and check if it is minimum or maximum
            this.addPossibleMinimum(time);
            this.addPossibleMaximum(time);
        } else {
            /*update the pair by incrementing number of occurrences, and 
            number of missed deadlines (if deadline was missed) */
            Pair updatedPair = this.FreqencyTableMap.get(time);
            updatedPair.incrementOccurences();
            if (deadlineWasMissed) { updatedPair.incrementDeadlineMisses(); }
            
            //put updated pair back into frequency table
            this.FreqencyTableMap.put(time, updatedPair);
        }
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

    /**
     * Returns String representation of FrequencyTable in the following format:
     * TIME NO_OF_OCCURRENCES_FOR_TIME NO_OF_MISSED_DEADLINES_FOR_TIME
     * 
     * @return String representation of FrequencyTable
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (Entry e : this.FreqencyTableMap.entrySet()) {
            sb.append(e.getKey());
            sb.append(" ");
            sb.append(((Pair) e.getValue()).noOfOccurences);
            sb.append(" ");
            sb.append(((Pair) e.getValue()).noOfDeadlineMisses);
            sb.append(System.lineSeparator());
        }
        
        return sb.toString();
    }
    
    /**
     * Used as wrapper around two Integers - number of occurrences of a single
     * time, and number of missed deadlines for this time.
     */
    private class Pair {
        public Integer noOfOccurences;
        public Integer noOfDeadlineMisses;
        
        public Pair () {
            this.noOfOccurences = 1;
            this.noOfDeadlineMisses = 0;
        }
        
        public void incrementOccurences() {
            this.noOfOccurences += 1;
        }
        
        public void incrementDeadlineMisses() {
            this.noOfDeadlineMisses += 1;
        }
    }
}
