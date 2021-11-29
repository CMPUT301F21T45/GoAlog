package com.example.goalog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * an Activity page with fields to show attributes of an Habit object and makes update to firestore
 * Used in two cases:
 *  1. the add Habit page, with all fields blank and needed to be filled by user
 *  2. the Habit detail page, with all fields of a given Habit and allows user to edit
 *
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class AddHabitActivity extends AppCompatActivity{

    // all fields
    private Button selectDate;
    private Button confirmButton;
    private EditText habitTitle;
    private EditText habitReason;
    private CheckBox mon, tue, wed, thu, fri, sat, sun, privacy;
    private String habitTitleString;
    private String habitReasonString;
    private TextView activityTitle;
    private boolean habitPrivacy = false;
    public static String habitDateString;
    protected static TextView dateDisplay;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // check whether the Activity is used for add or edit a Habit
    public static boolean editMode = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // get related views
        activityTitle = findViewById(R.id.activity_add_habit_title);
        dateDisplay = findViewById(R.id.display_start_date_add_habit);
        habitTitle = findViewById(R.id.habit_title);
        habitReason = findViewById(R.id.habit_reason);
        mon = findViewById(R.id.mon_checkbox);
        tue = findViewById(R.id.tue_checkbox);
        wed = findViewById(R.id.wed_checkbox);
        thu = findViewById(R.id.thu_checkbox);
        fri = findViewById(R.id.fri_checkbox);
        sat = findViewById(R.id.sat_checkbox);
        sun = findViewById(R.id.sun_checkbox);
        privacy = findViewById(R.id.checkbox_privacy_add);

        Habit myHabit = (Habit) getIntent().getSerializableExtra("Selected Habit");
        //if there's a selected Habit, this is an edit Habit page
        if(myHabit != null) {
            //edit mode
            activityTitle.setText("EDIT GOAL");
            editMode = true;

            // filled in Habit details
            habitTitle.setText(myHabit.getHabitTitle());
            habitReason.setText(myHabit.getHabitReason());
            dateDisplay.setText(myHabit.getStartDate());
            String weekPlan = myHabit.getWeekdayPlan();

            if (weekPlan.contains("1")){mon.setChecked(true);}
            if (weekPlan.contains("2")){tue.setChecked(true);}
            if (weekPlan.contains("3")){wed.setChecked(true);}
            if (weekPlan.contains("4")){thu.setChecked(true);}
            if (weekPlan.contains("5")){fri.setChecked(true);}
            if (weekPlan.contains("6")){sat.setChecked(true);}
            if (weekPlan.contains("7")){sun.setChecked(true);}
            if (myHabit.isPublic()){privacy.setChecked(true);}
        }
        //if there's no selected Habit, this is an add Habit page
        else
        {   //add mode
            LocalDateTime now = LocalDateTime.now();
            dateDisplay.setText(dtf.format(now));
            editMode = false;
        }

        // for date selection
        selectDate = (Button) findViewById(R.id.select_date_add_habit);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        confirmButton = (Button) findViewById(R.id.confirm_button_habit);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // confirm is clicked
                habitTitleString = habitTitle.getText().toString();
                habitReasonString = habitReason.getText().toString();
                habitDateString = dateDisplay.getText().toString();

                // input constrains
                if(habitTitleString.length()> 20) {
                    habitTitleString = habitTitleString.substring(0,20);
                }
                if (habitReasonString.length()>30) {
                    habitReasonString = habitReasonString.substring(0,30);
                }

                StringBuilder checked = new StringBuilder("");
                int dayIndex = 1;
                boolean isScheduled = false;
                CheckBox week[] = {mon, tue, wed, thu, fri, sat, sun};

                for (CheckBox box:week) {
                    if (box.isChecked()) {
                        checked.append(dayIndex);
                        isScheduled = true;
                    }
                    dayIndex++;
                }

                if (privacy.isChecked()){habitPrivacy = true;}

                // In edit mode
                if(editMode) {
                    // if inputs fits all constraints, update with new information
                    if (habitTitleString.length()>0 && habitReason.length()>0 && isScheduled) {
                        editMode = false;
                        myHabit.setHabitTitle(habitTitleString);
                        myHabit.setHabitReason(habitReasonString);
                        myHabit.setStartDate(habitDateString);
                        myHabit.setWeekdayPlan(checked.toString());
                        myHabit.setPublic(habitPrivacy);

                        // back to last page with the updated habit information
                        Intent intent = new Intent(AddHabitActivity.this, HabitListViewActivity.class);
                        intent.putExtra("Updated Habit", myHabit);
                        Toast.makeText(v.getContext(), "Successfully Edited!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                    // if inputs don't fit the constraints, nothing happens
                    else {
                        Toast.makeText(v.getContext(),
                                "Please enter a title & reason, and schedule for your habit!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //in add mode
                else {
                    // if inputs fits all constraints, update with new information
                    if (habitTitleString.length()>0 && habitReason.length()>0 && isScheduled) {
                        // generate an ID for new Habit
                        final String habitID = UUID.randomUUID().toString().replace("-", "");
                        Habit newHabit = new Habit(habitTitleString, habitReasonString, habitDateString, checked.toString(), habitPrivacy, habitID, new Long(-1), "none");

                        // back to last page with the new habit information
                        Intent intent = new Intent(AddHabitActivity.this, HabitListViewActivity.class);
                        intent.putExtra("New Habit", newHabit);
                        Toast.makeText(v.getContext(), "The habit has been added to your list!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                    // if inputs don't fit the constraints, nothing happens
                    else {
                        Toast.makeText(v.getContext(),
                                "Please enter a title & reason, and schedule for your habit!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}