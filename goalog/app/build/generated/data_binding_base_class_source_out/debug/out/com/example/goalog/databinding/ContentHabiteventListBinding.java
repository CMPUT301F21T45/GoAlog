// Generated by view binder compiler. Do not edit!
package com.example.goalog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ContentHabiteventListBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView completeDate;

  @NonNull
  public final TextView eventID;

  @NonNull
  public final TextView habitTitle;

  @NonNull
  public final TextView textView7;

  @NonNull
  public final TextView textView8;

  private ContentHabiteventListBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView completeDate, @NonNull TextView eventID, @NonNull TextView habitTitle,
      @NonNull TextView textView7, @NonNull TextView textView8) {
    this.rootView = rootView;
    this.completeDate = completeDate;
    this.eventID = eventID;
    this.habitTitle = habitTitle;
    this.textView7 = textView7;
    this.textView8 = textView8;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ContentHabiteventListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ContentHabiteventListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.content_habitevent_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ContentHabiteventListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.completeDate;
      TextView completeDate = ViewBindings.findChildViewById(rootView, id);
      if (completeDate == null) {
        break missingId;
      }

      id = R.id.eventID;
      TextView eventID = ViewBindings.findChildViewById(rootView, id);
      if (eventID == null) {
        break missingId;
      }

      id = R.id.habitTitle;
      TextView habitTitle = ViewBindings.findChildViewById(rootView, id);
      if (habitTitle == null) {
        break missingId;
      }

      id = R.id.textView7;
      TextView textView7 = ViewBindings.findChildViewById(rootView, id);
      if (textView7 == null) {
        break missingId;
      }

      id = R.id.textView8;
      TextView textView8 = ViewBindings.findChildViewById(rootView, id);
      if (textView8 == null) {
        break missingId;
      }

      return new ContentHabiteventListBinding((ConstraintLayout) rootView, completeDate, eventID,
          habitTitle, textView7, textView8);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}