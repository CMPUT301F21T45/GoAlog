package com.example.goalog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddHabitActivity extends AppCompatActivity {

    private Button selectDate;
    private Button confirmButton;
    private EditText habitTitle;
    private EditText habitReason;
    private String habitTitleString;
    private String habitReasonString;
    public static String habitDateString;
    protected static TextView dateDisplay;
    public static boolean editMode = false;

    //Habit myHabit = (Habit) getIntent().getSerializableExtra("selected habit");


    /*Put in main activity
    !SOME!List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
  //clicked on a item: if delete is clicked, remove the obj, if not, open the edit window
     @Override
     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          if(!delete) {
               intent.putExtra("selected habit",!SOME!DataList.get(i)));
                }else{
                       //implement delete here
                }
            }
        });
     */

    @SuppressLint("SimpleDateFormat") public static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);


        dateDisplay = findViewById(R.id.display_start_date_add_habit);
        habitTitle = findViewById(R.id.habit_title);
        habitReason = findViewById(R.id.habit_reason);


        /*
        if(myHabit != null)
        {
            editMode = true;
            habitTitle.setText(myHabit.getHabitTitle());
            habitReason.setText(myHabit.getHabitReason());
        }
        else
        {
            editMode = false;
        }

         */

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
                Date theDate = new Date();
                try {
                    theDate = dateFormat.parse(habitDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(editMode)
                {
                    /*
                    editMode = false;
                    myHabit.setHabitTitle(habitTitleString);
                    myHabit.setHabitReason(habitReasonString);

                    myHabit.setStartDate(theDate);

                     */
                }
                else
                {

                    new Habit(habitDateString,habitTitleString,theDate);
                }
            }
        });


    }
}