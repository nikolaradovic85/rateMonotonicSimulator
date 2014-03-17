package TaskSchedulingSimulator;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class PeriodicTaskMinMaxUniform extends PeriodicTask {

    private final int cMin;
    private final int cMax;
    private final int randomDistribution; // not used
    
    public PeriodicTaskMinMaxUniform(
            String pId, 
            int taskPeriod, 
            int deadline,
            int phi, 
            int pCMin, 
            int pCMax,
            int pRandomDistribution) {
        
        super(pId, taskPeriod, deadline, phi);
        this.cMin = pCMin;
        this.cMax = pCMax;
        this.randomDistribution = pRandomDistribution;
    }
    
    @Override
    public int getcTaskExecutionTime() {
        return cMin + (int) (Math.random() * (cMax - cMin + 1));
    }
}
