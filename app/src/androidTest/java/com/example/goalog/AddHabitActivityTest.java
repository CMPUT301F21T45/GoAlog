package com.example.goalog;

import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
//this can only be successful after logging in
public class AddHabitActivityTest {
    private Solo solo;
    private View view;
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
    public void AddHabitTest(){
        //start ay main page
        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
        view=solo.getView(R.id.add_goal_text_view);
        solo.clickOnView(view);
        solo.sleep(1000);
        //jump to add habit
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        /**
         * create a new habit
         */
        //add a new habit
        solo.enterText((EditText) solo.getView(R.id.habit_title), "sleep");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "8hr");
        view=solo.getView(R.id.sun_checkbox);
        solo.clickOnView(view);
        solo.sleep(1000);
        view=solo.getView(R.id.checkbox_privacy_add);
        solo.clickOnView(view);
        solo.sleep(1000);
        //select the attribute of habit
        solo.clickOnButton("Confirm");
        Assert.assertTrue(solo.waitForActivity("HabitListViewActivity"));

    }


}
