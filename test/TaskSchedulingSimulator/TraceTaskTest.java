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
        
        t.addResponseTime(3);
        t.incrementExecutedCounter();
        
        t.addResponseTime(2);
        t.incrementMissedCounter();
        
        t.addResponseTime(13);
        t.incrementExecutedCounter();
        
        t.addResponseTime(3);
        t.incrementExecutedCounter();
    }

    /**
     * Test of addResponseTime method, of class TraceTask.
     */
    @Test
    public void testAddResponseTime() {
        System.out.println("addResponseTime");
        TraceTask instance = new TraceTask();
        
        //test fresh responseTimeFreqTable
        assertEquals(0, instance.getAverageResponseTime(), 0.0001);
        assertEquals(Integer.MAX_VALUE, instance.getMinResponseTime());
        assertEquals(0, instance.getMaxResponseTime());
        assertEquals(0, instance.getMissedCounter());
        assertEquals(0, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addResponseTime(3);
        instance.incrementExecutedCounter();
        
        //test responseTimeFreqTable
        assertEquals(3, instance.getAverageResponseTime(), 0.0001);
        assertEquals(3, instance.getMinResponseTime());
        assertEquals(3, instance.getMaxResponseTime());
        assertEquals(0, instance.getMissedCounter());
        assertEquals(1, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addResponseTime(2);
        instance.incrementMissedCounter();
        
        //test responseTimeFreqTable
        assertEquals(2.5, instance.getAverageResponseTime(), 0.0001);
        assertEquals(2, instance.getMinResponseTime());
        assertEquals(3, instance.getMaxResponseTime());
        assertEquals(1, instance.getMissedCounter());
        assertEquals(1, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addResponseTime(13);
        instance.incrementExecutedCounter();

        //test responseTimeFreqTable
        assertEquals(6, instance.getAverageResponseTime(), 0.0001);
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
        assertEquals(5.25, result, 0.0001);
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
