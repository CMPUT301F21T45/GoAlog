package com.example.goalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static java.lang.Boolean.FALSE;



/**
 *  Unit Test to Test Habit Class to ensure that all public function works
 */

public class HabitTest {

    private Habit testHabit;

    @Before
    public void setUp() {
       testHabit = new Habit("title", "reason","startdate",
                "weekdayplan",FALSE,"id", new Long(1),"latestdate");
    }

    @Test
    public void testGetHabitTitle() {
        assertEquals("title", testHabit.getHabitTitle());
    }

    @Test
    public void testSetHabitTitle() {
        // less than or equal to 20 char
        testHabit.setHabitTitle("123");
        assertEquals("123", testHabit.getHabitTitle());
        // greater than 20 char
        testHabit.setHabitTitle("12345678901234567890123");
        assertEquals("12345678901234567890", testHabit.getHabitTitle());
    }

    @Test
    public void testGetHabitReason() {
        assertEquals("reason", testHabit.getHabitReason());
    }

    @Test
    public void testSetHabitReason() {
        // less than or equal to 20 char
        testHabit.setHabitReason("123");
        assertEquals("123", testHabit.getHabitReason());
        // greater than 20 char
        testHabit.setHabitReason("1234567890123456789012345678901234567890");
        assertEquals("123456789012345678901234567890", testHabit.getHabitReason());
    }

    @Test
    public void testGetStartDate() {
        assertEquals("startdate", testHabit.getStartDate());
    }

    @Test
    public void testSetStartDate() {
        testHabit.setStartDate("new");
        assertEquals("new", testHabit.getStartDate());
    }

    @Test
    public void testGetWeekdatPlan() {
        assertEquals("weekdayplan", testHabit.getWeekdayPlan());
    }

    @Test
    public void testSetWeekdayPlan() {
        testHabit.setWeekdayPlan("new");
        assertEquals("new", testHabit.getWeekdayPlan());
    }

    @Test
    public void testIsPublic() {
        assertFalse(testHabit.isPublic());
    }

    @Test
    public void testSetPublic() {
        testHabit.setPublic(Boolean.TRUE);
        assertTrue(testHabit.isPublic());
    }

    @Test
    public void testGetHabitID() {
        assertEquals("id", testHabit.getHabitID());
    }

    @Test
    public void testGetOrderID() {
        assertEquals(new Long(1), testHabit.getOrderID());
    }

    @Test
    public void testSetOrderID() {
        testHabit.setOrderID(new Long(2));
        assertEquals(new Long(2), testHabit.getOrderID());

    }

    @Test
    public void testGetLatestFinishDate() {
        assertEquals("latestdate", testHabit.getLatestFinishDate());
    }

    @Test
    public void testSetLatestFinishDate() {
        testHabit.setLatestFinishDate("new");
        assertEquals("new", testHabit.getLatestFinishDate());
    }

    @Test
    // compare two habit by orderID
    public void testCompareTo() {
        Habit testHabit1 = new Habit("title", "reason","startdate",
                "weekdayplan",FALSE,"id", new Long(1),"latestdate");
        Habit testHabit2 = new Habit("title", "reason","startdate",
                "weekdayplan",FALSE,"id", new Long(2),"latestdate");

        assertEquals(-1, testHabit1.compareTo(testHabit2));

    }


}
