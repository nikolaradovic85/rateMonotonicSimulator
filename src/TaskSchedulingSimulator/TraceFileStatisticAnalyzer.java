package TaskSchedulingSimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 *
 * @author nikola
 */
public class TraceFileStatisticAnalyzer {

    private ArrayList<Double> avgResponseTimeList;//for each periodic task
    private ArrayList<Double> minResponseTimeList;//for each periodic task
    private ArrayList<Double> maxResponseTimeList;//for each periodic task
    private ArrayList<Double> avgExecutionTimesRatioList;//for each periodic task, 
    //calculates average ratio executed part of instance : total execution time of instance,
    //for each instance that failed to be executed before deadline
    private ArrayList<Double> minExecutionTimesRatioList;
    private ArrayList<Double> maxExecutionTimesRatioList;
    private ArrayList<Integer> executedCounterList;//counts instances that executed before deadline
    private ArrayList<Integer> missedCounterList;//counts instances that missed to execute before deadline
    private ArrayList<Double> deadlineMissProbability;
    private HashMap<Integer, Integer> map;

    public TraceFileStatisticAnalyzer(String traceFile) {
        this.avgResponseTimeList = new ArrayList<>();
        this.minResponseTimeList = new ArrayList<>();
        this.maxResponseTimeList = new ArrayList<>();
        this.avgExecutionTimesRatioList = new ArrayList<>();
        this.minExecutionTimesRatioList = new ArrayList<>();
        this.maxExecutionTimesRatioList = new ArrayList<>();
        this.executedCounterList = new ArrayList<>();
        this.missedCounterList = new ArrayList<>();
        this.deadlineMissProbability = new ArrayList<>();
        this.map = new HashMap<>();
        parseTraceFile(traceFile);
        for(int i=0; i<missedCounterList.size();i++){
            deadlineMissProbability.add((1.0*missedCounterList.get(i))/(missedCounterList.get(i)+executedCounterList.get(i)));
        }
    }
    
    public double getDeadlineMissProbability(int index){
        return deadlineMissProbability.get(map.get(index));
    }
    /**
     * reads trace file and calculates statistic parameters
     *
     * @param traceFile input trace file
     */
    public void parseTraceFile(String traceFile) {
        try {
            Scanner scan = new Scanner(new File(traceFile));
            int periodicTaskCounter = 0;//number of diferent id, seen so far
            int listIndex;//index in statistic list (used in hash map as value) 

            // while not end of file
            while (scan.hasNextLine()) {
                try {
                    // Read id of periodic task - first int in trace is periodic task id
                    int id = scan.nextInt();
                    //creating helper variables
                    double responseTime = 0;
                    double realExecutionTime = 0;

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
                    if (!map.containsKey(id)) {
                        //add id to map
                        map.put(id, periodicTaskCounter);
                        //count new periodic task
                        periodicTaskCounter++;
                       
                        //if not missed deadline
                        if (missedDeadline == -1) {
                            //set appropriate counters
                            //instance didn't miss deadline and this instance is first instance
                            //with this id
                            //add to end of lists, executedCounter = 1, missedCounter = 0
                            executedCounterList.add(1);
                            missedCounterList.add(0);

                            //get last int from second line (time when last part of instace finished execution)
                            int finishTime = -1;
                            while (lineSecond.hasNextInt()) {
                                finishTime = lineSecond.nextInt();
                            }

                            responseTime = finishTime - activationTime;

                            //this is first instance with this id and deadline isn't missed, so
                            //avg, min, max response time = response time of instance => add it to end of lists
                            avgResponseTimeList.add(responseTime);
                            minResponseTimeList.add(responseTime);
                            maxResponseTimeList.add(responseTime);

                            //initializing ratio lists with values appropriate for later usage
                            //avg = 0, min = highest possible Double, so next instance with this id 
                            //that missed deadline will set own ratio as min
                            this.avgExecutionTimesRatioList.add(0.);
                            this.minExecutionTimesRatioList.add(Double.POSITIVE_INFINITY);
                            this.maxExecutionTimesRatioList.add(Double.NEGATIVE_INFINITY);

                        } else {//if missed deadline

                            //set appropriate counters
                            //instance did miss deadline and this instance is first instance
                            //with this id
                            //add to end of lists, executedCounter = 0, missedCounter = 1
                            missedCounterList.add(1);
                            executedCounterList.add(0);

                            //for each piece of execution substract finishtime - starttime and 
                            //add result to realExecutionTime(which is set to zero in begining)
                            while (lineFirst.hasNextInt() && lineSecond.hasNextInt()) {
                                int start = lineFirst.nextInt();
                                int end = lineSecond.nextInt();
                                realExecutionTime += end - start;
                            }

                            //initializing response time lists with values appropriate for later usage
                            //avg = 0, min = highest possible Double, so next instance with this id 
                            //that didn't miss deadline will set own respons time as min
                            avgResponseTimeList.add(0.);
                            minResponseTimeList.add(Double.POSITIVE_INFINITY);
                            maxResponseTimeList.add(Double.NEGATIVE_INFINITY);

                            //this is first instance with this id and deadline is missed, so
                            //avg, min, max ratio = instance's ratio => add it to end of lists
                            this.avgExecutionTimesRatioList.add(realExecutionTime / totalExecutionTime);//add to empty list
                            this.minExecutionTimesRatioList.add(realExecutionTime / totalExecutionTime);
                            this.maxExecutionTimesRatioList.add(realExecutionTime / totalExecutionTime);

                        }

                    } else {//if id is allready in map
                        
                        //get list index for this id from map
                        listIndex = map.get(id);

                        if (missedDeadline == -1) {//if not missed deadline

                            //appropriate counter ++
                            executedCounterList.set(listIndex, executedCounterList.get(listIndex) + 1);

                            //get last int from second line (time when last part of instace finished execution)
                            int finishTime = -1;
                            while (lineSecond.hasNextInt()) {
                                finishTime = lineSecond.nextInt();
                            }

                            responseTime = finishTime - activationTime;

                            if (minResponseTimeList.get(listIndex).compareTo(responseTime) > 0) {
                                minResponseTimeList.set(listIndex, responseTime);
                            } else if (maxResponseTimeList.get(listIndex).compareTo(responseTime) < 0) {
                                maxResponseTimeList.set(listIndex, responseTime);
                            }
                            //if this is not first instance of task that is executed properly
                            if (!executedCounterList.get(listIndex).equals(1)) {
                                //newAvgValue = (newValue + oldAvgValue * oldCounter) / newCounter
                                avgResponseTimeList.set(listIndex,
                                        (responseTime
                                        + avgResponseTimeList.get(listIndex)
                                        * (executedCounterList.get(listIndex) - 1))
                                        / executedCounterList.get(listIndex));
                            } else {//if first instance executed before deadline
                                avgResponseTimeList.set(listIndex, responseTime);
                            }
                        } else {//if missed deadline

                            //appropriate counter ++
                            missedCounterList.set(listIndex, missedCounterList.get(listIndex) + 1);

                            //for each piece of execution substract finishtime - starttime and 
                            //add result to realExecutionTime(which is set to zero in begining)
                            while (lineFirst.hasNextInt() && lineSecond.hasNextInt()) {
                                int start = lineFirst.nextInt();
                                int end = lineSecond.nextInt();
                                realExecutionTime += end - start;
                            }
                            
                            double ratio = realExecutionTime / totalExecutionTime;
                            
                            if (minExecutionTimesRatioList.get(listIndex).compareTo(ratio) > 0) {
                                minExecutionTimesRatioList.set(listIndex, ratio);
                            } else if (maxExecutionTimesRatioList.get(listIndex).compareTo(ratio) < 0) {
                                maxExecutionTimesRatioList.set(listIndex, ratio);
                            }
                            
                            //if not first instance of task that missed deadline
                            if (!missedCounterList.get(listIndex).equals(1)) {
                                //newAvgValue = (newValue + oldAvgValue * oldCounter) / newCounter
                                avgExecutionTimesRatioList.set(listIndex,
                                        (ratio + avgExecutionTimesRatioList.get(listIndex)
                                        * (missedCounterList.get(listIndex) - 1))
                                        / missedCounterList.get(listIndex));
                            } else {//if first ratio is avgRatio
                                avgExecutionTimesRatioList.set(listIndex, ratio);
                            }
                        }

                    }
                    scan.nextLine();//moving scanner on next line, one line below missed deadline 
                    scan.nextLine();//scanning empty line

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
        for (int id = 1; id <= map.size(); id++) {
            int listIndex = map.get(id);
            
            double noFinishedInstances = this.executedCounterList.get(listIndex);
            double noMissedDeadlines = this.missedCounterList.get(listIndex);
            double deadlineMissProbabilityLocal = noMissedDeadlines / (noFinishedInstances + noMissedDeadlines);
            sbResult.append(id);
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
            if (!this.executedCounterList.get(listIndex).equals(0)) {
                sbResult.append("Average response time (for finished instances): ");
                sbResult.append(this.avgResponseTimeList.get(listIndex));
                sbResult.append(System.lineSeparator());
                sbResult.append("Minimum response time (for finished instances): ");
                sbResult.append(this.minResponseTimeList.get(listIndex));
                sbResult.append(System.lineSeparator());
                sbResult.append("Maximum response time (for finished instances): ");
                sbResult.append(this.maxResponseTimeList.get(listIndex));
                sbResult.append(System.lineSeparator());
            }
            if (!this.missedCounterList.get(listIndex).equals(0)) {
                sbResult.append("Average ratio (executed time / total time): ");
                sbResult.append(this.avgExecutionTimesRatioList.get(listIndex));
                sbResult.append(System.lineSeparator());
                sbResult.append("Min ratio (executed time / total time): ");
                sbResult.append(this.minExecutionTimesRatioList.get(listIndex));
                sbResult.append(System.lineSeparator());
                sbResult.append("Max ratio (executed time / total time): ");
                sbResult.append(this.maxExecutionTimesRatioList.get(listIndex));
                sbResult.append(System.lineSeparator());
            }
            sbResult.append(System.lineSeparator());
            sbResult.append(System.lineSeparator());
        }

        return sbResult.toString();
    }

}
