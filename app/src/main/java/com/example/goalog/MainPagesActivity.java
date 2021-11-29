package com.example.goalog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * MainPagesActivity
 * View basic information; view, add and delete your reminders, set up your goal and logout in this
 * activity. This is the page you see once you successfully logged in.
 * 1. Logout: long click the logout icon. You'll see an Alert to confirm your logout.
 * 2. Reminder: You can add your daily reminders here. Long click to delete.
 * 3. Set up your goals. Tap the button to that activity.
 */
public class MainPagesActivity extends AppCompatActivity {

    private final ArrayList<String> reminders = new ArrayList<>();
    private ArrayAdapter<?> reminderAdapter;
    ListView reminderListView;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        Calendar todayCal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("MMMM dd");

        setContentView(R.layout.activity_main_pages);

        // Set name
        TextView userName = findViewById(R.id.user_name);
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String name = user.getDisplayName();
        userName.setText(name);

        // Set Date Info
        TextView dayTextView = findViewById(R.id.day_of_week_text_view);
        TextView dateTextView = findViewById(R.id.date_today_text_view);
        dayTextView.setText(sdfDay.format(todayCal.getTime()));
        dateTextView.setText(sdfDate.format(todayCal.getTime()));

        // Set number of jobs reminder:
        // Get instance of the this user, get number of jobs today, display info

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

        final CollectionReference reference = db.collection(Objects.requireNonNull(user.getEmail()));
        confirmReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReminderLayout.setVisibility(View.GONE);
                EditText note = findViewById(R.id.reminder_edit_text);
                // reminders.add(note.getText().toString());
                reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String content = note.getText().toString();
                        reference.document("Info")
                                .update("Reminder", FieldValue.arrayUnion(content));
                        reminders.add(content);
                        reminderAdapter.notifyDataSetChanged();
                        note.setText("");
                    }
                });
                makeReminderLayout.setVisibility(View.GONE);
                addReminderButton.setVisibility(View.VISIBLE);
            }
        });

        // get reminders from firebase
        final DocumentReference docRef = reference.document("Info");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("Reminder"));
                            List<String> reminderList = (List<String>) document.getData().get("Reminder");
                            reminders.addAll(reminderList);
                            reminderAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                reference.document("Info")
                        .update("Reminder", FieldValue.arrayRemove(reminders.get(position)));
                reminders.remove(reminders.get(position));
                reminderAdapter.notifyDataSetChanged();

                return true;
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

        // Sign Out
        ImageButton signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(MainPagesActivity.this)
                        .setTitle("Sign Out").setMessage("ARE YOU SURE?")
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Not Now", null)
                        .create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setTextColor(getResources().getColor(R.color.warning_red));
                    }
                });
                alert.show();
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Disable Back Button.
    }

}