package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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
    }

}
