package com.example.goalog;

import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;
import android.view.View;
import android.widget.EditText;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
//this can only be successful after logging in
public class AddHabitAcrossActivityTest {
    private Solo solo;
    private View view;
    @Rule
    public ActivityTestRule<UserPageActivity> rule =
            new ActivityTestRule<>(UserPageActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void AddHabitTest() {

        //start in user Activity
        solo.assertCurrentActivity("Wrong Activity", UserPageActivity.class);
        solo.clickOnButton("View All Goals");
        solo.sleep(1000);

        //jump to HabitListView
        solo.assertCurrentActivity("Wrong Activity",HabitListViewActivity.class);
        view = solo.getView(R.id.add_habit_button);
        solo.clickOnView(view);
        solo.sleep(1000);

        //jump to AddHabitView
        /**
         * create a new habit
         * title = Test Habit
         * reason = Testing reason
         * checkbox fri
         */
        solo.assertCurrentActivity("Wrong Activity",AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.habit_title),"Test Habit");
        solo.enterText((EditText) solo.getView(R.id.habit_reason),"Testing reason");
        view = solo.getView(R.id.fri_checkbox);
        solo.clickOnView(view);
        solo.sleep(1000);
        solo.clickOnButton("Confirm");

        //jump to HabitListView
        solo.assertCurrentActivity("Wrong Activity",HabitListViewActivity.class);
        view = solo.getView(R.id.habit_title);
        solo.sleep(1000);
        assertTrue(solo.searchText("Test Habit"));

    }
}
