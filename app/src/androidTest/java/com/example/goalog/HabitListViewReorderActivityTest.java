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
public class HabitListViewReorderActivityTest {
        private Solo solo;
        private View view;
        @Rule
        public ActivityTestRule<HabitListViewReorderActivity> rule =
                new ActivityTestRule<>(HabitListViewReorderActivity.class, true, true);
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
         *
         */
        @Test
        public void checkReorderButton() {
            /**
             * check the reorder button
             */
            solo.assertCurrentActivity("Wrong Activity", HabitListViewReorderActivity.class);
            view=solo.getView(R.id.reorder_hadler);
            solo.clickOnView(view);
            solo.sleep(1000);
            Assert.assertTrue(solo.waitForActivity(HabitListViewReorderActivity.class));
        }
        @Test
        public void checkReorderAction() {
            /**
             * check the reorder function work
             */
            solo.assertCurrentActivity("Wrong Activity", HabitListViewReorderActivity.class);
            view=solo.getView(R.id.reorder_done_button);
            solo.clickOnView(view);
            solo.sleep(1000);
            Assert.assertTrue(solo.waitForActivity(HabitListViewActivity.class));
        }


    }

