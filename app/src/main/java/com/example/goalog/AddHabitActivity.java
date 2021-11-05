package com.example.goalog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddHabitActivity extends AppCompatActivity{
    /**AddHabitActivity:
     * 1.Receive selected Habit from intent,
     * if Habit != null, edit mode,other wise create a new habit
     * 2. In add mode, check length of title and reason,
     * create a new habit, send the new Habit to HabitListViewActivity to upload it to firebase
     * 3. In edit mode, filled in the editTexts with habit details
     * send the updated Habit to HabitListViewActivity to upload it to firebase
     * 4. In edit mode, input constraints is deal in Habit Title/reason setters
     */

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
    public static boolean editMode = false;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        //set up parameters
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
        //if Selected Habit is not null, we jump here from a selected Habit ---> edit mode
        if(myHabit != null)
        {   //edit mode
            activityTitle.setText("EDIT HABIT");
            editMode = true;

            //filled in with Habit unedited details
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
        else
        {   //add mode
            LocalDateTime now = LocalDateTime.now();
            dateDisplay.setText(dtf.format(now));
            editMode = false;
        }

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
                //input constrain
                if(habitTitleString.length()> 20) {
                    habitTitleString = habitTitleString.substring(0,30);
                }
                if (habitReasonString.length()>30) {
                    habitReasonString = habitReasonString.substring(0,30);
                }

                StringBuilder checked = new StringBuilder();
                int dayIndex = 1;
                CheckBox week[] = {mon, tue, wed, thu, fri, sat, sun};

                for (CheckBox box:week) {
                    if (box.isChecked()) {
                        checked.append(dayIndex);
                    }
                    dayIndex++;
                }

                if (privacy.isChecked()){habitPrivacy = true;}

                if(editMode)
                {   //In edit mode
                    //update with new information
                    editMode = false;
                    myHabit.setHabitTitle(habitTitleString);
                    myHabit.setHabitReason(habitReasonString);
                    myHabit.setStartDate(habitDateString);
                    myHabit.setWeekdayPlan(checked.toString());
                    myHabit.setPublic(habitPrivacy);

                    //send the Habit back to AddHabit
                    Intent intent = new Intent(AddHabitActivity.this, HabitListViewActivity.class);
                    intent.putExtra("Updated Habit", myHabit);
                    startActivity(intent);
                }
                else
                {   //in add mode
                    final String habitID = UUID.randomUUID().toString().replace("-", "");
                    Habit newHabit = new Habit(habitTitleString, habitReasonString, habitDateString, checked.toString(), habitPrivacy, habitID);
                    Intent intent = new Intent(AddHabitActivity.this, HabitListViewActivity.class);
                    intent.putExtra("New Habit", newHabit);
                    startActivity(intent);
                }
            }
        });


    }
}