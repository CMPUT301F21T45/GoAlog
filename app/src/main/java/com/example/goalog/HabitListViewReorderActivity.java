package com.example.goalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.annotation.Nullable;

/**
 * HabitListViewReorderActivity: creates an Activity to
 * 1. Retrieve habit data list from firebase
 * 2. Allow user drag to reorder the habits
 * 3. When user click the check button, update orderID of habits to firebase based on the current order
 */
public class HabitListViewReorderActivity extends AppCompatActivity{
    DragListView HabitList;
    ReorderListItemAdapter listAdapter;
    ArrayList<Habit> habitDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        //-------set up parameters-------------------
        setContentView(R.layout.activity_habit_list_view_reorder);
        FloatingActionButton buttonDone = findViewById(R.id.reorder_done_button);
        HabitList = findViewById(R.id.habit_list);
        HabitList.setLayoutManager(new LinearLayoutManager(HabitListViewReorderActivity.this));
        // setup habit list data
        habitDataList = new ArrayList<>();
        listAdapter = new ReorderListItemAdapter(habitDataList, R.layout.content_habit_reorder_list_view, R.id.reorder_hadler, false);
        HabitList.setAdapter(listAdapter, false);
        HabitList.setCanDragHorizontally(false);

        // connect to firebase
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection(currentUser.getEmail());

        // once the user click the check(done) button, update current order info to every habits
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                for (Habit habit: habitDataList) {
                    Log.d("TAG", habit.getOrderID().toString());
                    // get current orderID
                    habit.setOrderID(new Long(index));
                    HashMap<String, Object> data = new HashMap<>();
                    // update to firebase
                    data.put("HabitClass", habit);
                    collectionReference.document(habit.getHabitID()).update(data);
                    index++;
                }
                // back to habit list view page
                Intent intent = new Intent(HabitListViewReorderActivity.this,HabitListViewActivity.class);
                startActivity(intent);

            }
        });

        // load all habits to habitDataList
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(
                    @Nullable QuerySnapshot queryDocumentSnapshots,
                    @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    // Iterate document
                    Log.d("Retrieve", String.valueOf(doc.getData().get("HabitClass")));
                    if (doc.getData().get("HabitClass") != null) {
                        // load all details of current habit
                        HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("HabitClass");
                        String habitTitle = (String) map.get("habitTitle");
                        String habitReason = (String) map.get("habitReason");
                        String startDate = (String)  map.get("startDate");
                        String weekdayPlan = (String)  map.get("weekdayPlan");
                        boolean isPublic = (boolean) map.get("public");
                        String habitID = (String) map.get("habitID");
                        Long orderID = (Long) map.get("orderID");
                        String latestFinishDate = (String) map.get("latestFinishDate");
                        // add to habitDataList
                        habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID, orderID, latestFinishDate));
                    }
                }
                // sort the habit data list based on the orderID
                Collections.sort(habitDataList);

                // Notifying the adapter to render any new data fetched from the cloud
                listAdapter.notifyDataSetChanged();
            }
        });

    }
}