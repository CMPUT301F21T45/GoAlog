package com.example.goalog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    ListView HabitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HabitList=findViewById(R.id.list_view);
        list = new ArrayList<>();
        habitAdapter = new CustomList(this, list);
        HabitList.setAdapter(habitAdapter);
    }
}
