package com.example.goalog;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class HabitList extends AppCompatActivity {
    ListView Habit_List;
    ArrayAdapter<Habit> habit_Adapter;
    ArrayList<Habit> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitlist);
        Habit_List=findViewById(R.id.list_view);
        list = new ArrayList<>();
        habit_Adapter = new CustomList(this, list);
        Habit_List.setAdapter(habit_Adapter);
    }
}
