package com.example.goalog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;


public class HabitEventListViewActivity extends AppCompatActivity {
    ListView HabitEventList;
    ArrayAdapter<HabitEvent> habitEventArrayAdapter;
    ArrayList<HabitEvent> List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitevent_list_view);
        HabitEventList=findViewById(R.id.HabitEventList);
        List = new ArrayList<>();
        habitEventArrayAdapter= new HabitEventCustomList(this, List);
        HabitEventList.setAdapter(habitEventArrayAdapter);
        //create habit event list
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection("user002");
        //connect the firestore
        Button buttonAddHabitEvent = (Button) findViewById(R.id.add_event);
        buttonAddHabitEvent.setOnClickListener(new View.OnClickListener() {
            //add some object to habit event list
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(HabitEventListViewActivity.this, AddHabitEventActivity.class);
                //startActivity(intent);
            }
        });

        
        HabitEvent newHabitEvent = (HabitEvent) getIntent().getSerializableExtra("New HabitEvent");
        HashMap<String, HabitEvent> data = new HashMap<>();
        data.put("EventID", newHabitEvent);
        if (newHabitEvent != null) {
            collectionReference.document(newHabitEvent.getHabitTitle()).set(data);
        }

        //get the data of firestore
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(
                    @Nullable QuerySnapshot queryDocumentSnapshots,
                    @Nullable FirebaseFirestoreException error) {
                List.clear();

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("EventID")));
                    String EventID = doc.getId();
                    if (doc.getData().get("HabitClass") != null) {
                        if (doc.getData().get("EventID") != null) {
                            HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("HabitEventClass");
                            //assert map != null;
                            boolean location = (boolean) map.get("LocationRecorded");
                            String eventComment = (String) map.get("eventComment");
                            LocalDate completeDate = (LocalDate) map.get("completeDate");
                            String eventID = (String) map.get("eventID");
                            String habitTitle = (String) map.get("habitTitle");
                            double latitude = (double) map.get("latitude");
                            double longitude = (double) map.get("longitude");
                            String userID = (String) map.get("userID");

                            List.add(new HabitEvent(location, completeDate, eventComment, eventID, habitTitle, latitude, longitude, userID));
                        }
                    }
                }
                habitEventArrayAdapter.notifyDataSetChanged();
                // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }

}
