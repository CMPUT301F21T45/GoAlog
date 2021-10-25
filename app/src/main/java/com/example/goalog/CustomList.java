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

public class CustomList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> Habits;
    private Context context;
    public CustomList(Context context, ArrayList<Habit> Habits) {
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
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Habit habit = Habits.get(position);
        TextView userID = view.findViewById(R.id.userID);
        TextView habbitName= view.findViewById(R.id.habitTitle);
        TextView habbitReason= view.findViewById(R.id.reason);
        TextView startDate= view.findViewById(R.id.startDate);

        userID.setText(habit.getUserID());
        habbitName.setText(habit.getHabitTitle());
        habbitReason.setText(habit.getReason());
        startDate.setText(habit.getDate().toString());


        return view;

    } // public view
}
