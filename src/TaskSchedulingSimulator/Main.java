package TaskSchedulingSimulator;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikola
 */
public class Main {

    public static void main(String[] args) {
        int numberOfTests = 100;
        ArrayList<Double> avgProbability1 = new ArrayList<Double>();
        ArrayList<Double> avgProbability2 = new ArrayList<Double>();
        ArrayList<Double> avgProbability3 = new ArrayList<Double>();
        for(int i=0;i<3;i++){
            avgProbability1.add(0.0);
            avgProbability2.add(0.0);
            avgProbability3.add(0.0);
        }
        for (int i = 1; i <= numberOfTests; i++) {
        
//        create a simulation, set it up and start it
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
        avgProbability1.set(0, avgProbability1.get(0) + tr1.getDeadlineMissProbability(1));
        avgProbability1.set(1, avgProbability1.get(1) + tr1.getDeadlineMissProbability(2));
        avgProbability1.set(2, avgProbability1.get(2) + tr1.getDeadlineMissProbability(3));

        TraceFileStatisticAnalyzer tr2 = new TraceFileStatisticAnalyzer(
                "io/trace/soft2.trc");
        SimulatorLogger logger2 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft2.log");
        logger2.log(tr2);
        logger2.saveLogToFile();
        avgProbability2.set(0, avgProbability2.get(0) + tr2.getDeadlineMissProbability(1));
        avgProbability2.set(1, avgProbability2.get(1) + tr2.getDeadlineMissProbability(2));
        avgProbability2.set(2, avgProbability2.get(2) + tr2.getDeadlineMissProbability(3));    
        
        TraceFileStatisticAnalyzer tr3 = new TraceFileStatisticAnalyzer(
                "io/trace/soft3.trc");
        SimulatorLogger logger3 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft3.log");
        logger3.log(tr3);
        logger3.saveLogToFile();
        avgProbability3.set(0, avgProbability3.get(0) + tr3.getDeadlineMissProbability(1));
        avgProbability3.set(1, avgProbability3.get(1) + tr3.getDeadlineMissProbability(2));
        avgProbability3.set(2, avgProbability3.get(2) + tr3.getDeadlineMissProbability(3));
        }
        
        for(Double i : avgProbability1){
            i=i/numberOfTests;
            System.out.println(i);
        }
        System.out.println();
        for(Double i : avgProbability2){
            i=i/numberOfTests;
            System.out.println(i);
        }
        System.out.println();
        for(Double i : avgProbability3){
            i=i/numberOfTests;
            System.out.println(i);
        }
        System.out.println();

//        int numberOfTests = 10;
//        ArrayList<Double> avgProbability1 = new ArrayList<Double>();
//        ArrayList<Double> avgProbability2 = new ArrayList<Double>();
//        ArrayList<Double> avgProbability3 = new ArrayList<Double>();
//       
//        for (int i = 0; i < 2; i++) {
//            avgProbability1.add(0.0);
//            avgProbability2.add(0.0);
//            avgProbability3.add(0.0);
//        }
//        for (int i = 1; i <= numberOfTests; i++) {
//
////        create a simulation, set it up and start it
//            Simulation rmSoft1 = new Simulation(
//                    "Rate Monotonic Soft 1",
//                    Simulation.SimulationTypes.SOFT,
//                    "io/input/input1.txt",
//                    "io/trace/rmsoft1.trc",
//                    InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
//            rmSoft1.start();
//            Simulation rmSoft2 = new Simulation(
//                    "Rate Monotonic Soft 2",
//                    Simulation.SimulationTypes.SOFT,
//                    "io/input/input2.txt",
//                    "io/trace/rmsoft2.trc",
//                    InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
//            rmSoft2.start();
//
//            Simulation rmSoft3 = new Simulation(
//                    "Rate Monotonic Soft 3",
//                    Simulation.SimulationTypes.SOFT,
//                    "io/input/input3.txt",
//                    "io/trace/rmsoft3.trc",
//                    InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
//            rmSoft3.start();
//
//            //use someThread.join() to wait until the thread is finished
//            try {
//                rmSoft1.join();
//                rmSoft2.join();
//                rmSoft3.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            //analyze trace file created by the simulation
//            TraceFileStatisticAnalyzer tr1 = new TraceFileStatisticAnalyzer(
//                    "io/trace/rmsoft1.trc");
//            SimulatorLogger logger1 = new SimulatorLogger("io/statistic/" + i + "-rmsoft1.log");
//            logger1.log(tr1);
//            logger1.saveLogToFile();
//            avgProbability1.set(0, avgProbability1.get(0) + tr1.getDeadlineMissProbability(1));
//            avgProbability1.set(1, avgProbability1.get(1) + tr1.getDeadlineMissProbability(2));
//
//            TraceFileStatisticAnalyzer tr2 = new TraceFileStatisticAnalyzer(
//                    "io/trace/rmsoft2.trc");
//            SimulatorLogger logger2 = new SimulatorLogger("io/statistic/" + i + "-rmsoft2.log");
//            logger2.log(tr2);
//            logger2.saveLogToFile();
//            avgProbability2.set(0, avgProbability2.get(0) + tr2.getDeadlineMissProbability(1));
//            avgProbability2.set(1, avgProbability2.get(1) + tr2.getDeadlineMissProbability(2));
//
//            TraceFileStatisticAnalyzer tr3 = new TraceFileStatisticAnalyzer(
//                    "io/trace/rmsoft3.trc");
//            SimulatorLogger logger3 = new SimulatorLogger("io/statistic/" + i + "-rmsoft3.log");
//            logger3.log(tr3);
//            logger3.saveLogToFile();
//            avgProbability3.set(0, avgProbability3.get(0) + tr3.getDeadlineMissProbability(1));
//            avgProbability3.set(1, avgProbability3.get(1) + tr3.getDeadlineMissProbability(2));
//        }
//
//        for (Double i : avgProbability1) {
//            i = i / numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
//        for (Double i : avgProbability2) {
//            i = i / numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
//        for (Double i : avgProbability3) {
//            i = i / numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();

    }
}
