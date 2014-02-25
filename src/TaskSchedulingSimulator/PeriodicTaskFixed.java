package TaskSchedulingSimulator;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class PeriodicTaskFixed extends PeriodicTask {
    int cFixedExecutionTime;

    public PeriodicTaskFixed(
            int pId, 
            int taskPeriod, 
            int phi, 
            int pCFixedExecutionTime) {
        
        super(pId, taskPeriod, phi);
        this.cFixedExecutionTime = pCFixedExecutionTime;
    }
    
    @Override
    public int getcTaskExecutionTime() {
        return cFixedExecutionTime;
    }
}
