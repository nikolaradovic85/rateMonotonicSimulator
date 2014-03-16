package TaskSchedulingSimulator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikola
 */
public class Main {

    private static final String inputDir = "io/input/";
    private static final String traceDir = "io/trace/";
    private static final String statsDir = "io/statistic/";
    
    public static void main(String[] args) {
        
        //Manualy editing args will be removed when ready for distribution.
        args = new String[10];
        args[0] = "2";                  //number of simulations (inputs)
        
        args[1] = "input1.txt";         //path to input file
        args[2] = "RM";                 //algorithm
        args[3] = "SOFT";               //type of simulation
        
        args[4] = "specialinput.txt";   //...
        args[5] = "RM";
        args[6] = "SOFT";
        
        args[7] = "2";                  //number of repetitions
        
        int noOfSimulations = Integer.parseInt(args[0]);
        int noOfRepetitions = Integer.parseInt(args[noOfSimulations * 3 + 1]);
        HashMap<String, TraceFileParser[]> stats = new HashMap<>();
        
        
        String inputFiles[] = new String[noOfSimulations];
        Simulation simulationArray[] = new Simulation[noOfSimulations];
        Simulation.SimulationTypes simulationType = null;
        Comparator<InstanceOfPeriodicTask> comparator = null;

        //repeat given number of times
        for (int repetition = 1; repetition <= noOfRepetitions; repetition++) {
        
            for (int iCount = 0; iCount < noOfSimulations; iCount++) {
                String inputFile    = args[iCount * 3 + 1];
                String simAlghoritm = args[iCount * 3 + 2];
                String simType      = args[iCount * 3 + 3];
                
                if (!stats.containsKey(inputFile)) {
                    stats.put(inputFile, new TraceFileParser[noOfRepetitions]);
                }

                switch (simAlghoritm) {
                    case "RM" : comparator = InstanceOfPeriodicTask.Comparators.TASK_PERIOD; break;
                    case "EDF": comparator = InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE; break;
                    case "DM" : comparator = InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE; break;
                }

                switch (simType) {
                    case "SOFT"  : simulationType = Simulation.SimulationTypes.SOFT; break;
                    case "HARD"  : simulationType = Simulation.SimulationTypes.SOFT; break;
                    case "HYBRID": simulationType = Simulation.SimulationTypes.HYBRID; break;
                }

                inputFiles[iCount] = inputFile;

                simulationArray[iCount] = new Simulation(
                        Integer.toString(iCount), 
                        simulationType, 
                        inputDir + inputFile, 
                        traceDir + repetition + inputFile + ".trc", 
                        comparator);
            }

            //start all simulations in this repetition
            for (Simulation s : simulationArray) {
                s.start();
            }

            //join, so that everything bellow has to wait until they're done
            for (Simulation s :simulationArray) {
                try {
                    s.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //at this point all simulations in this repetition are finished,
            //and now stats hashmap can be populated

            //for each input file
            for (String input : inputFiles) {
                //create a TraceFileParser object
                TraceFileParser tfsa = new TraceFileParser(
                        traceDir + repetition + input + ".trc");
                
                //and add it to stats (in the apropriate place with regard to
                //repetition)
                TraceFileParser t[] = stats.get(input);
                t[repetition - 1] = tfsa;
                stats.put(input, t);
            }
        
        }
        
        //we can now calculate averages, for each task of each simulation.
        //for each simulation
        for (Entry<String, TraceFileParser[]> e : stats.entrySet()) {

            //get the number of tasks and print simulation input file name
            int noOfTasks = e.getValue()[0].getNumberOfTasks();
            
            System.out.println(e.getKey());
            System.out.println("");

            //after all the calculations, these variables will contain
            //average values over all repetitions (per task - that's why
            //they are arrays)
            double noOfFinishedInstancesPerTask[]   = new double[noOfTasks];
            double noOfMissedDeadlinesPerTask[]     = new double[noOfTasks];
            double deadlineMissProbabilityPerTask[] = new double[noOfTasks];
            double minimumResponseTimePerTask[]     = new double[noOfTasks];
            double maximumResponseTimePerTask[]     = new double[noOfTasks];
            double averageResponseTimePerTask[]     = new double[noOfTasks];
            double minimumJitterPerTask[]           = new double[noOfTasks];
            double maximumJitterPerTask[]           = new double[noOfTasks];
            double averageJitterPerTask[]           = new double[noOfTasks];

            //add up all the values (per task)
            for (int iCount = 0; iCount < noOfRepetitions; iCount++) {
                //single trace file parser (single simulation) 
                //from a single repetition(iCount)
                TraceFileParser t = e.getValue()[iCount];

                for (int jCount = 0; jCount < noOfTasks; jCount++) {
                    noOfFinishedInstancesPerTask[jCount]   += t.getNoOfFinishedInstancesPerTask()[jCount];
                    noOfMissedDeadlinesPerTask[jCount]     += t.getNoOfMissedDeadlinesPerTask()[jCount];
                    deadlineMissProbabilityPerTask[jCount] += t.getDeadlineMissProbabilityPerTask()[jCount];
                    minimumResponseTimePerTask[jCount]     += t.getMinimumResponseTimePerTask()[jCount];
                    maximumResponseTimePerTask[jCount]     += t.getMaximumResponseTimePerTask()[jCount];
                    averageResponseTimePerTask[jCount]     += t.getAverageResponseTimePerTask()[jCount];
                    minimumJitterPerTask[jCount]           += t.getMinimumJitterPerTask()[jCount];
                    maximumJitterPerTask[jCount]           += t.getMaximumJitterPerTask()[jCount];
                    averageJitterPerTask[jCount]           += t.getAverageJitterPerTask()[jCount];
                }
            }
            
            //divide every value (per taks) by the number of repetitions
            //in order to get the average over repetitions
            for (int iCount = 0; iCount < noOfTasks; iCount++) {
                noOfFinishedInstancesPerTask[iCount]   /= noOfRepetitions;
                noOfMissedDeadlinesPerTask[iCount]     /= noOfRepetitions;
                deadlineMissProbabilityPerTask[iCount] /= noOfRepetitions;
                minimumResponseTimePerTask[iCount]     /= noOfRepetitions;
                maximumResponseTimePerTask[iCount]     /= noOfRepetitions;
                averageResponseTimePerTask[iCount]     /= noOfRepetitions;
                minimumJitterPerTask[iCount]           /= noOfRepetitions;
                maximumJitterPerTask[iCount]           /= noOfRepetitions;
                averageJitterPerTask[iCount]           /= noOfRepetitions;
                
                //print values - TODO - print to file
                System.out.println("Task " + iCount);
                
                System.out.print("Finished instances: ");
                System.out.print(noOfFinishedInstancesPerTask[iCount]);
                System.out.println("");
                
                System.out.print("Missed deadlines: ");
                System.out.print(noOfMissedDeadlinesPerTask[iCount]);
                System.out.println("");
                
                System.out.print("Deadline miss probability: ");
                System.out.print(deadlineMissProbabilityPerTask[iCount]);
                System.out.println("");
                
                System.out.print("Minimum response time: ");
                System.out.print(minimumResponseTimePerTask[iCount]);
                System.out.println("");
                
                System.out.print("Maximum response time: ");
                System.out.print(maximumResponseTimePerTask[iCount]);
                System.out.println("");
                
                System.out.print("Average response time: ");
                System.out.print(averageResponseTimePerTask[iCount]);
                System.out.println("");
                
                System.out.print("Minimum jitter: ");
                System.out.print(minimumJitterPerTask[iCount]);
                System.out.println("");
                
                System.out.print("Maximum jitter: ");
                System.out.print(maximumJitterPerTask[iCount]);
                System.out.println("");
                
                System.out.print("Average jitter: ");
                System.out.print(averageJitterPerTask[iCount]);
                System.out.println("");
                
                System.out.println("");
            }
            
            System.out.println("-----------------------------------------");
        }
    }
}
