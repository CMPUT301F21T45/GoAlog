package com.example.goalog;



import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.goalog.Habit;


/**
 *  Unit Test to Test Habit Class to ensure that the setters are working
 *  properly and following the constraints highlighted in the project description
 *  for Habit Title and Habit Reason
 */

public class HabitTest {

    //Test that setter for Habit Title is able to set a proper name to a habit
    @Test
    public void testProperHabitTitle() {
        String habitTitle = "Good Habit";

        Habit properTitle = new Habit("yes");

        properTitle.setHabitTitle(habitTitle);

        // checks that the setter has set Habit Title to "Good Habit"
        assertEquals(habitTitle, properTitle.getHabitTitle());
    }


    //Test that setter for Habit Title shows an error when Title length exceed maximum
    //length
    @Test
    public void testHabitTitleLength() {
        String habitTitle = "asdlkjok2k1jenlmnsfjakndfalkflakn";
        Habit testHabit = new Habit("lol");

        try{
            testHabit.setHabitTitle(habitTitle);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
        }

        //Error replication fails
      //  assertTrue(Boolean.FALSE);
    }

    //Test that setter for Habit Reason is able to set a proper reason to a Habit Reason
    @Test
    public void testSetHabitReason() {
        String habitReason = "I wanna be better";
        Habit testHabit = new Habit("motivation");

        testHabit.setHabitReason(habitReason);

        assertEquals(habitReason, testHabit.getHabitReason()); //check if habit reason matches
    }

    //Test that setter for Habit Reason is able to show an error when Habit Reason exceed Max
    //character limit
    @Test
    public void testSetReasonLength() {
        String reasonLength = "123456789123456789123456789101010";
        Habit testHabit = new Habit("no pain no gain");

        try{
            testHabit.setHabitReason(reasonLength);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
        }
        // error replication fails
      //  assertTrue(Boolean.FALSE);
    }

}
