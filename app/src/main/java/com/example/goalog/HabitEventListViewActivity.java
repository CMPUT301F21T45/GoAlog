package com.example.goalog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Nullable;


public class HabitEventListViewActivity extends AppCompatActivity {
    ListView HabitEventList;
    ArrayAdapter<HabitEvent> habitEventArrayAdapter;
    ArrayList<HabitEvent> List;
    /** HabitListViewActivity:
     * 1. Retrieve habitEvent data list from firebase
     * 2. Map habitEvent["title","Date"] on listView
     * 3. Swipe an habit to edit or delete, using intent to send the selected habit to AddHabitEventActivity
     * 4. Receive updated habit or new data from AddHabitEventActivity to firebase
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-------set up parameters-------------------
        Habit selectedHabit = (Habit) getIntent().getSerializableExtra("Selected");
        String selectedHabitId= selectedHabit.getHabitID();
        setContentView(R.layout.habitevent_list_view);
        HabitEventList=findViewById(R.id.habit_event_list);
        List = new ArrayList<>();
        habitEventArrayAdapter= new HabitEventCustomList(this, List);
        HabitEventList.setAdapter(habitEventArrayAdapter);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        try {
            final CollectionReference collectionReference = database.collection("user003")
                    .document(selectedHabitId)
                    .collection("HabitEvent");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onEvent(
                        @Nullable QuerySnapshot queryDocumentSnapshots,
                        @Nullable FirebaseFirestoreException error) {
                    List.clear();

                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d("Retrieve", String.valueOf(doc.getData().get("Event")));
                        // TODO: Retrieve data from firebase.
                        // Adding the habits from FireStore
                        HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("Event");
                        if (doc.getData().get("Event") != null){

                            String habitTitle = (String)  map.get("habitTitle");
                            String completeTime =  (String)  map.get("completeDate");
                            String eventCommentString = (String) map.get("eventComment");
                            String eventID = (String) map.get("eventID");
                            List.add(new HabitEvent(eventID,eventCommentString,completeTime,habitTitle));
                        }

                    }
                    habitEventArrayAdapter.notifyDataSetChanged();
                }
            });
        }
        catch (Exception e){
            Intent intent = new Intent(HabitEventListViewActivity.this, HabitListViewActivity.class);
            startActivity(intent);
        }






        }
}