// Generated by view binder compiler. Do not edit!
package com.example.goalog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.goalog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class HabiteventListViewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ListView HabitEventList;

  @NonNull
  public final Button addEvent;

  @NonNull
  public final Button button;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView userID;

  private HabiteventListViewBinding(@NonNull ConstraintLayout rootView,
      @NonNull ListView HabitEventList, @NonNull Button addEvent, @NonNull Button button,
      @NonNull TextView textView, @NonNull TextView textView3, @NonNull TextView userID) {
    this.rootView = rootView;
    this.HabitEventList = HabitEventList;
    this.addEvent = addEvent;
    this.button = button;
    this.textView = textView;
    this.textView3 = textView3;
    this.userID = userID;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static HabiteventListViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static HabiteventListViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.habitevent_list_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static HabiteventListViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.HabitEventList;
      ListView HabitEventList = ViewBindings.findChildViewById(rootView, id);
      if (HabitEventList == null) {
        break missingId;
      }

      id = R.id.add_event;
      Button addEvent = ViewBindings.findChildViewById(rootView, id);
      if (addEvent == null) {
        break missingId;
      }

      id = R.id.button;
      Button button = ViewBindings.findChildViewById(rootView, id);
      if (button == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.userID;
      TextView userID = ViewBindings.findChildViewById(rootView, id);
      if (userID == null) {
        break missingId;
      }

      return new HabiteventListViewBinding((ConstraintLayout) rootView, HabitEventList, addEvent,
          button, textView, textView3, userID);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}