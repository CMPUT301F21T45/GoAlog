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
public class RequestActivityTest {
    private Solo solo;
    private View view;
    @Rule
    public ActivityTestRule<RequestActivity> rule =
            new ActivityTestRule<>(RequestActivity.class, true, true);
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
    public void AddRequestTest(){
        /**
         * check the request page
         * add request
         */
        solo.assertCurrentActivity("Wrong Activity", RequestActivity.class);
        view=solo.getView(R.id.call_to_make_request);
        solo.clickOnView(view);
        //cancel request
        view=solo.getView(R.id.cancel_request_button);
        solo.clickOnView(view);
        view=solo.getView(R.id.call_to_make_request);
        solo.clickOnView(view);
        //add request
        solo.enterText((EditText) solo.getView(R.id.send_request_email), "2@34.com");
        solo.enterText((EditText) solo.getView(R.id.send_request_reason), "GOOD");
        view=solo.getView(R.id.send_request_button);
        solo.clickOnView(view);
        Assert.assertTrue(solo.waitForActivity(RequestActivity.class));
    }
}
