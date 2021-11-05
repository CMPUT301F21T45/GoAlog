package com.example.goalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomToday extends ArrayAdapter<Habit> {
    private final ArrayList<Habit> Habits;
    private final Context context;

    public CustomToday(Context context, ArrayList<Habit> Habits) {
        super(context,0,Habits);
        this.Habits = Habits;
        this.context = context;
    }//public custom list

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_today_list, parent,false);
        }

        Habit habit = Habits.get(position);
        TextView habitName= view.findViewById(R.id.habit_title);
        habitName.setText(habit.getHabitTitle());
        return view;

    } // public view
}