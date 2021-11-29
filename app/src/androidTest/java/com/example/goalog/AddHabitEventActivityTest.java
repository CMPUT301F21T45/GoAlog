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
public class AddHabitEventActivityTest {
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
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void AddHabitEventTest(){
        //start at user page
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        solo.clickOnButton("Done");
        /**
         * create a new habit event
         * click the map return latitude longitude
         */
        //add a new habit event
        solo.enterText((EditText) solo.getView(R.id.comment_text), "good good good");
        //jump to map
        view=solo.getView(R.id.map_text);
        solo.clickOnView(view);
        solo.sleep(1000);
        view=solo.getView(R.id.OK);
        solo.clickOnView(view);
        solo.sleep(1000);
        //return to add habit event

        solo.clickOnButton("Confirm");
        solo.sleep(1000);
        Assert.assertTrue(solo.waitForActivity("UserPageActivity"));

    }

}
