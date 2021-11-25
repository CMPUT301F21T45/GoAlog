// Generated by view binder compiler. Do not edit!
package com.example.goalog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.goalog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ContentBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView habitReason;

  @NonNull
  public final TextView habitTitle;

  @NonNull
  public final TextView startDate;

  private ContentBinding(@NonNull LinearLayout rootView, @NonNull TextView habitReason,
      @NonNull TextView habitTitle, @NonNull TextView startDate) {
    this.rootView = rootView;
    this.habitReason = habitReason;
    this.habitTitle = habitTitle;
    this.startDate = startDate;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ContentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ContentBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.content, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ContentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.habit_reason;
      TextView habitReason = ViewBindings.findChildViewById(rootView, id);
      if (habitReason == null) {
        break missingId;
      }

      id = R.id.habit_title;
      TextView habitTitle = ViewBindings.findChildViewById(rootView, id);
      if (habitTitle == null) {
        break missingId;
      }

      id = R.id.startDate;
      TextView startDate = ViewBindings.findChildViewById(rootView, id);
      if (startDate == null) {
        break missingId;
      }

      return new ContentBinding((LinearLayout) rootView, habitReason, habitTitle, startDate);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
