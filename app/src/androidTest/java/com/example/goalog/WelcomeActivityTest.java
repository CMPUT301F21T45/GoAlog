package com.example.goalog;
//this can only be successful before logging in
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class WelcomeActivityTest {
    private Solo solo;
    private View view;
    @Rule
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void loginTest(){
        /**
         * check the sign in page
         * register
         */
        solo.assertCurrentActivity("Wrong Activity", WelcomeActivity.class);
        view=solo.getView(R.id.login_button);
        solo.clickOnView(view);
        solo.enterText((EditText) solo.getView(R.id.email), "1@23.com");
        view=solo.getView(R.id.button_next);
        solo.clickOnView(view);
        solo.enterText((EditText) solo.getView(R.id.password), "123456");
        solo.clickOnButton("Sign in");
        solo.assertCurrentActivity("Wrong Activity", MainPagesActivity.class);

    }




}
