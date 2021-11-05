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
    SwipeMenuListView HabitEventList;
    ArrayAdapter<HabitEvent> habitEventArrayAdapter;
    ArrayList<HabitEvent> habitEventDataList;
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
        habitEventDataList = new ArrayList<>();
        habitEventArrayAdapter= new HabitEventCustomList(this, habitEventDataList);
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
                    habitEventDataList.clear();

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
                            habitEventDataList.add(new HabitEvent(eventID,eventCommentString,completeTime,habitTitle));
                        }

                    }
                    habitEventArrayAdapter.notifyDataSetChanged();
                }
            });

            // for swipe delete
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0x33, 0x99,
                            0xff)));
                    // set item width
                    openItem.setWidth(180);
                    // set item title
                    openItem.setIcon(R.drawable.ic_edit);
                    // add to menu
                    menu.addMenuItem(openItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(180);
                    // set a icon
                    deleteItem.setIcon(R.drawable.ic_delete);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };

            // set creator
            HabitEventList.setMenuCreator(creator);

            HabitEventList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // Edit (pen is clicked)
                            Intent intent = new Intent(com.example.goalog.HabitEventListViewActivity.this,AddHabitActivity.class);
                            intent.putExtra("Selected Habit",habitEventDataList.get(position));
                            startActivity(intent);
                            break;
                        case 1:
                            // Delete (trash can is clicked)
                            collectionReference.document(habitEventDataList.get(position).getEventID())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Sample", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Sample", "Error deleting document", e);
                                        }
                                    });
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });

        }
        catch (Exception e){
            Intent intent = new Intent(HabitEventListViewActivity.this, HabitListViewActivity.class);
            startActivity(intent);
        }






    }
}