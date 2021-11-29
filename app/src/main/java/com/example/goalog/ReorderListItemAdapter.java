package com.example.goalog;


import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

/**
 * An Adapter for habit list, used in the page to show all habit in a reorderable list view
 *
 * @see HabitListViewReorderActivity
 */
class ReorderListItemAdapter extends DragItemAdapter<Habit, ReorderListItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;


    /**
     * Constructor
     *
     * @param list              a arraylist of Habit to be shown
     * @param layoutId          int, the id of layout used to show habit list
     * @param grabHandleId      int, the id of grab handle to allow user to drag and reorder habits
     * @param dragOnLongPress   a boolean to indicate whether user can drag item by long press or not
     */
    ReorderListItemAdapter(ArrayList<Habit> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }

    /**
     * a ViewHolder to get the related view of a habit list
     *
     * @param parent        parent view group
     * @param viewType      int to indicate the view type
     * @return              the ViewHolder of the habit list
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    /**
     * bind data to corresponding views of an item in list
     *
     * @param position      the position of this content inside the list
     * @param holder        the ViewHolder of an item in list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Habit habit= mItemList.get(position);
        // update information to related views
        holder.habitName.setText(habit.getHabitTitle());
        holder.habitReason.setText(habit.getHabitReason());
        holder.startDate.setText(habit.getStartDate());
    }

    /**
     * get the order ID if an item inside list
     *
     * @param position      the position of this content inside the list
     * @return              the ID used to order the items in list
     */
    @Override
    public long getUniqueItemId(int position) {
        // use the orderID attribute of a habit
        return mItemList.get(position).getOrderID() ;
    }

    /**
     * a class to get the related view of an item view
     */
    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView habitName;
        TextView habitReason;
        TextView startDate;

        /**
         * Constructor
         *
         * @param itemView  the view to be built
         */
        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            // get all related views
            habitName= itemView.findViewById(R.id.habit_title);
            habitReason= itemView.findViewById(R.id.habit_reason);
            startDate= itemView.findViewById(R.id.startDate);

        }
    }
}