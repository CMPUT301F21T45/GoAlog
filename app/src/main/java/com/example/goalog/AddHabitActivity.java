package com.example.goalog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);


        dateDisplay = findViewById(R.id.display_start_date_add_habit);
        habitTitle = findViewById(R.id.habit_title);
        habitReason = findViewById(R.id.habit_reason);

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
                if(editMode)
                {
                    editMode = false;
                    //filled in with the object's data
                }
                else
                {
                   new Habit(habitDateString,habitTitleString,habitReasonString);
                }
            }
        });


    }
}