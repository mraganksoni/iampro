package com.mssinfotech.iampro.co.fragments;

import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.events.NewLoginEvent;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.viewmodels.LoginViewModel;
import java.util.Objects;
import org.greenrobot.eventbus.EventBus;

/** A simple {@link Fragment} subclass. */
public class LoginFragment extends Fragment {

  /* ***********************************
   * View References
   *********************************** */
  private Button btnLogin;
  private TextInputLayout tilUsername;
  private TextInputLayout tilPassword;
  private EditText etUsername;
  private EditText etPassword;
  private TextView tvForgotPassword;

  /* ***********************************
   * ViewModels
   *********************************** */
  LoginViewModel loginViewModel;

  /* ***********************************
   * SharedPreferences
   *********************************** */
  SharedPreferences loginSharedPreferences;

  public LoginFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    AndroidViewModelFactory factory = new AndroidViewModelFactory(getActivity().getApplication());
    loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);

    loginSharedPreferences = Objects.requireNonNull(getContext())
        .getSharedPreferences(LoginPreferencesConstants.PREF_NAME, Context.MODE_PRIVATE);

    initViews(view);
    hookViews();
    hookIntoViewModel();
  }

  private void initViews(View view) {
    btnLogin = view.findViewById(R.id.btnLogin);
    tilUsername = view.findViewById(R.id.tilUsername);
    tilPassword = view.findViewById(R.id.tilPassword);
    etUsername = view.findViewById(R.id.etUsername);
    etPassword = view.findViewById(R.id.etPassword);
    tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
  }

  private void hookViews() {
    btnLogin.setOnClickListener(
        v -> {
          // Check for username
          if (validateUsername() && validatePassword()) {
            Toast.makeText(getContext(), "Validated", Toast.LENGTH_SHORT).show();
            loginViewModel.startLoginProcess(
                etUsername.getText().toString(), etPassword.getText().toString());
          }
        });
    tvForgotPassword.setOnClickListener(v -> {
      EventBus.getDefault().post(new NewLoginEvent(false));
      Toast.makeText(getContext().getApplicationContext(), "fired", Toast.LENGTH_SHORT).show();
    });
  }

  private void hookIntoViewModel() {
    loginViewModel
        .getLoginStatus()
        .observe(
            this,
            aBoolean -> {
              if (aBoolean) {
                Toast.makeText(getContext().getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                loginSharedPreferences.edit()
                    .putString(LoginPreferencesConstants.KEY_USERNAME, etUsername.getText().toString())
                    .putString(LoginPreferencesConstants.KEY_PASSWORD, etPassword.getText().toString())
                    .commit();
                EventBus.getDefault().post(new NewLoginEvent(true));
                getActivity().finish();
              } else {
                Toast.makeText(getContext().getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
              }
            });
  }

  private boolean validateUsername() {
    final String data = etUsername.getText().toString();
    // Check if username is entered
    if (data.length() == 0) {
      if (!tilUsername.isErrorEnabled()) {
        tilUsername.setErrorEnabled(true);
      }
      tilUsername.setError("Username Required");
      return false;
    } else {
      if (tilUsername.isErrorEnabled()) {
        tilUsername.setErrorEnabled(false);
      }
      return true;
    }
  }

  private boolean validatePassword() {
    final String data = etPassword.getText().toString();
    // Check if username is entered
    if (data.length() == 0) {
      if (!tilPassword.isErrorEnabled()) {
        tilPassword.setErrorEnabled(true);
      }
      tilPassword.setError("Username Required");
      return false;
    } else {
      if (tilPassword.isErrorEnabled()) {
        tilPassword.setErrorEnabled(false);
      }
      return true;
    }
  }
}
