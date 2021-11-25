package com.example.goalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainPagesActivity extends AppCompatActivity {

    private final ArrayList<String> reminders = new ArrayList<>();
    private ArrayAdapter<?> reminderAdapter;
    ListView reminderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar todayCal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("MMMM dd");

        setContentView(R.layout.activity_main_pages);

        // Set name
        TextView userName = findViewById(R.id.user_name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String name = user.getDisplayName();
        userName.setText(name);

        // Set Date Info
        TextView dayTextView = findViewById(R.id.day_of_week_text_view);
        TextView dateTextView = findViewById(R.id.date_today_text_view);
        dayTextView.setText(sdfDay.format(todayCal.getTime()));
        dateTextView.setText(sdfDate.format(todayCal.getTime()));

        // Set number of jobs reminder:
        // Todo: get instance of the this user, get number of jobs today, display info

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setSelectedItemId(R.id.navigation_trend);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_my_page:
                        startActivity(new Intent(getApplicationContext(), UserPageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_trend:
                        return true;
                }
                return false;
            }
        });

        LinearLayout makeReminderLayout = findViewById(R.id.add_reminder_layout);
        reminderAdapter = new ArrayAdapter<>(this, R.layout.content_reminder_list, reminders);
        reminderListView = findViewById(R.id.reminder_listview);
        reminderListView.setAdapter(reminderAdapter);
        Button addReminderButton = findViewById(R.id.add_reminder_button);
        Button confirmReminderButton = findViewById(R.id.confirm_reminder_button);
        Button cancelReminderButton = findViewById(R.id.cancel_reminder_button);

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReminderLayout.setVisibility(View.VISIBLE);
                addReminderButton.setVisibility(View.INVISIBLE);
            }
        });

        confirmReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReminderLayout.setVisibility(View.GONE);
                EditText note = findViewById(R.id.reminder_edit_text);
                reminders.add(note.getText().toString());
                addReminderButton.setVisibility(View.VISIBLE);
                reminderAdapter.notifyDataSetChanged();
            }
        });

        cancelReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReminderLayout.setVisibility(View.GONE);
                addReminderButton.setVisibility(View.VISIBLE);
            }
        });


        Button setupButton = findViewById(R.id.add_goal_text_view);
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddHabitActivity.class));
                overridePendingTransition(1, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable Back Button.
    }

}