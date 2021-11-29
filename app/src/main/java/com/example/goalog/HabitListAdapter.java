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

/**
 * An ArrayAdapter for habit list, used in the page to show all habit in a list view
 *
 * @see HabitListViewActivity
 */
public class HabitListAdapter extends ArrayAdapter<Habit> {
    private final ArrayList<Habit> Habits;
    private final Context context;

    /**
     * Constructor
     *
     * @param context
     * @param Habits an array list of habits
     */
    public HabitListAdapter(Context context, ArrayList<Habit> Habits) {
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
            view = LayoutInflater.from(context).inflate(R.layout.content_habit_list_view, parent,false);
        }

        // get corresponding Habit object
        Habit habit = Habits.get(position);

        // get all related views
        TextView habitName= view.findViewById(R.id.habit_title);
        TextView habitReason= view.findViewById(R.id.habit_reason);
        TextView startDate= view.findViewById(R.id.startDate);

        // update information to related views
        habitName.setText(habit.getHabitTitle());
        habitReason.setText(habit.getHabitReason());
        startDate.setText(habit.getStartDate());

        return view;

    }
}