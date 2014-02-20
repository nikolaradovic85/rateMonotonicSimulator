package TaskSchedulingSimulator;

/**
 *
 * @author nikola
 */
public class Main {

    public static void main(String[] args) {

        TraceFileStatisticAnalyzer tr1 = new TraceFileStatisticAnalyzer(
                "io/trace/edfSoftSpecial.log");
        SimulatorLogger logger = new SimulatorLogger("io/statistic/speciallog1.log");
        logger.log(tr1);
        logger.saveLogToFile();
//
//        TraceFileStatisticAnalyzer tr2 = new TraceFileStatisticAnalyzer(
//                "io/trace/rmSoft.log");
//        SimulatorLogger logger2 = new SimulatorLogger("io/statistic/log2.log");
//        logger2.log(tr2);
//        logger2.saveLogToFile();
//
//        TraceFileStatisticAnalyzer tr3 = new TraceFileStatisticAnalyzer(
//                "io/trace/dmHard.log");
//        SimulatorLogger logger3 = new SimulatorLogger("io/statistic/log3.log");
//        logger3.log(tr3);
//        logger3.saveLogToFile();
//
//        TraceFileStatisticAnalyzer tr4 = new TraceFileStatisticAnalyzer(
//                "io/trace/dmSoft.log");
//        SimulatorLogger logger4 = new SimulatorLogger("io/statistic/log4.log");
//        logger4.log(tr4);
//        logger4.saveLogToFile();
//
//        TraceFileStatisticAnalyzer tr5 = new TraceFileStatisticAnalyzer(
//                "io/trace/edfHard.log");
//        SimulatorLogger logger5 = new SimulatorLogger("io/statistic/log5.log");
//        logger5.log(tr5);
//        logger5.saveLogToFile();
//
//        TraceFileStatisticAnalyzer tr6 = new TraceFileStatisticAnalyzer(
//                "io/trace/edfSoft.log");
//        SimulatorLogger logger6 = new SimulatorLogger("io/statistic/log6.log");
//        logger6.log(tr6);
//        logger6.saveLogToFile();

//        Simulation rmHard  = new Simulation(
//                "Rate Monotonic Hard",
//                Simulation.SimulationTypes.HARD,
//                "io/input/input.txt", 
//                "io/trace/rmHard.log",  
//                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
//        rmHard.start();
//       
//        Simulation rmSoft  = new Simulation(
//                "Rate Monotonic Soft",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/simpleInput.txt", 
//                "io/trace/rmSoft.log",  
//                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
//        rmSoft.start();
//        
//        TraceFileStatisticAnalyzer srmS = new TraceFileStatisticAnalyzer(
//                "io/trace/rmSoft.log");
//        SimulatorLogger logger2 = new SimulatorLogger("io/statistic/statisticrmSoft.log");
//        logger2.log(srmS);
//        logger2.saveLogToFile();
//        Simulation edfHard = new Simulation(
//                "Earliest Deadline First Hard",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/specialinput.txt", 
//                "io/trace/edfSoftSpecial.log", 
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        edfHard.start();
////        
//        Simulation edfSoft = new Simulation(
//                "Earliest Deadline First Soft",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/input.txt", 
//                "io/trace/edfSoft.log", 
//                InstanceOfPeriodicTask.Comparators.ABSOLUTE_DEADLINE);
//        edfSoft.start();
//        
//        Simulation dmHard = new Simulation(
//                "Deadline Monotonic Hard",
//                Simulation.SimulationTypes.HARD,
//                "io/input/input.txt", 
//                "io/trace/dmHard.log", 
//                InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE);
//        dmHard.start();
//        
//        Simulation dmSoft = new Simulation(
//                "Deadline Monotonic Soft",
//                Simulation.SimulationTypes.SOFT,
//                "io/input/input.txt", 
//                "io/trace/dmSoft.log", 
//                InstanceOfPeriodicTask.Comparators.RELATIVE_DEADLINE);
//        dmSoft.start();
    }
}
