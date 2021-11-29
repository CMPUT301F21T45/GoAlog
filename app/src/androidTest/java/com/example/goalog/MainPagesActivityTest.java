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


    @Test
    public void AddReminderTest(){
        /**
         * check main page action
         * add reminder
         */
//        solo.assertCurrentActivity("Wrong Activity", WelcomeActivity.class);
//        view=solo.getView(R.id.login_button);
//        solo.clickOnView(view);
//        solo.enterText((EditText) solo.getView(R.id.email), "1@23.com");
//        view=solo.getView(R.id.button_next);
//        solo.clickOnView(view);
//        solo.enterText((EditText) solo.getView(R.id.password), "123456");
//        solo.clickOnButton("Sign in");
//        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
        //start at main page
        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
        view=solo.getView(R.id.add_reminder_button);
        solo.clickOnView(view);
        solo.sleep(2000);
        //cancel add
        view=solo.getView(R.id.cancel_reminder_button);
        solo.clickOnView(view);
        solo.sleep(2000);
        view=solo.getView(R.id.add_reminder_button);
        solo.clickOnView(view);
        solo.sleep(2000);
        //add reminder
        solo.enterText((EditText) solo.getView(R.id.reminder_edit_text), "no");
        solo.sleep(2000);
        view=solo.getView(R.id.confirm_reminder_button);
        solo.clickOnView(view);
        solo.sleep(2000);
        view=solo.getView(R.id.add_goal_text_view);
        solo.clickOnView(view);
        solo.sleep(2000);
        Assert.assertTrue(solo.waitForActivity(AddHabitActivity.class));
    }
//    @Test
//    public void SignOutButtonTest(){
//        /**
//         * check sign out
//         */
//        solo.assertCurrentActivity("Wrong Activity", WelcomeActivity.class);
//        view=solo.getView(R.id.login_button);
//        solo.clickOnView(view);
//        solo.enterText((EditText) solo.getView(R.id.email), "1@23.com");
//        view=solo.getView(R.id.button_next);
//        solo.clickOnView(view);
//        solo.enterText((EditText) solo.getView(R.id.password), "123456");
//        solo.clickOnButton("Sign in");
//        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
//        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);
//        view=solo.getView(R.id.sign_out_button);
//        solo.clickLongOnView(view);
//        solo.clickOnButton("Sign Out");
//        Assert.assertTrue(solo.waitForActivity(WelcomeActivity.class));
//
//    }




}
