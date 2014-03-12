package TaskSchedulingSimulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
        args[0] = "2";
        
        args[1] = "input1.txt";
        args[2] = "RM";
        args[3] = "SOFT";
        
        args[4] = "specialinput.txt";
        args[5] = "RM";
        args[6] = "SOFT";
        
        args[7] = "5";
        
        int noOfSimulations = Integer.parseInt(args[0]);
        int noOfRepetitions = Integer.parseInt(args[noOfSimulations * 3 + 1]);
        HashMap<String, TraceFileStatisticAnalyzer[]> stats = new HashMap<>();
        
        
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
                    stats.put(inputFile, new TraceFileStatisticAnalyzer[noOfRepetitions]);
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
            //and now the analysis of trace files begins

            //for each input file
            for (String input : inputFiles) {
                TraceFileStatisticAnalyzer tfsa = 
                        new TraceFileStatisticAnalyzer(traceDir + repetition + input + ".trc");
                
                TraceFileStatisticAnalyzer t[] = stats.get(input);
                t[repetition - 1] = tfsa;
                stats.put(input, t);
                /*SimulatorLogger logger = new SimulatorLogger(statsDir + repetition + input + ".log");
                logger.log(tfsa);
                logger.saveLogToFile();*/
            }
        
        }
        
        
        
        
//        int numberOfTests = 100;
//        ArrayList<Double> avgProbability1 = new ArrayList<>();
//        ArrayList<Double> avgProbability2 = new ArrayList<>();
//        ArrayList<Double> avgProbability3 = new ArrayList<>();
//        
//        for(int i=0;i<3;i++){
//            avgProbability1.add(0.0);
//            avgProbability2.add(0.0);
//            avgProbability3.add(0.0);
//        }
//        for (int i = 1; i <= numberOfTests; i++) {
//        
//        //create a simulation, set it up and start it
//        Simulation rmSoft1 = new Simulation(
//                "Rate Monotonic Soft 1",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/soft1.txt",
//                "io/trace/soft1.trc",
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        rmSoft1.start();
//        Simulation rmSoft2 = new Simulation(
//                "Rate Monotonic Soft 2",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/soft2.txt",
//                "io/trace/soft2.trc",
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        rmSoft2.start();
//        
//        Simulation rmSoft3 = new Simulation(
//                "Rate Monotonic Soft 3",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/soft3.txt",
//                "io/trace/soft3.trc",
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        rmSoft3.start();
//        
//        //use someThread.join() to wait until the thread is finished
//        try {
//            rmSoft1.join();
//            rmSoft2.join();
//            rmSoft3.join();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //analyze trace file created by the simulation
//        TraceFileStatisticAnalyzer tr1 = new TraceFileStatisticAnalyzer(
//                "io/trace/soft1.trc");
//        SimulatorLogger logger1 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft1.log");
//        logger1.log(tr1);
//        logger1.saveLogToFile();
//        avgProbability1.set(0, avgProbability1.get(0) + tr1.getDeadlineMissProbability(1));
//        avgProbability1.set(1, avgProbability1.get(1) + tr1.getDeadlineMissProbability(2));
//        avgProbability1.set(2, avgProbability1.get(2) + tr1.getDeadlineMissProbability(3));
//
//        TraceFileStatisticAnalyzer tr2 = new TraceFileStatisticAnalyzer(
//                "io/trace/soft2.trc");
//        SimulatorLogger logger2 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft2a.log");
//        logger2.log(tr2);
//        logger2.saveLogToFile();
//        avgProbability2.set(0, avgProbability2.get(0) + tr2.getDeadlineMissProbability(1));
//        avgProbability2.set(1, avgProbability2.get(1) + tr2.getDeadlineMissProbability(2));
//        avgProbability2.set(2, avgProbability2.get(2) + tr2.getDeadlineMissProbability(3));    
//        
//        TraceFileStatisticAnalyzer tr3 = new TraceFileStatisticAnalyzer(
//                "io/trace/soft3.trc");
//        SimulatorLogger logger3 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft3a.log");
//        logger3.log(tr3);
//        logger3.saveLogToFile();
//        avgProbability3.set(0, avgProbability3.get(0) + tr3.getDeadlineMissProbability(1));
//        avgProbability3.set(1, avgProbability3.get(1) + tr3.getDeadlineMissProbability(2));
//        avgProbability3.set(2, avgProbability3.get(2) + tr3.getDeadlineMissProbability(3));
//        }
//        
//        for(Double i : avgProbability1){
//            i=i/numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
//        for(Double i : avgProbability2){
//            i=i/numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
//        for(Double i : avgProbability3){
//            i=i/numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
//
//  ---------------------------------------------      
        
        
        
        
//        int numberOfTests = 100;
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

        
//        //specialinput.txt
//        
//        int numberOfTests = 1;
//        ArrayList<Double> avgProbability1 = new ArrayList<Double>();
//
//        for(int i=0;i<3;i++){
//            avgProbability1.add(0.0);
//
//        }
//        for (int i = 1; i <= numberOfTests; i++) {
//        
//        //create a simulation, set it up and start it
//        Simulation rmSoft1 = new Simulation(
//                "Rate Monotonic Soft 1",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/specialinput.txt",
//                "io/trace/soft100.trc",
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        rmSoft1.start();
//
//        //use someThread.join() to wait until the thread is finished
//        try {
//            rmSoft1.join();
//
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //analyze trace file created by the simulation
//        TraceFileStatisticAnalyzer tr1 = new TraceFileStatisticAnalyzer(
//                "io/trace/soft100.trc");
//        SimulatorLogger logger1 = new SimulatorLogger("io/statistic/"+ i +"-edfsoft100.log");
//        logger1.log(tr1);
//        logger1.saveLogToFile();
//        avgProbability1.set(0, avgProbability1.get(0) + tr1.getDeadlineMissProbability(1));
//        avgProbability1.set(1, avgProbability1.get(1) + tr1.getDeadlineMissProbability(2));
//        avgProbability1.set(2, avgProbability1.get(2) + tr1.getDeadlineMissProbability(3));
//
//
//
//        }
//        
//        for(Double i : avgProbability1){
//            i=i/numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
        
        
        //inputFT.txt
        
//        int numberOfTests = 100;
//        ArrayList<Double> avgProbability1 = new ArrayList<Double>();
//
//        for(int i=0;i<2;i++){
//            avgProbability1.add(0.0);
//
//        }
//        for (int i = 1; i <= numberOfTests; i++) {
//        
//        //create a simulation, set it up and start it
//        Simulation rmSoft1 = new Simulation(
//                "Rate Monotonic Soft 1",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/inputFT.txt",
//                "io/trace/FTedf.trc",
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        rmSoft1.start();
//
//        //use someThread.join() to wait until the thread is finished
//        try {
//            rmSoft1.join();
//
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //analyze trace file created by the simulation
//        TraceFileStatisticAnalyzer tr1 = new TraceFileStatisticAnalyzer(
//                "io/trace/FTedf.trc");
//        SimulatorLogger logger1 = new SimulatorLogger("io/statistic/"+ i +"-ftEDF.log");
//        logger1.log(tr1);
//        logger1.saveLogToFile();
//        avgProbability1.set(0, avgProbability1.get(0) + tr1.getDeadlineMissProbability(1));
//        avgProbability1.set(1, avgProbability1.get(1) + tr1.getDeadlineMissProbability(2));
//
//
//
//        }
//        
//        for(Double i : avgProbability1){
//            i=i/numberOfTests;
//            System.out.println(i);
//        }
//        System.out.println();
 
    }
}
