package com.example.goalog;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserPageActivityTest {
    private Solo solo;
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
    public void checkAction(){
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        solo.clickOnButton("My Goals");
        Assert.assertTrue(solo.waitForActivity(UserPageActivity.class));

    }
    @Test
    public void checkButton1(){
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        solo.clickOnButton("Done");
        Assert.assertTrue(solo.waitForActivity(UserPageActivity.class));

    }

}
