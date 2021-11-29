package com.example.goalog;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *  Unit Test to Test Event Class to ensure that the event comment constraints and setters are
 *  working properly
 */

public class EventTest {

   HabitEvent h1 = new HabitEvent("Event ID", "Event Comment",
           "date completed", "Good Title", "Picture Name", null);


   @Test
    public void testEventComment(){
        String commentTest = "this is way too long a comment for an event zxcawejkdbnkjbcwsjhadsd";

        /*try-catch method to catch the exception when testing a event comment that exceeds the
        maximum character limit*/
        try {
            h1.setEventComment(commentTest);

        } catch (IllegalArgumentException e){
            assertTrue(Boolean.TRUE);
            return;
        }

            assertTrue(Boolean.FALSE);
    }



}
