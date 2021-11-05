package com.example.goalog;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddHabitEventActivityTest {
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
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void checkButton1(){
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        solo.clickOnButton("Done");
        solo.enterText((EditText) solo.getView(R.id.comment_text), "good good good");
        solo.clickOnButton("Confirm");
        Assert.assertTrue(solo.waitForActivity("UserPageActivity"));

    }

}
