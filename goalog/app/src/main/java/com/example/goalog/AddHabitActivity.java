package com.example.goalog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;


public class AddHabitActivity extends AppCompatActivity{

    //AddHabitActivityConfirmListener AL;
    private Button selectDate;
    private Button confirmButton;
    private EditText habitTitle;
    private EditText habitReason;
    private CheckBox mon, tue, wed, thu, fri, sat, sun, privacy;
    private String habitTitleString;
    private String habitReasonString;
    private boolean habitPrivacy = false;
    public static String habitDateString;
    protected static TextView dateDisplay;
    public static boolean editMode = false;

    //Habit myHabit = (Habit) getIntent().getSerializableExtra("selected habit");


    @SuppressLint("SimpleDateFormat") public static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

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

//        if(myHabit != null)
//        {
//            editMode = true;
//            habitTitle.setText(myHabit.getHabitTitle());
//            habitReason.setText(myHabit.getHabitReason());
//        }
//        else
//        {
//            editMode = false;
//        }

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
                // Create a new habit
                // Upload it into firebase.
                habitTitleString = habitTitle.getText().toString();
                habitReasonString = habitReason.getText().toString();
                habitDateString = dateDisplay.getText().toString();

                StringBuilder checked = new StringBuilder();
                int dayIndex = 1;
                CheckBox week[] = {mon, tue, wed, thu, fri, sat, sun};

                for (CheckBox box:week) {
                    if (box.isChecked()) {
                        checked.append(dayIndex);
                    }
                    dayIndex++;
                }

                if (privacy.isChecked()) {
                    habitPrivacy = true;
                }

                if(editMode)
                {
//                    editMode = false;
//                    myHabit.setHabitTitle(habitTitleString);
//                    myHabit.setHabitReason(habitReasonString);
//                    myHabit.setStartDate(theDate);
                }
                else
                {
                    //AL.onConfirmPressed(new Habit(habitDateString,habitTitleString,theDate));
                    //finish();
                    Habit newHabit = new Habit(habitTitleString,habitReasonString,habitDateString, checked.toString(), habitPrivacy);
                    Intent intent = new Intent(AddHabitActivity.this, HabitListViewActivity.class);
                    intent.putExtra("New Habit", newHabit);
                    startActivity(intent);
                }
            }
        });


    }
}
