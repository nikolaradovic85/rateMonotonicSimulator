package TaskSchedulingSimulator;

import java.util.Map;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class PeriodicTaskFrequencyTable extends PeriodicTask {

    Map<Integer, Integer> table;
    
    public PeriodicTaskFrequencyTable(
            String pId, 
            int taskPeriod, 
            int deadline,
            int phi, 
            Map<Integer, Integer> pTable) {
        
        super(pId, taskPeriod, deadline, phi);
        this.table = pTable;
    }

    @Override
    public int getcTaskExecutionTime() {
        
        int cTaskExecutionTime = 0;
        
        // find a random number [1,100] (uniform distribution)
        int random100 = (int) Math.ceil(Math.random() * 100);

        // find its match in the hashmap
        for (Map.Entry<Integer, Integer> entry : this.table.entrySet()) {
            if (random100 <= entry.getValue()) {
                cTaskExecutionTime = entry.getKey();
                return cTaskExecutionTime;
            }
        }
        
        return cTaskExecutionTime;
    }
}
