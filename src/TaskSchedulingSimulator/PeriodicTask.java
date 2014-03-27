package TaskSchedulingSimulator;

import java.util.Map;

/**
 * PeriodicTask class is used to make a "task set" (called input in Simulation).
 *
 * @author nikola
 */
public class PeriodicTask {

    private final String id;
    private final int phi;
    private final int deadline;
    private final Type periodType;
    Map<Integer, Integer> periodMap;
    private final Type executionType;
    Map<Integer, Integer> executionMap;

    /**
     * Constructor.
     * @param id
     * @param phi
     * @param taskPeriodType
     * @param periodMap
     * @param deadline
     * @param executionTimeType
     * @param executionMap 
     */
    PeriodicTask(String id, int phi, String taskPeriodType, Map<Integer, Integer> periodMap,
            int deadline, String executionTimeType, Map<Integer, Integer> executionMap) {
        this.id = id;
        this.phi = phi;
        this.deadline = deadline;
        this.periodType = getParameterType(taskPeriodType);
        this.periodMap = periodMap;
        this.executionType = getParameterType(executionTimeType);
        this.executionMap = executionMap;
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
     * @return the taskPeriod, calculated in method getValueForType.
     */
    public int getTaskPeriod() {
        return getValueForType(periodType, periodMap);
    }

    /**
     * @return the phi
     */
    public int getPhi() {
        return phi;
    }

    public int getDeadline() {
        return this.deadline;
    }

    /**
     * @return the cTaskExecutionTime, calculated in method getValueForType. 
     */
    public int getcTaskExecutionTime() {
        return getValueForType(executionType, executionMap);
    }

    /**
     * Converts string from input file in appropriate enum type
     *
     * @param stringType
     * @return Type
     */
    public Type getParameterType(String stringType) {
        switch (stringType) {
            case "FIXED":
                return Type.FIXED;
            case "MIN_MAX_UNIFORM":
                return Type.MIN_MAX_UNIFORM;
            case "FREQUENCY_TABLE":
                return Type.FREQUENCY_TABLE;
            default:
                return Type.FIXED;
        }
    }

    /**
     * 
     * @param type - type of "operation"
     * @param map - contains all values needed to produce output 
     * (if type Fixed map contains only one value; if min max uniform map contains three values,
     * min, max, random distribution; if frequency table type map contains whole frequency table)
     * @return appropriate value for actual type: if type fixed returns always same FIXED Value,
     * if MIN_MAX_UNIFORM returns integer value in range [min,max], if FREQUENCY_TABLE returns 
     * value that is in accordance with frequency table stored in map.
     * This method is used for calculating time period and execution time
     */
    public int getValueForType(Type type, Map<Integer, Integer> map) {
        switch (type) {
            case FIXED:
                return (int) map.get(1);
            case MIN_MAX_UNIFORM:
                int cMin = (int) map.get(1);
                int cMax = (int) map.get(2);
                int randomDistribution = (int) map.get(3);
                return cMin + (int) (Math.random() * (cMax - cMin + 1));
            case FREQUENCY_TABLE:
                int cTaskExecutionTime = 0;

                // find a random number [1,100] (uniform distribution)
                int random100 = (int) Math.ceil(Math.random() * 100);

                // find its match in the hashmap
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    if (random100 <= entry.getValue()) {
                        cTaskExecutionTime = entry.getKey();
                        return cTaskExecutionTime;
                    }
                }
        }
        return -1;
    }
}
