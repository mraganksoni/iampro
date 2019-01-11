package com.mssinfotech.iampro.co.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mssinfotech.iampro.co.R;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.events.NewLoginEvent;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.viewmodels.LoginViewModel;
import com.mssinfotech.iampro.co.viewmodels.RegisterViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {
  private Button btnRegister;
  private TextInputLayout tilFirstname;
  private TextInputLayout tilLastname;
  private TextInputLayout tilMobile;
  private TextInputLayout tilEmail;
  private TextInputLayout tilPassword;
  private TextInputLayout tilcPassword;
  private Spinner etProfession;
  private EditText etFirstname;
  private EditText etLastname;
  private EditText etMobile;
  private EditText etEmail;
  private EditText etPassword;
  private EditText etcPassword;


  //RegisterViewModel loginViewModel;
  RegisterViewModel registerViewModel;
  /* ***********************************
   * SharedPreferences
   *********************************** */
  SharedPreferences loginSharedPreferences;

  public SignupFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_signup, container, false);
  }
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
    registerViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel.class);

    //loginSharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(LoginPreferencesConstants.PREF_NAME, Context.MODE_PRIVATE);

    initViews(view);
    hookViews();
    hookIntoViewModel();
  }

  private void initViews(View view) {
    btnRegister = view.findViewById(R.id.btnRegister);
    tilFirstname = view.findViewById(R.id.tilFirstname);
    tilLastname = view.findViewById(R.id.tilLastName);

    tilMobile = view.findViewById(R.id.tilMobile);
    tilEmail = view.findViewById(R.id.tilEmail);
    tilPassword = view.findViewById(R.id.tilPassword);
    tilcPassword= view.findViewById(R.id.tilCPassword);


    etFirstname = view.findViewById(R.id.etFirstname);
    etLastname = view.findViewById(R.id.etLastname);

    etMobile = view.findViewById(R.id.etMobile);
    etEmail = view.findViewById(R.id.etEmail);
    etPassword = view.findViewById(R.id.etPassword);
    etcPassword= view.findViewById(R.id.etCPassword);
  }

  private void hookViews() {
    btnRegister.setOnClickListener(
            v -> {
              // Check for username
              Log.d("btnRegister","btnRegister");
              if (validateFirstname() && validateLastname() && validateMobile() && validateEmail() && validatePassword()) {
                Log.d("btnRegisterin","btnRegister");
                Toast.makeText(getContext(), "Validated", Toast.LENGTH_SHORT).show();
                registerViewModel.startRegisterProcess(
                        etFirstname.getText().toString(),etLastname.getText().toString(),etMobile.getText().toString(),etEmail.getText().toString(), etPassword.getText().toString());
              }
              else if (etPassword.getText().toString()!=etcPassword.getText().toString()){
                Toast.makeText(getContext(), "Password and ConfirmPassword doesnot match", Toast.LENGTH_SHORT).show();
              }
            });
    /*tvForgotPassword.setOnClickListener(v -> {
      EventBus.getDefault().post(new NewLoginEvent(false));
      Toast.makeText(getContext().getApplicationContext(), "fired", Toast.LENGTH_SHORT).show();
    }); */
  }

  private void hookIntoViewModel() {
    registerViewModel
            .getRegisterStatus()
            .observe(
                    this,
                    aBoolean -> {
                      if (aBoolean) {
                        Toast.makeText(getContext().getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                        /*loginSharedPreferences.edit()
                                .putString(LoginPreferencesConstants.KEY_USERNAME, etUsername.getText().toString())
                                .putString(LoginPreferencesConstants.KEY_PASSWORD, etPassword.getText().toString())
                                .commit(); */

                        EventBus.getDefault().post(new NewLoginEvent(true));
                        getActivity().finish();
                      } else {
                        Toast.makeText(getContext().getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                      }
                    });
  }

  private boolean validateFirstname() {
    final String data = etFirstname.getText().toString();
    // Check if firstname is entered
    if (data.length() == 0) {
      if (!tilFirstname.isErrorEnabled()) {
        tilFirstname.setErrorEnabled(true);
      }
      tilFirstname.setError("FirstName Required");
      return false;
    } else {
      if (tilFirstname.isErrorEnabled()) {
        tilFirstname.setErrorEnabled(false);
      }
      return true;
    }
  }
  private boolean validateLastname() {
    final String data = etLastname.getText().toString();
    // Check if lastname is entered
    if (data.length() == 0) {
      if (!tilLastname.isErrorEnabled()) {
        tilLastname.setErrorEnabled(true);
      }
      tilLastname.setError("LastName Required");
      return false;
    } else {
      if (tilLastname.isErrorEnabled()) {
        tilLastname.setErrorEnabled(false);
      }
      return true;
    }
  }
  private boolean validateMobile() {
    final String data = etMobile.getText().toString();
    // Check if mobile is entered
    if (data.length() == 0) {
      if (!tilMobile.isErrorEnabled()) {
        tilMobile.setErrorEnabled(true);
      }
      tilMobile.setError("ContactNo Required");
      return false;
    } else {
      if (tilMobile.isErrorEnabled()) {
        tilMobile.setErrorEnabled(false);
      }
      return true;
    }
  }
  private boolean validateEmail() {
    final String data = etEmail.getText().toString();
    // Check if mobile is entered
    if (data.length() == 0) {
      if (!tilEmail.isErrorEnabled()) {
        tilEmail.setErrorEnabled(true);
      }
      tilEmail.setError("EmailId Required");
      return false;
    } else {
      if (tilEmail.isErrorEnabled()) {
        tilEmail.setErrorEnabled(false);
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
      tilPassword.setError("Password Required");
      return false;
    } else {
      if (tilPassword.isErrorEnabled()) {
        tilPassword.setErrorEnabled(false);
      }
      return true;
    }
  }

}
