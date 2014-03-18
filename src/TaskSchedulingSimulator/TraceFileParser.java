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
 * Used to parse a trace file and store and retrieve all the data it contains.
 * 
 * @author nikola
 */
public final class TraceFileParser {

    /**
     * Gets populated with data from trace file. Key is task id, value is
     * TraceTask object (every other piece of information about every instance
     * of the task).
     */
    private final HashMap<String, TraceTask> map;

    /**
     * Constructor. Parses trace file, so the class can be immutable.
     * 
     * @param traceFile Path to trace file to be parsed
     */
    public TraceFileParser(String traceFile) {
        this.map = new HashMap<>();
        parseTraceFile(traceFile);
    }
    
    /**
     * Returns the number of different tasks in the trace file
     * 
     * @return number of tasks
     */
    public int getNumberOfTasks() {
        return map.size();
    }
    
    /* 
    GETTERS FOR EACH OF THE PROPERTIES OF TraceTask.java PER TASK;
    IF YOU ADD A NEW PROPERTY FOR TraceTask.java YOU ALSO NEED TO ADD ANOTHER
    METHOD HERE, IN ORDER TO USE IT IN Main.java
    */
    // <editor-fold defaultstate="collapsed" desc="Property getters per task - boilerplate code">
    public String[] getTaskIDsPerTask() {
        String[] result = new String[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getKey();
            counter++;
        }
        
        return result;
    }
    
    public int[] getNoOfFinishedInstancesPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getExecutedCounter();
            counter++;
        }
        
        return result;
    }
    
    public int[] getNoOfMissedDeadlinesPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getMissedCounter();
            counter++;
        }
        
        return result;
    }
    
    public double[] getDeadlineMissProbabilityPerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().getDeadlineMissProbability();
            counter++;
        }
        
        return result;
    }
    
    public double[] getAverageResponseTimePerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().responseTimeFT.getAverageTime();
            counter++;
        }
        
        return result;
    }
    
    public double[] getResponseTimeStandardDevPerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().responseTimeFT.getStandardDeviation();
            counter++;
        }
        
        return result;
    }
    
    public double[] getResponseTimeVariancePerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().responseTimeFT.getVariance();
            counter++;
        }
        
        return result;
    }
    
    public int[] getMinimumResponseTimePerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().responseTimeFT.getMinimum();
            counter++;
        }
        
        return result;
    }
    
    public int[] getMaximumResponseTimePerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().responseTimeFT.getMaximum();
            counter++;
        }
        
        return result;
    }
    
    public double[] getAverageJitterPerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().jitterFT.getAverageTime();
            counter++;
        }
        
        return result;
    }
    
    public double[] getJitterStandardDevPerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().jitterFT.getStandardDeviation();
            counter++;
        }
        
        return result;
    }
    
    public double[] getJitterVariancePerTask() {
        double[] result = new double[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().jitterFT.getVariance();
            counter++;
        }
        
        return result;
    }
    
    public int[] getMinimumJitterPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().jitterFT.getMinimum();
            counter++;
        }
        
        return result;
    }
    
    public int[] getMaximumJitterPerTask() {
        int[] result = new int[map.size()];
        int counter = 0;
        
        for (Entry<String, TraceTask> e : map.entrySet()) {
            result[counter] = e.getValue().jitterFT.getMaximum();
            counter++;
        }
        
        return result;
    } // </editor-fold> 

    /**
     * Parses trace file. Basically, it transforms a trace file into a
     * HashMap<String, TraceTask>.
     *
     * @param traceFile path to trace file
     */
    public void parseTraceFile(String traceFile) {
        try {
            Scanner scan = new Scanner(new File(traceFile));

            // while not end of file
            while (scan.hasNextLine()) {
                //first int in trace is periodic task id
                String id = scan.next();

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
                int lineMissedDeadline = scan.nextInt();
                boolean missedDeadline = lineMissedDeadline != -1;

                //if this is first occurance of any instance with this id
                //add a new task to the map
                if (!map.containsKey(id)) {
                    map.put(id, new TraceTask());
                }

                //get first activation of the instance, to calculate jitter
                int firstExecutionStart = lineFirst.nextInt();
                int jitter = firstExecutionStart - activationTime;
                
                //get last end of execution (actual finish time), to calculate
                //response time
                int finishTime = -1;
                while (lineSecond.hasNextInt()) {
                    finishTime = lineSecond.nextInt();
                }
                
                //response time is calculated
                int responseTime = finishTime - activationTime;

                //current task is the task with id that has been read from
                //the trace file
                TraceTask currentTask = map.get(id);

                //add response time to frequency table of response times and
                //whether deadline was missed
                currentTask.responseTimeFT.addTime(responseTime, missedDeadline);
                
                //add jitter to frequency table of jitter values and
                //whether deadline was missed
                currentTask.jitterFT.addTime(jitter, missedDeadline);

                //if deadline isn't missed, increment counter for executed
                //instances
                if (!missedDeadline) {
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