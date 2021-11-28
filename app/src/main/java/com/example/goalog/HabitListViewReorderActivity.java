package com.example.goalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
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

/** HabitListViewActivity:
 * 1. Retrieve habit data list from firebase
 * 2. Map habit["title","reason","StartDate"] on listView
 * 3. Swipe an habit to edit or delete, using intent to send the selected habit to AddHabitActivity
 * 4. Receive updated habit or new data from AddHabitActivity to firebase
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

        habitDataList = new ArrayList<>();
//        habitDataList.add(h1);

        HabitList.setLayoutManager(new LinearLayoutManager(HabitListViewReorderActivity.this));
        listAdapter = new ReorderListItemAdapter(habitDataList, R.layout.content_habit_reorder_list_view, R.id.reorder_hadler, false);
        HabitList.setAdapter(listAdapter, false);
        HabitList.setCanDragHorizontally(false);


        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection("user003"); //assume we already logged in
        HabitList.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
            }
        });

        // Jump to Add Habit activity for to create a new habit
        // update all position info???
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                for (Habit habit: habitDataList) {
                    Log.d("TAG", habit.getOrderID().toString());
                    habit.setOrderID(new Long(index));
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("HabitClass", habit);
                    collectionReference.document(habit.getHabitID()).update(data);
                    index++;
                }
                Intent intent = new Intent(HabitListViewReorderActivity.this,HabitListViewActivity.class);
                startActivity(intent);

            }
        });

        //upload an new habit to firebase
        Habit newHabit = (Habit) getIntent().getSerializableExtra("New Habit");
        HashMap<String, Habit> data = new HashMap<>();
        data.put("HabitClass", newHabit);
        if (newHabit != null) {
            collectionReference.document(newHabit.getHabitID()).set(data);
        }

        //update existed habits to firebase
        Habit updatedHabit = (Habit) getIntent().getSerializableExtra("Updated Habit");
        HashMap<String, Object> editData = new HashMap<>();
        editData.put("HabitClass", updatedHabit);
        if (updatedHabit != null) {
            collectionReference.document(updatedHabit.getHabitID()).update(editData);
        }

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
                        HashMap<String, Object> map = (HashMap<String, Object>) doc.getData().get("HabitClass");
                        String habitTitle = (String) map.get("habitTitle");
                        String habitReason = (String) map.get("habitReason");
                        String startDate = (String)  map.get("startDate");
                        String weekdayPlan = (String)  map.get("weekdayPlan");
                        boolean isPublic = (boolean) map.get("public");
                        String habitID = (String) map.get("habitID");
                        Long orderID = (Long) map.get("orderID");
                        String latestFinishDate = (String) map.get("latestFinishDate");
                        habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID, orderID, latestFinishDate));
                    }
                }
                Collections.sort(habitDataList);


                // Notifying the adapter to render any new data fetched from the cloud
                listAdapter.notifyDataSetChanged();

            }
        });

    }
}