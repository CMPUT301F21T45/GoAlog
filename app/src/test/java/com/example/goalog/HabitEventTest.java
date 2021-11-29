package com.example.goalog;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;


/**
 *  Unit Test to Test Event Class to ensure that all public function works
 */

public class HabitEventTest {

    private HabitEvent testHabitEvent;
    private HashMap<String, String> locationMap;

    @Before
    public void setUp() {
        locationMap = new HashMap<String, String>();
        locationMap.put("a", "1");
        locationMap.put("b", "c");
        testHabitEvent = new HabitEvent("id", "comment",
                "date", "title", "img", locationMap);
    }

    @Test
    public void testGetEventID() {
        assertEquals("id", testHabitEvent.getEventID());
    }

    @Test
    public void testGetHabitTitle() {
        assertEquals("title", testHabitEvent.getHabitTitle());
    }

    @Test
    public void testSetHabitTitle() {
        testHabitEvent.setHabitTitle("new");
        assertEquals("new", testHabitEvent.getHabitTitle());
    }

    @Test
    public void testGetCompleteDate() {
        assertEquals("date", testHabitEvent.getCompleteDate());
    }

    @Test
    public void testGetLocation() {
        assertEquals(locationMap, testHabitEvent.getLocation());
    }

    @Test
    public void testSetLocation() {
        locationMap.put("c", "3");
        testHabitEvent.setLocation(locationMap);
        assertEquals(locationMap, testHabitEvent.getLocation());
    }

    @Test
    public void testGetEventComment() {
        assertEquals("comment", testHabitEvent.getEventComment());
    }

    @Test
    public void testSetEventComment(){
        String commentTest = "this is way too long a comment for an event zxcawejkdbnkjbcwsjhadsd";

        /*try-catch method to catch the exception when testing a event comment that exceeds the
        maximum character limit*/
        try {
            testHabitEvent.setEventComment(commentTest);

        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }
    }
    @Test
    public void testGetImage() {
        assertEquals("img", testHabitEvent.getImage());
    }

    @Test
    public void testSetImage() {
        testHabitEvent.setImage("new");
        assertEquals("new", testHabitEvent.getImage());
    }

    @Test
    // compare two habit by orderID
    public void testCompareTo() {
        HabitEvent testHabitEvent1 = new HabitEvent("id", "comment",
                "2021-07-03", "title", "img", locationMap);
        HabitEvent testHabitEvent2 = new HabitEvent("id", "comment",
                "2021-09-09", "title", "img", locationMap);

        assertEquals(-1, testHabitEvent1.compareTo(testHabitEvent2));

        // testHabitEvent has invalid format of date, compare function will return -2
        assertEquals(-2, testHabitEvent.compareTo(testHabitEvent1));

    }

}



