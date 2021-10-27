package com.example.goalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Nullable;

public class UserPageActivity extends AppCompatActivity {
    ArrayList<Habit> habitDataList;
    ArrayAdapter<Habit> listAdapter;
    ListView todayList;
    FirebaseFirestore db;
    String weekday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        Button myGoalButton = findViewById(R.id.my_goal);
        myGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserPageActivity.this, HabitList.class);
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
        listAdapter = new CustomList(this,habitDataList);
        todayList = findViewById(R.id.today_list);
        todayList.setAdapter(listAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("user001");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(
                    @Nullable QuerySnapshot queryDocumentSnapshots,
                    @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("HabitClass")));
                    String habitTitle = doc.getId();
                    // TODO: Retrieve data from firebase.
                    // Adding the habits from FireStore
                    HashMap<String, String> map = (HashMap<String, String>) doc.getData().get("HabitClass");
                    String weekdayPlan = map.get("weekdayPlan");
                    String startDate = map.get("startDate");
                    String habitReason = map.get("habitReason");

                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                    Date today = new Date();
                    try {
                        if(today.after(date.parse(startDate))) {
                            for (int i = 0; i < weekdayPlan.length(); i++) {
                                char ch = weekdayPlan.charAt(i);
                                if (weekday.equals(String.valueOf(ch))) {
                                    habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan));
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listAdapter.notifyDataSetChanged();
                    // Notifying the adapter to render any new data fetched from the cloud
                }
            }
        });
}
}





