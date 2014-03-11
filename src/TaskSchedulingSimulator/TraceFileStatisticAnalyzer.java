package TaskSchedulingSimulator;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author nikola
 */
public final class TraceFileStatisticAnalyzer {

    private final HashMap<Integer, TraceTask> map;

    public TraceFileStatisticAnalyzer(String traceFile) {
        this.map = new HashMap<>();
        parseTraceFile(traceFile);
    }

    public double getDeadlineMissProbability(int id) {
        return map.get(id).getDeadlineMissProbability();
    }

    /**
     * reads trace file and calculates statistic parameters
     *
     * @param traceFile input trace file
     */
    public void parseTraceFile(String traceFile) {
        try {
            Scanner scan = new Scanner(new File(traceFile));

            // while not end of file
            while (scan.hasNextLine()) {
                try {
                    //first int in trace is periodic task id
                    int id = scan.nextInt();
                    //creating helper variables
                    Integer responseTime;

                    //scanning next three integers from trace file
                    int activationTime = scan.nextInt();
                    int deadline = scan.nextInt();
                    int totalExecutionTime = scan.nextInt();
                    //moving scanner on next line !!!!!
                    scan.nextLine();

                    String startTimes = scan.nextLine();
                    Scanner lineFirst = new Scanner(startTimes);

                    String endTimes = scan.nextLine();
                    Scanner lineSecond = new Scanner(endTimes);

                    int missedDeadline = scan.nextInt();

                    //if this is first occurance of any instance with this id
                    //add a new task to the map
                    if (!map.containsKey(id)) {
                        map.put(id, new TraceTask());
                    }

                    //get last int from second line (time when last part of 
                    //instace finished execution)
                    int finishTime = -1;
                    while (lineSecond.hasNextInt()) {
                        finishTime = lineSecond.nextInt();
                    }

                    responseTime = finishTime - activationTime;
                    
                    TraceTask currentTask = map.get(id);
                    
                    currentTask.addPossibleMinResponseTime(responseTime);
                    currentTask.addPossibleMaxResponseTime(responseTime);
                    currentTask.addResponseTimeToFreqTable(responseTime);

                    //if deadline isn't missed
                     if (missedDeadline == -1) {
                         currentTask.incrementExecutedCounter();
                     } 
                     //if deadline IS missed
                     else {
                         currentTask.incrementMissedCounter();
                     }
                    
                    //moving scanner to the next instance
                    scan.nextLine();
                    scan.nextLine();

                } catch (InputMismatchException e) {
                    System.out.println("File not found1.");

                } catch (NoSuchElementException e) {
                    System.out.println("File not found2.");
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        }
    }

    @Override
    public String toString() {
        StringBuilder sbResult = new StringBuilder();
        //for (int id = 1; id <= map.size(); id++) {
        for (Map.Entry<Integer, TraceTask> e : map.entrySet()) {
            //int listIndex = map.get(id);

            int noFinishedInstances = e.getValue().getExecutedCounter();
            int noMissedDeadlines = e.getValue().getMissedCounter();
            double deadlineMissProbabilityLocal = e.getValue().getDeadlineMissProbability();
            
            sbResult.append(e.getKey());
            sbResult.append(" - task statistic: ");
            sbResult.append(System.lineSeparator());
            sbResult.append(System.lineSeparator());
            sbResult.append("Number of finished instances: ");
            sbResult.append(noFinishedInstances);
            sbResult.append(System.lineSeparator());
            sbResult.append("Number of missed deadline: ");
            sbResult.append(noMissedDeadlines);
            sbResult.append(System.lineSeparator());
            sbResult.append("Deadline miss probability: ");
            sbResult.append(deadlineMissProbabilityLocal);
            sbResult.append(System.lineSeparator());
            
            if (noFinishedInstances != 0) {
                sbResult.append("Average response time (for all instances): ");
                sbResult.append(e.getValue().getAverageResponseTime());
                sbResult.append(System.lineSeparator());
                sbResult.append("Minimum response time (for finished instances): ");
                sbResult.append(e.getValue().getMinResponseTime());
                sbResult.append(System.lineSeparator());
                sbResult.append("Maximum response time (for finished instances): ");
                sbResult.append(e.getValue().getMaxResponseTime());
                sbResult.append(System.lineSeparator());
            }
            sbResult.append(System.lineSeparator());
            sbResult.append(System.lineSeparator());
        }

        return sbResult.toString();
    }
}