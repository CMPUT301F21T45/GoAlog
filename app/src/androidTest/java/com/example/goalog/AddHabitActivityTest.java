package com.example.goalog;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddHabitActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<AddHabitActivity> rule =
            new ActivityTestRule<>(AddHabitActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void checkButton(){
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.habit_title), "sleep");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "8hr");
        solo.clickOnButton("Confirm");
        Assert.assertTrue(solo.waitForActivity("HabitListViewActivity"));

    }


}
