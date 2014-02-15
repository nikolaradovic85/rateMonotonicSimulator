package javaapplication1;

/**
 *
 * @author nikola
 */
public class Main {
    
    public static void main(String[] args) {
        /* userInput(input);            //for user input
         File f = new File(args[0]);    //for command-line file name input */
        
        System.out.println("Rate Monotonic: ");
        Simulation rm  = new Simulation(
                "inputUp1.txt", 
                "trace/rm.log",  
                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
        
        System.out.println("Earliest Deadline First: ");
        Simulation edf = new Simulation(
                "inputUp1.txt", 
                "trace/edf.log", 
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        
        System.out.println("Deadline Monotonic: ");
        Simulation rm2 = new Simulation(
                "inputUp1.txt", 
                "trace/rm2.log", 
                InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE);
        
        boolean feasibilityTestRM  = rm.simulate();
        boolean feasibilityTestEDF = edf.simulate();
        boolean feasibilityTestRM2 = rm2.simulate();
    }
}