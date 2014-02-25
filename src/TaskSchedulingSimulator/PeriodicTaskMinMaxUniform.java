package TaskSchedulingSimulator;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class PeriodicTaskMinMaxUniform extends PeriodicTask {

    private int cMin;
    private int cMax;
    private int randomDistribution; // not used
    
    public PeriodicTaskMinMaxUniform(
            int pId, 
            int taskPeriod, 
            int phi, 
            int pCMin, 
            int pCMax,
            int pRandomDistribution) {
        
        super(pId, taskPeriod, phi);
        this.cMin = pCMin;
        this.cMax = pCMax;
        this.randomDistribution = pRandomDistribution;
    }
    
    @Override
    public int getcTaskExecutionTime() {
        return cMin + (int) (Math.random() * (cMax - cMin + 1));
    }
}
