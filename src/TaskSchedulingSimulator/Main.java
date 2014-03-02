package TaskSchedulingSimulator;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikola
 */
public class Main {

    public static void main(String[] args) {

        for (int i = 1; i <= 20; i++) {
        
        //create a simulation, set it up and start it
        Simulation rmSoft1 = new Simulation(
                "Rate Monotonic Soft 1",
                Simulation.SimulationTypes.SOFT,
                "io/input/soft1.txt",
                "io/trace/soft1.trc",
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        rmSoft1.start();
        
        Simulation rmSoft2 = new Simulation(
                "Rate Monotonic Soft 2",
                Simulation.SimulationTypes.SOFT,
                "io/input/soft2.txt",
                "io/trace/soft2.trc",
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        rmSoft2.start();
        
        Simulation rmSoft3 = new Simulation(
                "Rate Monotonic Soft 3",
                Simulation.SimulationTypes.SOFT,
                "io/input/soft3.txt",
                "io/trace/soft3.trc",
                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
        rmSoft3.start();
        
        //use someThread.join() to wait until the thread is finished
        try {
            rmSoft1.join();
            rmSoft2.join();
            rmSoft3.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //analyze trace file created by the simulation
        TraceFileStatisticAnalyzer tr1 = new TraceFileStatisticAnalyzer(
                "io/trace/soft1.trc");
        SimulatorLogger logger1 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft1.log");
        logger1.log(tr1);
        logger1.saveLogToFile();
        
        TraceFileStatisticAnalyzer tr2 = new TraceFileStatisticAnalyzer(
                "io/trace/soft2.trc");
        SimulatorLogger logger2 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft2.log");
        logger2.log(tr2);
        logger2.saveLogToFile();
        
        TraceFileStatisticAnalyzer tr3 = new TraceFileStatisticAnalyzer(
                "io/trace/soft3.trc");
        SimulatorLogger logger3 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft3.log");
        logger3.log(tr3);
        logger3.saveLogToFile();
        
        }

    }
}
