// Generated by view binder compiler. Do not edit!
package com.example.project.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigationView;

  @NonNull
  public final ImageView btnBell;

  @NonNull
  public final ImageView btnChat;

  @NonNull
  public final ImageView btnHome;

  @NonNull
  public final ImageView btnProfile;

  @NonNull
  public final CoordinatorLayout coordinatorLayout;

  @NonNull
  public final FrameLayout frame;

  @NonNull
  public final LinearLayout layout1;

  @NonNull
  public final LinearLayout layout2;

  @NonNull
  public final LinearLayout layout3;

  @NonNull
  public final LinearLayout layout4;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView tvBell;

  @NonNull
  public final TextView tvChat;

  @NonNull
  public final TextView tvHome;

  @NonNull
  public final TextView tvProfile;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationView bottomNavigationView, @NonNull ImageView btnBell,
      @NonNull ImageView btnChat, @NonNull ImageView btnHome, @NonNull ImageView btnProfile,
      @NonNull CoordinatorLayout coordinatorLayout, @NonNull FrameLayout frame,
      @NonNull LinearLayout layout1, @NonNull LinearLayout layout2, @NonNull LinearLayout layout3,
      @NonNull LinearLayout layout4, @NonNull ConstraintLayout main, @NonNull TextView tvBell,
      @NonNull TextView tvChat, @NonNull TextView tvHome, @NonNull TextView tvProfile) {
    this.rootView = rootView;
    this.bottomNavigationView = bottomNavigationView;
    this.btnBell = btnBell;
    this.btnChat = btnChat;
    this.btnHome = btnHome;
    this.btnProfile = btnProfile;
    this.coordinatorLayout = coordinatorLayout;
    this.frame = frame;
    this.layout1 = layout1;
    this.layout2 = layout2;
    this.layout3 = layout3;
    this.layout4 = layout4;
    this.main = main;
    this.tvBell = tvBell;
    this.tvChat = tvChat;
    this.tvHome = tvHome;
    this.tvProfile = tvProfile;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
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
      id = R.id.bottomNavigationView;
      BottomNavigationView bottomNavigationView = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigationView == null) {
        break missingId;
      }

      id = R.id.btnBell;
      ImageView btnBell = ViewBindings.findChildViewById(rootView, id);
      if (btnBell == null) {
        break missingId;
      }

      id = R.id.btnChat;
      ImageView btnChat = ViewBindings.findChildViewById(rootView, id);
      if (btnChat == null) {
        break missingId;
      }

      id = R.id.btnHome;
      ImageView btnHome = ViewBindings.findChildViewById(rootView, id);
      if (btnHome == null) {
        break missingId;
      }

      id = R.id.btnProfile;
      ImageView btnProfile = ViewBindings.findChildViewById(rootView, id);
      if (btnProfile == null) {
        break missingId;
      }

      id = R.id.coordinatorLayout;
      CoordinatorLayout coordinatorLayout = ViewBindings.findChildViewById(rootView, id);
      if (coordinatorLayout == null) {
        break missingId;
      }

      id = R.id.frame;
      FrameLayout frame = ViewBindings.findChildViewById(rootView, id);
      if (frame == null) {
        break missingId;
      }

      id = R.id.layout1;
      LinearLayout layout1 = ViewBindings.findChildViewById(rootView, id);
      if (layout1 == null) {
        break missingId;
      }

      id = R.id.layout2;
      LinearLayout layout2 = ViewBindings.findChildViewById(rootView, id);
      if (layout2 == null) {
        break missingId;
      }

      id = R.id.layout3;
      LinearLayout layout3 = ViewBindings.findChildViewById(rootView, id);
      if (layout3 == null) {
        break missingId;
      }

      id = R.id.layout4;
      LinearLayout layout4 = ViewBindings.findChildViewById(rootView, id);
      if (layout4 == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.tvBell;
      TextView tvBell = ViewBindings.findChildViewById(rootView, id);
      if (tvBell == null) {
        break missingId;
      }

      id = R.id.tvChat;
      TextView tvChat = ViewBindings.findChildViewById(rootView, id);
      if (tvChat == null) {
        break missingId;
      }

      id = R.id.tvHome;
      TextView tvHome = ViewBindings.findChildViewById(rootView, id);
      if (tvHome == null) {
        break missingId;
      }

      id = R.id.tvProfile;
      TextView tvProfile = ViewBindings.findChildViewById(rootView, id);
      if (tvProfile == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, bottomNavigationView, btnBell,
          btnChat, btnHome, btnProfile, coordinatorLayout, frame, layout1, layout2, layout3,
          layout4, main, tvBell, tvChat, tvHome, tvProfile);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
