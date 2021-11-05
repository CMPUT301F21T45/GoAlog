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

public class HabitEventCustomList extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> HabitEvents;
    private Context context;
    public HabitEventCustomList(Context context, ArrayList<HabitEvent> HabitEvents) {
        super(context,0,HabitEvents);
        this.HabitEvents= HabitEvents;
        this.context = context;
    }//public custom list



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_habitevent_list_view, parent,false);
        }

        HabitEvent habitEvent = HabitEvents.get(position);

        TextView habitName= view.findViewById(R.id.habitTitle);

        TextView completeDate= view.findViewById(R.id.completeDate);


        habitName.setText(habitEvent.getHabitTitle());

        completeDate.setText(habitEvent.getCompleteDate().toString());


        return view;

    } // public view
}
