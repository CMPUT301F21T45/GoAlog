package com.example.goalog;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitListViewReorderActivityTest {
        private Solo solo;
        private View view;
        @Rule
        public ActivityTestRule<com.example.goalog.HabitListViewReorderActivity> rule =
                new ActivityTestRule<>(com.example.goalog.HabitListViewReorderActivity.class, true, true);
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
        public void checkImageview() {
            /**
             * check the reorder button
             */
            solo.assertCurrentActivity("Wrong Activity", HabitListViewReorderActivity.class);
            view=solo.getView(R.id.reorder_hadler);
            solo.clickOnView(view);
            Assert.assertTrue(solo.waitForActivity(HabitListViewReorderActivity.class));
        }
        @Test
        public void checkAction() {
            /**
             * check the reorder function work
             */
            solo.assertCurrentActivity("Wrong Activity", HabitListViewReorderActivity.class);
            view=solo.getView(R.id.reorder_done_button);
            solo.clickOnView(view);
            Assert.assertTrue(solo.waitForActivity(HabitListViewActivity.class));
        }


    }

