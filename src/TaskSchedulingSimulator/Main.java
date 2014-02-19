package TaskSchedulingSimulator;

/**
 *
 * @author nikola
 */
public class Main {
    
    public static void main(String[] args) {
        
        Simulation rmHard  = new Simulation(
                "Rate Monotonic Hard",
                Simulation.SimulationTypes.HARD,
                "io/input/input.txt", 
                "io/trace/rmHard.log",  
                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
        rmHard.start();
        
        Simulation rmSoft  = new Simulation(
                "Rate Monotonic Soft",
                Simulation.SimulationTypes.SOFT,
                "io/input/input.txt", 
                "io/trace/rmSoft.log",  
                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
        rmSoft.start();
        
        Simulation edfHard = new Simulation(
                "Earliest Deadline First Hard",
                Simulation.SimulationTypes.HARD,
                "io/input/input.txt", 
                "io/trace/edfHard.log", 
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        edfHard.start();
        
        Simulation edfSoft = new Simulation(
                "Earliest Deadline First Soft",
                Simulation.SimulationTypes.SOFT,
                "io/input/input.txt", 
                "io/trace/edfSoft.log", 
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        edfSoft.start();
        
        Simulation dmHard = new Simulation(
                "Deadline Monotonic Hard",
                Simulation.SimulationTypes.HARD,
                "io/input/input.txt", 
                "io/trace/dmHard.log", 
                InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE);
        dmHard.start();
        
        Simulation dmSoft = new Simulation(
                "Deadline Monotonic Soft",
                Simulation.SimulationTypes.SOFT,
                "io/input/input.txt", 
                "io/trace/dmSoft.log", 
                InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE);
        dmSoft.start();
    }
}