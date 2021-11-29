package com.example.goalog;

import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//this can only be successful after logging in
public class UserPageActivityTest {
    private Solo solo;
    private View view;
    @Rule
    public ActivityTestRule<UserPageActivity> rule =
            new ActivityTestRule<>(UserPageActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void myGoalButtonTest(){
        /**
         * check user page
         */
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        view=solo.getView(R.id.my_goal);
        solo.clickOnView(view);
        Assert.assertTrue(solo.waitForActivity(HabitListViewActivity.class));

    }
    @Test
    public void doneGoalButtonTest(){
        /**
         * check the done button
         */
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        view=solo.getView(R.id.done_goal);
        solo.clickOnView(view);
        Assert.assertTrue(solo.waitForActivity(AddHabitEventActivity.class));

    }

}