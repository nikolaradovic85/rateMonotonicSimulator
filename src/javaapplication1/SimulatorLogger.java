package javaapplication1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class SimulatorLogger {

    private PrintWriter writer;
    
    /**
     *Constructor for SimulatorLogger.
     * 
     * @param pFilename Log will be saved to this file
     */
    public SimulatorLogger(String pFilename) {
        try {
            this.writer = new PrintWriter(pFilename, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(SimulatorLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *Takes an InstanceOfPeriodicTask and logs its toString representation.
     * 
     * @param instance InstanceOfPeriodicTask to be logged
     */
    public void log(InstanceOfPeriodicTask instance) {
        instance.removeRedundantTimesFromLists();
        writer.println(instance);
    }
    
    /**
     *Saves log to file.
     */
    public void saveLogToFile() {
        writer.close();
    }
}
