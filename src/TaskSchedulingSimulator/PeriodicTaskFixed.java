package TaskSchedulingSimulator;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class PeriodicTaskFixed extends PeriodicTask {
    int cFixedExecutionTime;

    public PeriodicTaskFixed(
            String pId, 
            int taskPeriod, 
            int deadline,
            int phi, 
            int pCFixedExecutionTime) {
        
        super(pId, taskPeriod, deadline, phi);
        this.cFixedExecutionTime = pCFixedExecutionTime;
    }
    
    @Override
    public int getcTaskExecutionTime() {
        return cFixedExecutionTime;
    }
}
