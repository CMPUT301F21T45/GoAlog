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
 * An ArrayAdapter for habit event list, used in the page to show all habit events in a list view
 *
 * @see HabitEventListViewActivity
 */
public class HabitEventListAdapter extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> HabitEvents;
    private Context context;

    /**
     * Constructor
     *
     * @param context
     * @param HabitEvents an array list of habit events
     */
    public HabitEventListAdapter(Context context, ArrayList<HabitEvent> HabitEvents) {
        super(context,0,HabitEvents);
        this.HabitEvents= HabitEvents;
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
            view = LayoutInflater.from(context).inflate(R.layout.content_habitevent_list_view, parent,false);
        }

        // get corresponding HabitEvent object
        HabitEvent habitEvent = HabitEvents.get(position);

        // get all related views
        TextView completeDate= view.findViewById(R.id.completeDate);
        TextView eventComment= view.findViewById(R.id.eventComment);

        // update information to related views
        completeDate.setText(habitEvent.getCompleteDate().toString());
        eventComment.setText(habitEvent.getEventComment().toString());

        return view;

    }
}
