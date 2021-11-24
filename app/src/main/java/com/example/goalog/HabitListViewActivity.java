package com.example.goalog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

/** HabitListViewActivity:
 * 1. Retrieve habit data list from firebase
 * 2. Map habit["title","reason","StartDate"] on listView
 * 3. Swipe an habit to edit or delete, using intent to send the selected habit to AddHabitActivity
 * 4. Receive updated habit or new data from AddHabitActivity to firebase
 */
public class HabitListViewActivity extends AppCompatActivity{
    SwipeMenuListView HabitList;
    static ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        //-------set up parameters-------------------
        setContentView(R.layout.habit_list_view_swipe_to_edit);
        FloatingActionButton buttonAddHabit = findViewById(R.id.add_habit_button);
        HabitList = findViewById(R.id.habit_list);
        habitDataList = new ArrayList<>();
        habitAdapter = new CustomHabitList(this, habitDataList);
        HabitList.setAdapter(habitAdapter);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = database.collection("user003"); //assume we already logged in
        //----------------------------------------------
        HabitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habit selectedHabit=habitDataList.get(position);
                Intent intent=new Intent(HabitListViewActivity.this,HabitEventListViewActivity.class);
                intent.putExtra("Selected",selectedHabit);
                startActivity(intent);
            }
        });

        // Jump to Add Habit activity for to create a new habit
        buttonAddHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitListViewActivity.this, AddHabitActivity.class);
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
        HabitList.setMenuCreator(creator);

        HabitList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // Edit (pen is clicked)
                        Intent intent = new Intent(com.example.goalog.HabitListViewActivity.this,AddHabitActivity.class);
                        intent.putExtra("Selected Habit",habitDataList.get(position));
                        startActivity(intent);
                        break;
                    case 1:
                        // Delete (trash can is clicked)
                        collectionReference.document(habitDataList.get(position).getHabitID())
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

        //Retrieve Habit data list from firebase
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
                        habitDataList.add(new Habit(habitTitle, habitReason, startDate, weekdayPlan, isPublic,habitID));
                    }
                }
                habitAdapter.notifyDataSetChanged();
                // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }
}