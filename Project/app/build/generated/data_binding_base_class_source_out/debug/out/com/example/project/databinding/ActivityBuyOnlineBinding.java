// Generated by view binder compiler. Do not edit!
package com.example.project.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.project.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityBuyOnlineBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton btnBack;

  @NonNull
  public final AppCompatButton btnPay;

  @NonNull
  public final View dimView;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final ProgressBar probar;

  @NonNull
  public final TextView tvCusId;

  @NonNull
  public final TextView tvPay;

  private ActivityBuyOnlineBinding(@NonNull ConstraintLayout rootView, @NonNull ImageButton btnBack,
      @NonNull AppCompatButton btnPay, @NonNull View dimView, @NonNull ConstraintLayout main,
      @NonNull ProgressBar probar, @NonNull TextView tvCusId, @NonNull TextView tvPay) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.btnPay = btnPay;
    this.dimView = dimView;
    this.main = main;
    this.probar = probar;
    this.tvCusId = tvCusId;
    this.tvPay = tvPay;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBuyOnlineBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBuyOnlineBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_buy_online, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBuyOnlineBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnBack;
      ImageButton btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      id = R.id.btnPay;
      AppCompatButton btnPay = ViewBindings.findChildViewById(rootView, id);
      if (btnPay == null) {
        break missingId;
      }

      id = R.id.dimView;
      View dimView = ViewBindings.findChildViewById(rootView, id);
      if (dimView == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.probar;
      ProgressBar probar = ViewBindings.findChildViewById(rootView, id);
      if (probar == null) {
        break missingId;
      }

      id = R.id.tvCusId;
      TextView tvCusId = ViewBindings.findChildViewById(rootView, id);
      if (tvCusId == null) {
        break missingId;
      }

      id = R.id.tvPay;
      TextView tvPay = ViewBindings.findChildViewById(rootView, id);
      if (tvPay == null) {
        break missingId;
      }

      return new ActivityBuyOnlineBinding((ConstraintLayout) rootView, btnBack, btnPay, dimView,
          main, probar, tvCusId, tvPay);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
