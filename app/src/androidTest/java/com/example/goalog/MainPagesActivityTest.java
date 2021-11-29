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

public class MainPagesActivityTest {
    private Solo solo;
    private View view;
    @Rule
    public ActivityTestRule<MainPagesActivity> rule =
            new ActivityTestRule<>(MainPagesActivity.class, true, true);
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
        /**
         * check main page action
         * add reminder
         */
        //start at main page
        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
        view=solo.getView(R.id.add_reminder_button);
        solo.clickOnView(view);
        //cancel add
        view=solo.getView(R.id.cancel_reminder_button);
        solo.clickOnView(view);
        view=solo.getView(R.id.add_reminder_button);
        solo.clickOnView(view);
        //add reminder
        solo.enterText((EditText) solo.getView(R.id.reminder_edit_text), "yeees");
        view=solo.getView(R.id.confirm_reminder_button);
        solo.clickOnView(view);
        view=solo.getView(R.id.add_goal_text_view);
        solo.clickOnView(view);
        Assert.assertTrue(solo.waitForActivity(AddHabitActivity.class));
    }
    @Test
    public void checkSignOut(){
        /**
         * check sign out
         */
        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
        view=solo.getView(R.id.sign_out_button);
        solo.clickLongOnView(view);
        solo.clickOnButton("Sign Out");
        Assert.assertTrue(solo.waitForActivity(WelcomeActivity.class));

    }



}
