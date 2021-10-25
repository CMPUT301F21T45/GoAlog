package com.example.goalog;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class HabitList extends AppCompatActivity {
    ListView HabitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habitlist);
        HabitList=findViewById(R.id.list_view);
        list = new ArrayList<>();
        habitAdapter = new CustomList(this, list);
        HabitList.setAdapter(habitAdapter);
    }
}
