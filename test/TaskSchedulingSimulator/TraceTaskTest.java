package TaskSchedulingSimulator;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Tests TaskTrace class.
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
        t.addJitter(3);
        t.incrementExecutedCounter();
        
        t.addResponseTime(2);
        t.addJitter(2);
        t.incrementMissedCounter();
        
        t.addResponseTime(13);
        t.addJitter(13);
        t.incrementExecutedCounter();
        
        t.addResponseTime(3);
        t.addJitter(3);
        t.incrementExecutedCounter();
    }
    
    @BeforeClass
    public static void before() {
        System.out.println();
        System.out.println("---START OF TraceTaskTest.java---");
        System.out.println();
    }
    
    @AfterClass
    public static void after() {
        System.out.println();
        System.out.println("---END OF TraceTaskTest.java---");
        System.out.println();
    }

    /**
     * Test of addResponseTime and addJitter method, of class TraceTask.
     */
    @Test
    public void testAddX() {
        System.out.print("addResponseTime & addJitter: ");
        TraceTask instance = new TraceTask();
        
        //test fresh responseTimeFreqTable
        assertEquals(0, instance.getAverageResponseTime(), 0.0001);
        assertEquals(Integer.MAX_VALUE, instance.getMinResponseTime());
        assertEquals(0, instance.getMaxResponseTime());
        
        assertEquals(0, instance.getAverageJitter(), 0.0001);
        assertEquals(Integer.MAX_VALUE, instance.getMinJitter());
        assertEquals(0, instance.getMaxJitter());
        
        assertEquals(0, instance.getMissedCounter());
        assertEquals(0, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addResponseTime(3);
        instance.addJitter(3);
        instance.incrementExecutedCounter();
        
        //test responseTimeFreqTable
        assertEquals(3, instance.getAverageResponseTime(), 0.0001);
        assertEquals(3, instance.getMinResponseTime());
        assertEquals(3, instance.getMaxResponseTime());
        
        assertEquals(3, instance.getAverageJitter(), 0.0001);
        assertEquals(3, instance.getMinJitter());
        assertEquals(3, instance.getMaxJitter());
        
        assertEquals(0, instance.getMissedCounter());
        assertEquals(1, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addResponseTime(2);
        instance.addJitter(2);
        instance.incrementMissedCounter();
        
        //test responseTimeFreqTable
        assertEquals(2.5, instance.getAverageResponseTime(), 0.0001);
        assertEquals(2, instance.getMinResponseTime());
        assertEquals(3, instance.getMaxResponseTime());
        
        assertEquals(2.5, instance.getAverageJitter(), 0.0001);
        assertEquals(2, instance.getMinJitter());
        assertEquals(3, instance.getMaxJitter());
        
        assertEquals(1, instance.getMissedCounter());
        assertEquals(1, instance.getExecutedCounter());
        
        //add something to responseTimeFreqTable
        instance.addResponseTime(13);
        instance.addJitter(13);
        instance.incrementExecutedCounter();

        //test responseTimeFreqTable
        assertEquals(6, instance.getAverageResponseTime(), 0.0001);
        assertEquals(2, instance.getMinResponseTime());
        assertEquals(13, instance.getMaxResponseTime());
        
        assertEquals(6, instance.getAverageJitter(), 0.0001);
        assertEquals(2, instance.getMinJitter());
        assertEquals(13, instance.getMaxJitter());
        
        assertEquals(1, instance.getMissedCounter());
        assertEquals(2, instance.getExecutedCounter());
        
        System.out.println("OK");
    }

    /**
     * Test of getDeadlineMissProbability method, of class TraceTask.
     */
    @Test
    public void testGetDeadlineMissProbability() {
        System.out.print("getDeadlineMissProbability: ");
        
        double result = t.getDeadlineMissProbability();
        assertEquals(0.25, result, 0.0001);
        
        System.out.println("OK");
    }

    /**
     * Test of getAverageResponseTime and getAverageJitter methods.
     */
    @Test
    public void testGetAverageX() {
        System.out.print("getAverageResponseTime & getAverageJitter: ");
        

        double resultResponseTime = t.getAverageResponseTime();
        assertEquals(5.25, resultResponseTime, 0.0001);
        
        double resultJitter = t.getAverageResponseTime();
        assertEquals(5.25, resultJitter, 0.0001);
        
        System.out.println("OK");
    }

    /**
     * Test of incrementExecutedCounter method, of class TraceTask.
     */
    @Test
    public void testIncrementExecutedCounter() {
        System.out.print("incrementExecutedCounter: ");
        TraceTask instance = new TraceTask();        
        instance.incrementExecutedCounter();
        assertEquals(1, instance.getExecutedCounter());
        
        System.out.println("OK");
    }

    /**
     * Test of incrementMissedCounter method, of class TraceTask.
     */
    @Test
    public void testIncrementMissedCounter() {
        System.out.print("incrementMissedCounter: ");
        TraceTask instance = new TraceTask();
        instance.incrementMissedCounter();
        assertEquals(1, instance.getMissedCounter());
        
        System.out.println("OK");
    }

    /**
     * Test of getMinResponseTime and getMinJitter methods, of class TraceTask.
     */
    @Test
    public void testGetMinX() {
        System.out.print("getMinResponseTime & getMinJitter: ");       
        assertEquals(2, t.getMinResponseTime());
        assertEquals(2, t.getMinJitter());
        
        System.out.println("OK");
    }

    /**
     * Test of getMaxResponseTime and getMaxJitter methods, of class TraceTask.
     */
    @Test
    public void testGetMaxX() {
        System.out.print("getMaxResponseTime & getMaxJitter: ");
        assertEquals(13, t.getMaxResponseTime());
        assertEquals(13, t.getMaxJitter());
        
        System.out.println("OK");
    }

    /**
     * Test of getExecutedCounter method, of class TraceTask.
     */
    @Test
    public void testGetExecutedCounter() {
        System.out.print("getExecutedCounter: ");
        assertEquals(3, t.getExecutedCounter());
        
        System.out.println("OK");
    }

    /**
     * Test of getMissedCounter method, of class TraceTask.
     */
    @Test
    public void testGetMissedCounter() {
        System.out.print("getMissedCounter: ");
        assertEquals(1, t.getMissedCounter());
        
        System.out.println("OK");
    }
    
}
