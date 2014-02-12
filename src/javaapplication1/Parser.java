package javaapplication1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javaapplication1.Main.endOfTime;

/**
 * Class Parser contains static methods that read input files 
 * (or manual user input).
 * 
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class Parser {

    /**
     * Manual user input.
     * @param input 
     */
    public static void userInput(ArrayList<periodicTask> input) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Number of periodic tasks: ");
        final int numberOfPeriodicTasks = scan.nextInt();

        for (int i = 0; i < numberOfPeriodicTasks; i++) {
            System.out.println((i + 1) + ". periodic task:");
            System.out.print("Phase: ");
            int phi = scan.nextInt();
            System.out.print("Period: ");
            int taskPeriod = scan.nextInt();
            System.out.print("Execution Time: ");
            int cTaskExecutionTime = scan.nextInt();
            periodicTask temp = new periodicTask(taskPeriod, phi, cTaskExecutionTime);
            input.add(temp);
        }

        System.out.print("End of time period: ");
        endOfTime = scan.nextInt();
    }
    
    /**
     * Reads a file and populates input, execution time calculated from uniform
     * distribution
     *
     * @param input ArrayList of periodicTask to be populated
     * @param f file which is parsed
     */
    public static void readInputFromFile(ArrayList<periodicTask> input,
            File f) {
        try {
            Scanner scan = new Scanner(f);

            int numberOfPeriodicTasks = scan.nextInt();

            for (int i = 0; i < numberOfPeriodicTasks; i++) {
                int phi = scan.nextInt();
                int taskPeriod = scan.nextInt();
                int cMin = scan.nextInt();
                int cMax = scan.nextInt();
                int randomDistribution = scan.nextInt(); // not used
                //at this time uniform distribution is used - Math.random()
                int cTaskExecutionTime = cMin + (int) (Math.random() * (cMax - cMin + 1));
                System.out.println(cTaskExecutionTime);
                periodicTask temp = new periodicTask(taskPeriod, phi, cTaskExecutionTime);
                input.add(temp);
            }

            endOfTime = scan.nextInt();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        }

    }

    /**
     * Reads a file and populates input
     *
     * @param input ArrayList of periodicTask to be populated
     * @param f file which is parsed
     */
    public static void simpleReadInputFromFile(ArrayList<periodicTask> input,
            File f) {
        try {
            Scanner scan = new Scanner(f);

            int numberOfPeriodicTasks = scan.nextInt();

            for (int i = 0; i < numberOfPeriodicTasks; i++) {
                int phi = scan.nextInt();
                int taskPeriod = scan.nextInt();
                int cTaskExecutionTime = scan.nextInt();
                periodicTask temp = new periodicTask(taskPeriod, phi, cTaskExecutionTime);
                input.add(temp);
            }

            endOfTime = scan.nextInt();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        }
    }
}
