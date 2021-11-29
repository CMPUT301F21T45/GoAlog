package com.example.goalog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * An ArrayAdapter for habit list, used in the page to show all habit of today
 *
 * @see UserPageActivity
 */
public class TodayHabitListAdapter extends ArrayAdapter<Habit> {
    private final ArrayList<Habit> Habits;
    private final Context context;
    private static boolean doneClicked = false;

    /**
     * Constructor
     *
     * @param context
     * @param Habits an array list of habits
     */
    public TodayHabitListAdapter(Context context, ArrayList<Habit> Habits) {
        super(context,0,Habits);
        this.Habits = Habits;
        this.context = context;
    }


    /**
     * help to build the view inside a content of a list view
     *
     * @param position      the position of this content inside the list
     * @param convertView   the view to be built
     * @param parent        parent view group
     * @return              the built view based on given information
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_today_list, parent,false);
        }
        // get corresponding Habit object
        Habit habit = Habits.get(position);

        // show the Habit name
        TextView habitName= view.findViewById(R.id.habit_title);
        habitName.setText(habit.getHabitTitle());

        Button doneButton = (Button)view.findViewById(R.id.done_goal);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // once the done button is clicked
            public void onClick(View v) {
                // go to AddHabitEventActivity page with the Habit object informantion
                Intent intent = new Intent(parent.getContext(), AddHabitEventActivity.class);
                intent.putExtra("Habit",habit);
                parent.getContext().startActivity(intent);
                doneClicked  = true;
            }
        });

        return view;

    }


}