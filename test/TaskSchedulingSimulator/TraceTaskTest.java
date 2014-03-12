package TaskSchedulingSimulator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class TraceTaskTest {
    
    private TraceTask t;
    
    public TraceTaskTest() {
    }
    
    @Before
    public void setUp() {
        t = new TraceTask();
        
        t.addPossibleMinResponseTime(3);
        t.addPossibleMaxResponseTime(3);
        t.addResponseTimeToFreqTable(3);
        t.incrementExecutedCounter();
        
        t.addPossibleMinResponseTime(2);
        t.addPossibleMaxResponseTime(2);
        t.addResponseTimeToFreqTable(2);
        t.incrementMissedCounter();
        
        t.addPossibleMinResponseTime(13);
        t.addPossibleMaxResponseTime(13);
        t.addResponseTimeToFreqTable(13);
        t.incrementExecutedCounter();
        
        t.addPossibleMinResponseTime(3);
        t.addPossibleMaxResponseTime(3);
        t.addResponseTimeToFreqTable(3);
        t.incrementExecutedCounter();
    }

    /**
     * Test of addPossibleMinResponseTime method, of class TraceTask.
     */
    @Test
    public void testAddPossibleMinResponseTime() {
        System.out.println("addPossibleMinResponseTime");
        TraceTask instance = new TraceTask();
        
        instance.addPossibleMinResponseTime(5);
        assertEquals(5, instance.getMinResponseTime());
        
        instance.addPossibleMinResponseTime(10);
        assertEquals(5, instance.getMinResponseTime());
        
        instance.addPossibleMinResponseTime(-100);
        assertEquals(-100, instance.getMinResponseTime());
    }

    /**
     * Test of addPossibleMaxResponseTime method, of class TraceTask.
     */
    @Test
    public void testAddPossibleMaxResponseTime() {
        System.out.println("addPossibleMaxResponseTime");
        TraceTask instance = new TraceTask();
        
        instance.addPossibleMaxResponseTime(5);
        assertEquals(5, instance.getMaxResponseTime());
        
        instance.addPossibleMaxResponseTime(10);
        assertEquals(10, instance.getMaxResponseTime());
        
        instance.addPossibleMaxResponseTime(-100);
        assertEquals(10, instance.getMaxResponseTime());
    }

    /**
     * Test of addResponseTimeToFreqTable method, of class TraceTask.
     */
    @Test
    public void testAddResponseTimeToFreqTable() {
        System.out.println("addResponseTimeToFreqTable");
        TraceTask instance = new TraceTask();
        
        //test fresh responseTimeFreqTable
        assertEquals(0, instance.getAverageResponseTime(), 0.0001);
        assertEquals(Integer.MAX_VALUE, instance.getMinResponseTime());
        assertEquals(0, instance.getMaxResponseTime());
        assertEquals(0, instance.getMissedCounter());
        assertEquals(0, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addPossibleMinResponseTime(3);
        instance.addPossibleMaxResponseTime(3);
        instance.addResponseTimeToFreqTable(3);
        instance.incrementExecutedCounter();
        
        //test responseTimeFreqTable
        assertEquals(3, instance.getAverageResponseTime(), 0.0001);
        assertEquals(3, instance.getMinResponseTime());
        assertEquals(3, instance.getMaxResponseTime());
        assertEquals(0, instance.getMissedCounter());
        assertEquals(1, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addPossibleMinResponseTime(2);
        instance.addPossibleMaxResponseTime(2);
        instance.addResponseTimeToFreqTable(2);
        instance.incrementMissedCounter();
        
        //test responseTimeFreqTable
        assertEquals(5, instance.getAverageResponseTime(), 0.0001);
        assertEquals(2, instance.getMinResponseTime());
        assertEquals(3, instance.getMaxResponseTime());
        assertEquals(1, instance.getMissedCounter());
        assertEquals(1, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addPossibleMinResponseTime(13);
        instance.addPossibleMaxResponseTime(13);
        instance.addResponseTimeToFreqTable(13);
        instance.incrementExecutedCounter();

        //test responseTimeFreqTable
        assertEquals(9, instance.getAverageResponseTime(), 0.0001);
        assertEquals(2, instance.getMinResponseTime());
        assertEquals(13, instance.getMaxResponseTime());
        assertEquals(1, instance.getMissedCounter());
        assertEquals(2, instance.getExecutedCounter());
    }

    /**
     * Test of getDeadlineMissProbability method, of class TraceTask.
     */
    @Test
    public void testGetDeadlineMissProbability() {
        System.out.println("getDeadlineMissProbability");
        
        double result = t.getDeadlineMissProbability();
        assertEquals(0.25, result, 0.0001);
    }

    /**
     * Test of getAverageResponseTime method, of class TraceTask.
     */
    @Test
    public void testGetAverageResponseTime() {
        System.out.println("getAverageResponseTime");
        

        double result = t.getAverageResponseTime();
        assertEquals(7, result, 0.0001);
    }

    /**
     * Test of incrementExecutedCounter method, of class TraceTask.
     */
    @Test
    public void testIncrementExecutedCounter() {
        System.out.println("incrementExecutedCounter");
        TraceTask instance = new TraceTask();        
        instance.incrementExecutedCounter();
        assertEquals(1, instance.getExecutedCounter());
    }

    /**
     * Test of incrementMissedCounter method, of class TraceTask.
     */
    @Test
    public void testIncrementMissedCounter() {
        System.out.println("incrementMissedCounter");
        TraceTask instance = new TraceTask();
        instance.incrementMissedCounter();
        assertEquals(1, instance.getMissedCounter());
    }

    /**
     * Test of getMinResponseTime method, of class TraceTask.
     */
    @Test
    public void testGetMinResponseTime() {
        System.out.println("getMinResponseTime");       
        assertEquals(2, t.getMinResponseTime());
    }

    /**
     * Test of getMaxResponseTime method, of class TraceTask.
     */
    @Test
    public void testGetMaxResponseTime() {
        System.out.println("getMaxResponseTime");
        assertEquals(13, t.getMaxResponseTime());
    }

    /**
     * Test of getExecutedCounter method, of class TraceTask.
     */
    @Test
    public void testGetExecutedCounter() {
        System.out.println("getExecutedCounter");
        assertEquals(3, t.getExecutedCounter());
    }

    /**
     * Test of getMissedCounter method, of class TraceTask.
     */
    @Test
    public void testGetMissedCounter() {
        System.out.println("getMissedCounter");
        assertEquals(1, t.getMissedCounter());
    }
    
}
