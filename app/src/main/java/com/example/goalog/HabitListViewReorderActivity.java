package com.example.goalog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
        setContentView(R.layout.habit_list_view_reorder);
        FloatingActionButton buttonDone = findViewById(R.id.reorder_done_button);
        HabitList = findViewById(R.id.habit_list);
        Habit h1 = new Habit("habit1", "r","dd","T",true,"1");
        Habit h2 = new Habit("habit2", "r","dd","T",true,"2");
        Habit h3 = new Habit("habit3", "r","dd","T",true,"3");

        habitDataList = new ArrayList<>();
//        habitDataList.add(h1);

        HabitList.setLayoutManager(new LinearLayoutManager(HabitListViewReorderActivity.this));
        listAdapter = new ReorderListItemAdapter(habitDataList, R.layout.habit_list_view_content, R.id.habit_list_view_item_used_in_RecyclerView, false);
        HabitList.setAdapter(listAdapter, false);
        HabitList.setCanDragHorizontally(false);


        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection("user003"); //assume we already logged in
        HabitList.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(HabitListViewReorderActivity.this, "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(HabitListViewReorderActivity.this, "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Jump to Add Habit activity for to create a new habit
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // back to normal page
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
                        habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID, orderID));
                    }
                }


                // Notifying the adapter to render any new data fetched from the cloud
                listAdapter.notifyDataSetChanged();

            }
        });

    }
}