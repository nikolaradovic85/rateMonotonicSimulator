package TaskSchedulingSimulator;

/**
 *
 * @author nikola
 */
public class Main {

    public static void main(String[] args) {

        Simulation rmSoft = new Simulation(
                "Rate Monotonic Soft",
                Simulation.SimulationTypes.SOFT,
                "io/input/input.txt",
                "io/trace/rmSoft.log",
                InstanceOfPeriodicTask.Comparators.TASK_PERIOD);
        rmSoft.start();

        TraceFileStatisticAnalyzer tr2 = new TraceFileStatisticAnalyzer(
                "io/trace/rmSoft.log");
        SimulatorLogger logger2 = new SimulatorLogger("io/statistic/log2.log");
        logger2.log(tr2);
        logger2.saveLogToFile();

    }
}
