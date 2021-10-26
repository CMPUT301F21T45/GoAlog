package com.example.goalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class HabitList extends AppCompatActivity /*implements AddHabitActivityConfirmListener*/ {
    ListView HabitList;
    static ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    boolean delete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.habit_list_view);
        HabitList = findViewById(R.id.habit_list);

        habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);

        HabitList.setAdapter(habitAdapter);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection("user001");

        Button buttonAddHabit = (Button) findViewById(R.id.add_habit_button);
        buttonAddHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitList.this, AddHabitActivity.class);
                startActivity(intent);
            }
        });

        Habit newHabit = (Habit) getIntent().getSerializableExtra("New Habit");
        HashMap<String, Habit> data = new HashMap<>();
        data.put("HabitClass", newHabit);
        if (newHabit != null) {
            collectionReference.document(newHabit.getHabitTitle()).set(data);
        }


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
                    if (doc.getData().get("HabitClass") != null) {
                        HashMap<String, String> map = (HashMap<String, String>) doc.getData().get("HabitClass");
                        //assert map != null;
                        String habitReason = map.get("habitReason");
                        String startDate = map.get("startDate");
                        String weekdayPlan = map.get("weekdayPlan");
                        habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan));
                    }
                }
                habitAdapter.notifyDataSetChanged();
                // Notifying the adapter to render any new data fetched from the cloud
            }
        });


/*
        HabitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!delete)
                {
                    Intent intent = new Intent(com.example.goalog.HabitList.this,AddHabitActivity.class);
                    intent.putExtra("Selected Habit",habitDataList.get(i));
                    startActivity(intent);
                }else
                {//implement delete here
                }
            }
        });
 */
    }
}