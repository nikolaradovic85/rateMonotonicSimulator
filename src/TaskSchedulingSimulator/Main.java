package TaskSchedulingSimulator;

/**
 *
 * @author nikola
 */
public class Main {
    
    public static void main(String[] args) {
        /* userInput(input);            //for user input
         File f = new File(args[0]);    //for command-line file name input */
        
        Simulation rm  = new Simulation(
                "Rate Monotonic",
                "io/input/inputUp1.txt", 
                "io/trace/rm.log",  
                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
        rm.start();
        
        Simulation edf = new Simulation(
                "Earliest Deadline First",
                "io/input/inputUp1.txt", 
                "io/trace/edf.log", 
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        edf.start();
        
        Simulation rm2 = new Simulation(
                "Deadline Monotonic",
                "io/input/inputUp1.txt", 
                "io/trace/rm2.log", 
                InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE);
        rm2.start();
    }
}