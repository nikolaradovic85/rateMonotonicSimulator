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
        
        t.responseTimeFT.addTime(3);
        t.jitterFT.addTime(3);
        t.incrementExecutedCounter();
        
        t.responseTimeFT.addTime(2);
        t.jitterFT.addTime(2);
        t.incrementMissedCounter();
        
        t.responseTimeFT.addTime(13);
        t.jitterFT.addTime(13);
        t.incrementExecutedCounter();
        
        t.responseTimeFT.addTime(3);
        t.jitterFT.addTime(3);
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
