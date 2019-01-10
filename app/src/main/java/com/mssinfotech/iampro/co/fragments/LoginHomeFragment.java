package com.mssinfotech.iampro.co.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import com.mssinfotech.iampro.co.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginHomeFragment extends Fragment {
  /* ***********************************
   * Callbacks
   *********************************** */
  private LoginHomeFragmentCallback callback;

  /* ***********************************
   * View References
   *********************************** */
  private Button btnLogin;
  private Button btnSignup;

  public LoginHomeFragment() {
    // Required empty public constructor
  }

  public void setCallback(LoginHomeFragmentCallback callback) {
    this.callback = callback;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_login_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initViews(view);
    hookViews();
  }

  private void initViews(View view) {
    btnLogin = view.findViewById(R.id.btnLogin);
    btnSignup = view.findViewById(R.id.btnSignup);
  }

  private void hookViews() {
    btnLogin.setOnClickListener((view) -> {
      if (callback != null) {
        callback.onLoginButtonClicked();
      }
    });
    btnSignup.setOnClickListener(v -> {
      if (callback != null) {
        callback.onSignupButtonClicked();
      }
    });
  }

  public interface LoginHomeFragmentCallback {
    void onLoginButtonClicked();
    void onSignupButtonClicked();
  }
}
