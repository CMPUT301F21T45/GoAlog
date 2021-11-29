package com.example.goalog;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;


import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * UserPageActivity
 * The class for User Page and other interactions.
 * The page displays the user profile, a progress indicator, today's habit list.
 * You can also follow other's habits by long click their habits (will ask your confirmation)
 */
public class UserPageActivity extends AppCompatActivity {
    ArrayList<Habit> habitDataList;
    ArrayList<String> habitTitleDataList;
    ArrayAdapter<?> listAdapter;
    ListView todayList;
    FirebaseFirestore db;
    String weekday;
    int numOfHabit;
    boolean isFromIntent = false;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

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
        //first three letters for Weekday - get the weekday of the day
        String sdf = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
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
        habitTitleDataList = new ArrayList<>();

        todayList = findViewById(R.id.today_list);

        String emailExtra = getIntent().getStringExtra("emailString");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(currentUser.getEmail());

        // View My Page or Other User's Page
        final TextView myPageTextView = findViewById(R.id.top_my_page_textview);;

        if (emailExtra != null) {
            // See other user's information
            isFromIntent = true;
            collectionReference = db.collection(emailExtra);
            assert user != null;
            myPageTextView.setText("Back To My Page");
            TextView todayPublicTextView = findViewById(R.id.today_or_public_text_view);
            todayPublicTextView.setText("Public Goals");
            myGoalButton.setVisibility(View.INVISIBLE);
            email.setText(emailExtra);
            DocumentReference viewUser = db.collection(emailExtra).document("Info");
            viewUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            HashMap<String, Object> infoMap = (HashMap<String, Object>) document.getData().get("UserInfo");
                            // Set other user's display name
                            if (infoMap != null) {
                                String name = (String) infoMap.get("displayName");
                                userName.setText(name);
                            } else {
                                userName.setText(emailExtra);
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            LinearLayout ingLLayout = findViewById(R.id.following_click_layout);
            LinearLayout erLLayout = findViewById(R.id.follower_click_layout);
            ingLLayout.setVisibility(View.INVISIBLE);
            erLLayout.setVisibility(View.INVISIBLE);
            listAdapter = new ArrayAdapter<>(this, R.layout.content_follow_user_list, habitTitleDataList);
        } else {
            // See your information and indicator
            isFromIntent = false;
            assert user != null;
            String nameString = user.getDisplayName();
            String emailString = user.getEmail();
            userName.setText(nameString);
            email.setText(emailString);
            listAdapter = new TodayHabitListAdapter(this,habitDataList);
        }
        todayList.setAdapter(listAdapter);

        // Back to my page if in another user's page
        myPageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailExtra != null) {
                    Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });
        // follow other's habits
        if (emailExtra != null) {
          todayList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
              @Override
              public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                  AlertDialog followHabitAlert = new  AlertDialog
                          .Builder(UserPageActivity.this)
                          .setTitle("Follow Habit")
                          .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  Habit selectedHabit = habitDataList.get(position);
                                  selectedHabit.setOrderID((long) -1);
                                  selectedHabit.setLatestFinishDate("none");
                                  HashMap<String, Habit> habitMap = new HashMap<>();
                                  habitMap.put("HabitClass", selectedHabit);
                                  CollectionReference collRef = db.collection(currentUser.getEmail());
                                  if (selectedHabit!=null) {
                                      collRef.document(selectedHabit.getHabitID())
                                              .set(habitMap, SetOptions.merge());
                                  }

                              }
                          }).setNegativeButton("No", null).create();
                  followHabitAlert.setOnShowListener(new DialogInterface.OnShowListener() {
                      @Override
                      public void onShow(DialogInterface dialog) {
                          followHabitAlert.getButton(AlertDialog.BUTTON_POSITIVE)
                                  .setTextColor(getResources().getColor(R.color.warning_red));
                      }
                  });
                  followHabitAlert.show();
                  return false;
              }
          });
        }

        // case for view user: All Habits - Public
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(
                    @Nullable QuerySnapshot queryDocumentSnapshots,
                    @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();

                assert queryDocumentSnapshots != null;
                int completedOnTodayNum = 0;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("HabitClass")));
                    // Adding the habits from FireStore
                    HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("HabitClass");
                    if (doc.getData().get("HabitClass") != null){
                        // get all attributes
                        String habitTitle = (String) map.get("habitTitle");
                        String habitReason = (String) map.get("habitReason");
                        String startDate = (String)  map.get("startDate");
                        String weekdayPlan = (String)  map.get("weekdayPlan");
                        boolean isPublic = (boolean) map.get("public");
                        String habitID = (String) map.get("habitID");
                        String latestFinishDate = (String)  map.get("latestFinishDate");

                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                        Date today = new Date();
                        if (emailExtra!=null) {
                            if(isPublic) {
                                habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID));
                                habitTitleDataList.add(habitTitle);
                                // if the latest finished date of habit is today
                                if (latestFinishDate.equals(date.format(today))) {
                                    // increase the completed-on-today habit number
                                    completedOnTodayNum++;
                                }
                            }
                        } else {
                            try {
                                if(today.after(date.parse(startDate))) {
                                    for (int i = 0; i < weekdayPlan.length(); i++) {
                                        char ch = weekdayPlan.charAt(i);
                                        if (weekday.equals(String.valueOf(ch))) {
                                            habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID));
                                            // if the latest finished date of habit is today
                                            if (latestFinishDate.equals(date.format(today))) {
                                                // increase the completed-on-today habit number
                                                completedOnTodayNum++;
                                            }
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    listAdapter.notifyDataSetChanged();

                }
                // get the daily progress related view
                ProgressBar indicator = (ProgressBar) findViewById(R.id.progress_bar_indicator);
                TextView percentage = (TextView) findViewById(R.id.percentage_indicator);
                TextView ratio = (TextView) findViewById(R.id.finished_all_ratio_indicator);

                int ratioNum;
                numOfHabit = habitDataList.size(); // get total number of habits

                // calculate thr ratio of completed-on-today habit number and total habit number
                if (numOfHabit ==0) {
                    ratioNum = 0;
                } else {
                    ratioNum = (int) 100 * completedOnTodayNum/numOfHabit;
                }

                // set the daily progress data
                percentage.setText(ratioNum+"%");
                indicator.setProgress(ratioNum, true);
                ratio.setText(completedOnTodayNum+"/"+numOfHabit);
                // Notifying the adapter to render any new data fetched from the cloud
            }
        });

        // Initializations for following and follower sub sections.
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

        ListView followerListView = findViewById(R.id.follower_listview);
        ListView followingListView = findViewById(R.id.following_listview);

        // following, follower;
        ArrayList<String> userFollowings = new ArrayList<>();
        ArrayList<String> userFollowers = new ArrayList<>();
        ArrayAdapter<String> followingAdapter, followerAdapter;

        followerAdapter = new ArrayAdapter<>(this,R.layout.content_follow_user_list, userFollowers);
        followerListView.setAdapter(followerAdapter);
        followingAdapter = new ArrayAdapter<>(this,R.layout.content_follow_user_list, userFollowings);
        followingListView.setAdapter(followingAdapter);

        DocumentReference followingRef = db.collection(Objects.requireNonNull(currentUser.getEmail()))
                .document("Info");
        followingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("following"));
                            List<String> followers = (List<String>) document.getData().get("following");
                            userFollowings.addAll(followers);
                            followingAdapter.notifyDataSetChanged();
                            TextView numFollowings = findViewById(R.id.num_following_text_view);
                            numFollowings.setText(Integer.toString(userFollowings.size()));
                            Log.d(TAG, "DocumentSnapshot data2: " + userFollowings);
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


        followingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("followers"));
                            List<String> followers = (List<String>) document.getData().get("followers");
                            userFollowers.addAll(followers);
                            followerAdapter.notifyDataSetChanged();
                            TextView numFollowers = findViewById(R.id.num_follower_text_view);
                            numFollowers.setText(Integer.toString(userFollowers.size()));
                            Log.d(TAG, "DocumentSnapshot data2: " + followers);
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

        followingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(UserPageActivity.this, UserPageActivity.class);
                intent.putExtra("emailString", userFollowings.get(i));
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

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
                        if (isFromIntent) {
                            startActivity(new Intent(getApplicationContext(), UserPageActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Disable Back Button.
        if (isFromIntent) {
            Intent intent = new Intent(this, UserPageActivity.class);
            startActivity(intent);
        }
    }
}





