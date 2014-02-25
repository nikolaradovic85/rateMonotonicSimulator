package TaskSchedulingSimulator;

import java.util.Random;

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
        Random r = new Random(System.currentTimeMillis());
        return cMin + (int) (r.nextDouble() * (cMax - cMin + 1));
    }
}
