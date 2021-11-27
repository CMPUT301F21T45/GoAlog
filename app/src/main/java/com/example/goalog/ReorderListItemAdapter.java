package com.example.goalog;


import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

class ReorderListItemAdapter extends DragItemAdapter<Habit, ReorderListItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;

    ReorderListItemAdapter(ArrayList<Habit> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Habit habit= mItemList.get(position);
        // update information to related views
        holder.habitName.setText(habit.getHabitTitle());
        holder.habitReason.setText(habit.getHabitReason());
        holder.startDate.setText(habit.getStartDate());
    }

    @Override
    public long getUniqueItemId(int position) {
        // change later right now use the habit ID
        return mItemList.get(position).getOrderID() ;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView habitName;
        TextView habitReason;
        TextView startDate;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            // get all related views
            habitName= itemView.findViewById(R.id.habit_title);
            habitReason= itemView.findViewById(R.id.habit_reason);
            startDate= itemView.findViewById(R.id.startDate);

        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}