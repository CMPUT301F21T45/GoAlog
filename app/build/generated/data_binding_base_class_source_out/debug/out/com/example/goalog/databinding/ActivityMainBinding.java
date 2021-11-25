// Generated by view binder compiler. Do not edit!
package com.example.goalog.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.goalog.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView finishedAllRatioIndicator;

  @NonNull
  public final TextView percentageIndicator;

  @NonNull
  public final ProgressBar progressBarIndicator;

  private ActivityMainBinding(@NonNull LinearLayout rootView,
      @NonNull TextView finishedAllRatioIndicator, @NonNull TextView percentageIndicator,
      @NonNull ProgressBar progressBarIndicator) {
    this.rootView = rootView;
    this.finishedAllRatioIndicator = finishedAllRatioIndicator;
    this.percentageIndicator = percentageIndicator;
    this.progressBarIndicator = progressBarIndicator;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.finished_all_ratio_indicator;
      TextView finishedAllRatioIndicator = ViewBindings.findChildViewById(rootView, id);
      if (finishedAllRatioIndicator == null) {
        break missingId;
      }

      id = R.id.percentage_indicator;
      TextView percentageIndicator = ViewBindings.findChildViewById(rootView, id);
      if (percentageIndicator == null) {
        break missingId;
      }

      id = R.id.progress_bar_indicator;
      ProgressBar progressBarIndicator = ViewBindings.findChildViewById(rootView, id);
      if (progressBarIndicator == null) {
        break missingId;
      }

      return new ActivityMainBinding((LinearLayout) rootView, finishedAllRatioIndicator,
          percentageIndicator, progressBarIndicator);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
