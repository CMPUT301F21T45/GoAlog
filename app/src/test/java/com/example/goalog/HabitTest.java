package com.example.goalog;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.lang.Boolean.FALSE;
import org.junit.jupiter.api.Test;


/**
 *  Unit Test to Test Habit Class to ensure that the setters are working
 *  properly and following the constraints highlighted in the project description
 *  for Habit Title and Habit Reason
 */

public class HabitTest {



    //Test that setter for Habit Title is able to set a proper name to a habit
    @Test
    public void testProperHabitTitle() {

        Habit testingHabit = new Habit("Doer", null,null,
                null,FALSE,null);

        String testTitle = "Good Habit Title";
        //setting habit title to testTitle using the Setter method
        testingHabit.setHabitTitle(testTitle);

        // checks that the setter has set Habit Title to "Good Habit"
        assertEquals(testingHabit.getHabitTitle(),testTitle);

    }


    //Test that setter for Habit Title cuts downs to 20 characters when title is >20 characters
    @Test
    public void testHabitTitleLength() {
        Habit testingHabit = new Habit("Nice title", null,null,
                null,FALSE,null);

        String testTitle = "This Name is too large and should be cut down to 20 characters";

        //setting habit title to testTitle using the Setter method
        testingHabit.setHabitTitle(testTitle);

        //checking that the setter has set Habit Title to the first 20 characters by default
        assertEquals(testingHabit.getHabitTitle(),
                testTitle.substring(0,20));
    }

    //Test that setter for Habit Reason is able to set a proper reason to a Habit Reason
    @Test
    public void testSetHabitReason() {
        Habit testingHabit = new Habit(null, "this isn't a reason",null,
                null,FALSE,null);


        String habitReason = "I wanna be better";

        //setting habit reason to habitReason using the Setter method
        testingHabit.setHabitReason(habitReason);

        assertEquals(habitReason, testingHabit.getHabitReason()); //check if habit reason matches
    }

    //Test that setter for Habit Reason is able to cut downs to 30 characters when reason
    // is >30 characters
    @Test
    public void testSetReasonLength() {

        Habit testingHabit = new Habit(null, "this isn't a reason",null,
                null,FALSE,null);

        String reasonLength = "This is way too long to be a Habit Reason, and should be cut" +
                "down to 30 characters or less";

        testingHabit.setHabitReason(reasonLength);
        assertEquals(testingHabit.getHabitReason(),
                reasonLength.substring(0,30));

    }

}
