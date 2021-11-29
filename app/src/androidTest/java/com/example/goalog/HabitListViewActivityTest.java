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
public class HabitListViewActivityTest {
    private Solo solo;
    private View view;
    @Rule
    public ActivityTestRule<HabitListViewActivity> rule =
            new ActivityTestRule<>(HabitListViewActivity.class, true, true);
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
    public void clickHabitListTest() {
        solo.assertCurrentActivity("Wrong Activity", HabitListViewActivity.class);
        solo.clickOnView(solo.getView(R.id.habit_list));
        solo.sleep(1000);
        Assert.assertTrue(solo.waitForActivity(HabitEventListViewActivity.class));
    }
    @Test
    public void addHabitTest(){
        solo.assertCurrentActivity("Wrong Activity", HabitListViewActivity.class);
        view = solo.getView(R.id.add_habit_button);
        solo.clickOnView(view);
        solo.sleep(1000);
        Assert.assertTrue(solo.waitForActivity(AddHabitActivity.class));

    }

}


