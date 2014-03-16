package TaskSchedulingSimulator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class FrequencyTableTest {
    
    FrequencyTable ft;
    
    public FrequencyTableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println();
        System.out.println("---START OF FrequencyTest.java---");
        System.out.println();
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println();
        System.out.println("---END OF FrequencyTest.java---");
        System.out.println();
    }
    
    @Before
    public void setUp() {
        ft = new FrequencyTable();
        
        ft.addTime(3);
        ft.addTime(2);
        ft.addTime(13);
        ft.addTime(3);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addTime method, of class FrequencyTable.
     */
    @Test
    public void testAddTime() {
        System.out.print("addTime: ");
        FrequencyTable instance = new FrequencyTable();
        
        //test fresh FrequencyTable
        assertEquals(0, instance.getAverageTime(), 0.0001);
        assertEquals(Integer.MAX_VALUE, instance.getMinimum());
        assertEquals(0, instance.getMaximum());

        
        //add something to FrequencyTable
        instance.addTime(3);
        
        //test FrequencyTable
        assertEquals(3, instance.getAverageTime(), 0.0001);
        assertEquals(3, instance.getMinimum());
        assertEquals(3, instance.getMaximum());
        
        
        //add something to FrequencyTable
        instance.addTime(2);
        
        //test FrequencyTable
        assertEquals(2.5, instance.getAverageTime(), 0.0001);
        assertEquals(2, instance.getMinimum());
        assertEquals(3, instance.getMaximum());
        
        
        //add something to FrequencyTable
        instance.addTime(13);

        //test FrequencyTable
        assertEquals(6, instance.getAverageTime(), 0.0001);
        assertEquals(2, instance.getMinimum());
        assertEquals(13, instance.getMaximum());
        
        System.out.println("OK");
    }

    /**
     * Test of getAverageTime method, of class FrequencyTable.
     */
    @Test
    public void testGetAverageTime() {
        System.out.print("getAverageTime: ");
        

        double result = ft.getAverageTime();
        assertEquals(5.25, result, 0.0001);
        
        System.out.println("OK");
    }

    /**
     * Test of getMinimum method, of class FrequencyTable.
     */
    @Test
    public void testGetMinimum() {
        System.out.print("getMinimum: ");       
        assertEquals(2, ft.getMinimum());
        
        System.out.println("OK");
    }

    /**
     * Test of getMaximum method, of class FrequencyTable.
     */
    @Test
    public void testGetMaximum() {
        System.out.print("getMaximum: ");       
        assertEquals(13, ft.getMaximum());
        
        System.out.println("OK");
    }
    
}
