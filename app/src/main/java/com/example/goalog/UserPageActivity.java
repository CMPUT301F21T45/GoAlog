package com.example.goalog;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Nullable;

/**
 * UserPageActivity
 * The class for User Page and other interactions.
 * The page displays the user profile, a progress indicator, today's habit list.
 * Current Issues:
 *   1. Need to set the "Done" button to invisible after successfully create a Event for the habit.
 *   2. Finish Visual Indicator
 */
public class UserPageActivity extends AppCompatActivity {
    ArrayList<Habit> habitDataList;
    ArrayAdapter<Habit> listAdapter;
    ListView todayList;
    FirebaseFirestore db;
    String weekday;
    int numOfHabit;

    /**
     * User: Today's Habit List, Visual Indicator, Button to all habits, habit events.
     * Today's List prompts th user to follow today's schedule.
     * The visual indicator reflects the day's progress.
     * Buttons to page, activities switch.
     * @param savedInstanceState
     *  Bundle savedInstanceSate
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        TextView userName = findViewById(R.id.my_user_name_text_view);
        TextView email = findViewById(R.id.my_email_text_view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String nameString = user.getDisplayName();
        String emailString = user.getEmail();
        userName.setText(nameString);
        email.setText(emailString);

        Button myGoalButton = findViewById(R.id.my_goal);
        myGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserPageActivity.this, HabitListViewActivity.class);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        //first three letters for Weekday
        String sdf = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());//get today's weekday
        switch (sdf){
            case "Mon":
                weekday = "1";
                break;
            case "Tue":
                weekday = "2";
                break;
            case "Wed":
                weekday = "3";
                break;
            case "Thu":
                weekday = "4";
                break;
            case "Fri":
                weekday = "5";
                break;
            case "Sat":
                weekday = "6";
                break;
            case "Sun":
                weekday = "7";
                break;
        }

        habitDataList = new ArrayList<>();
        listAdapter = new CustomTodayHabitList(this,habitDataList);
        todayList = findViewById(R.id.today_list);
        todayList.setAdapter(listAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("user003");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(
                    @Nullable QuerySnapshot queryDocumentSnapshots,
                    @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("HabitClass")));
                    // TODO: Retrieve data from firebase.
                    // Adding the habits from FireStore
                    HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("HabitClass");
                    if (doc.getData().get("HabitClass") != null){
                        String habitTitle = (String) map.get("habitTitle");
                        String habitReason = (String) map.get("habitReason");
                        String startDate = (String)  map.get("startDate");
                        String weekdayPlan = (String)  map.get("weekdayPlan");
                        boolean isPublic = (boolean) map.get("public");
                        String habitID = (String) map.get("habitID");
                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date today = new Date();
                        try {
                            if(today.after(date.parse(startDate))) {
                                for (int i = 0; i < weekdayPlan.length(); i++) {
                                    char ch = weekdayPlan.charAt(i);
                                    if (weekday.equals(String.valueOf(ch))) {
                                        habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID));
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }}
                    listAdapter.notifyDataSetChanged();
                    ProgressBar indicator = (ProgressBar) findViewById(R.id.progress_bar_indicator);
                    TextView percentage = (TextView) findViewById(R.id.percentage_indicator);
                    TextView ratio = (TextView) findViewById(R.id.finished_all_ratio_indicator);

                    int ratioNum;
                    numOfHabit = habitDataList.size();

                    if (numOfHabit ==0) {
                        ratioNum = 0;
                    } else {
                        ratioNum = (int) 100 * 1/numOfHabit;
                    }

                    // TODO: Finish Visual Indicator
                    // Today's Progress:
                    percentage.setText(ratioNum+"%");
                    indicator.setProgress(ratioNum, true);
                    ratio.setText("1/"+numOfHabit);
                    // Notifying the adapter to render any new data fetched from the cloud
                }
            }
        });

        LinearLayout followerLayout, followingLayout, todayLinearLayout;
        followerLayout = findViewById(R.id.follower_click_layout);
        followingLayout = findViewById(R.id.following_click_layout);
        todayLinearLayout = findViewById(R.id.my_today_linear_layout);
        TextView followerText = findViewById(R.id.follower_textview);
        TextView followingText = findViewById(R.id.following_textview);
        LinearLayout followerLLayout = findViewById(R.id.follower_list_layout);
        LinearLayout followingLLayout = findViewById(R.id.following_list_layout);
        ImageButton imageFollowerButton = findViewById(R.id.close_er_button);
        ImageButton imageFollowingButton = findViewById(R.id.close_ing_button);
        //Todo: following, follower;
        ArrayList<User> following, follower;

        imageFollowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followerText.setTextColor(getResources().getColor(R.color.white));
                followingLLayout.setVisibility(View.GONE);
                followerLLayout.setVisibility(View.GONE);
                todayLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        imageFollowingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingText.setTextColor(getResources().getColor(R.color.white));
                followingLLayout.setVisibility(View.GONE);
                followerLLayout.setVisibility(View.GONE);
                todayLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        followerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingText.setTextColor(getResources().getColor(R.color.white));
                todayLinearLayout.setVisibility(View.GONE);
                followingLLayout.setVisibility(View.GONE);
                followerLLayout.setVisibility(View.VISIBLE);
                followerText.setTextColor(getResources().getColor(R.color.blue));
            }
        });

        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followerText.setTextColor(getResources().getColor(R.color.white));
                todayLinearLayout.setVisibility(View.GONE);
                followerLLayout.setVisibility(View.GONE);
                followingLLayout.setVisibility(View.VISIBLE);
                followingText.setTextColor(getResources().getColor(R.color.blue));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setSelectedItemId(R.id.navigation_my_page);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_trend:
                        startActivity(new Intent(getApplicationContext(), MainPagesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_my_page:
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Disable Back Button.
    }
}





