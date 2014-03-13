package TaskSchedulingSimulator;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.InputMismatchException;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 *
 * @author nikola
 */
public final class TraceFileParser {

    private final HashMap<Integer, TraceTask> map;

    public TraceFileParser(String traceFile) {
        this.map = new HashMap<>();
        parseTraceFile(traceFile);
    }

    public double getDeadlineMissProbability(int id) {
        return map.get(id).getDeadlineMissProbability();
    }
    
    public int getNumberOfTasks() {
        return map.size();
    }
    
    public int[] getTaskIDsPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getKey();
            counter++;
        }
        
        return result;
    }
    
    public int[] getNoOfFinishedInstancesPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getExecutedCounter();
            counter++;
        }
        
        return result;
    }
    
    public int[] getNoOfMissedDeadlinesPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getMissedCounter();
            counter++;
        }
        
        return result;
    }
    
    public double[] getDeadlineMissProbabilityPerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getDeadlineMissProbability();
            counter++;
        }
        
        return result;
    }
    
    public double[] getAverageResponseTimePerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getAverageResponseTime();
            counter++;
        }
        
        return result;
    }
    
    public int[] getMinimumResponseTimePerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getMinResponseTime();
            counter++;
        }
        
        return result;
    }
    
    public int[] getMaximumResponseTimePerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<Integer, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getMaxResponseTime();
            counter++;
        }
        
        return result;
    }

    /**
     * Parses trace file. Basically, it transforms a trace file into a
     * HashMap<Integer, TraceTask>.
     *
     * @param traceFile path to trace file
     */
    public void parseTraceFile(String traceFile) {
        try {
            Scanner scan = new Scanner(new File(traceFile));

            // while not end of file
            while (scan.hasNextLine()) {
                //first int in trace is periodic task id
                int id = scan.nextInt();

                //scanning next three integers from trace file
                int activationTime = scan.nextInt();
                int deadline = scan.nextInt();
                int totalExecutionTime = scan.nextInt();
                //moving scanner to next line !!!!!
                scan.nextLine();

                //these two lines in trace file represent every start
                //of execution, and every end of execution of this instance
                String startTimes = scan.nextLine();
                Scanner lineFirst = new Scanner(startTimes);
                String endTimes = scan.nextLine();
                Scanner lineSecond = new Scanner(endTimes);

                //scan the row which contains -1 if deadline isn't missed
                int missedDeadline = scan.nextInt();

                //if this is first occurance of any instance with this id
                //add a new task to the map
                if (!map.containsKey(id)) {
                    map.put(id, new TraceTask());
                }

                //get last end of execution (actual finish time)
                int finishTime = -1;
                while (lineSecond.hasNextInt()) {
                    finishTime = lineSecond.nextInt();
                }

                //response time is calculated
                int responseTime = finishTime - activationTime;

                //current task is the task with id that has been read from
                //the trace file
                TraceTask currentTask = map.get(id);

                //add response time to frequency table of response times
                //and check if it's min or max
                currentTask.addPossibleMinResponseTime(responseTime);
                currentTask.addPossibleMaxResponseTime(responseTime);
                currentTask.addResponseTimeToFreqTable(responseTime);

                //if deadline isn't missed, increment counter for executed
                //instances
                if (missedDeadline == -1) {
                    currentTask.incrementExecutedCounter();
                } 
                //if deadline IS missed, increment counter for instances
                //which missed their deadline
                else {
                    currentTask.incrementMissedCounter();
                }

                //moving scanner to the next instance
                scan.nextLine();
                scan.nextLine();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        } catch (InputMismatchException e) {
            System.out.println("File not found1.");
        } catch (NoSuchElementException e) {
            System.out.println("File not found2.");
        }
    }
}