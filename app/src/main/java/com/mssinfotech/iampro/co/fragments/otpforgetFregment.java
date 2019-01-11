package com.mssinfotech.iampro.co.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.viewmodels.ForgetViewModel;

public class OTPforgetFregment extends Fragment {
    public String OTP,Email;
    /* ***********************************
     * View References
     *********************************** */
    private Button btnOTPForget;
    private TextInputLayout tilOTP;
    private EditText etOTP;

    private TextInputLayout tilNewPassword;
    private EditText etNewPassword;

    private TextInputLayout tilConformPassword;
    private EditText etConformPassword;


    /* ***********************************
     * ViewModels
     *********************************** */
    ForgetViewModel forgetViewModel;

    /* ***********************************
     * SharedPreferences
     *********************************** */
    SharedPreferences loginSharedPreferences;

    public otpforgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otpforget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        forgetViewModel = ViewModelProviders.of(this, factory).get(ForgetViewModel.class);
        Bundle bundle=getArguments();

        //loginSharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(LoginPreferencesConstants.PREF_NAME, Context.MODE_PRIVATE);
        OTP = bundle.getString("vcode");
        Email = bundle.getString("email");
        initViews(view);
        hookViews();
        hookIntoViewModel();
    }

    private void initViews(View view) {
        btnOTPForget = view.findViewById(R.id.btnOTPForget);
        tilOTP = view.findViewById(R.id.tilOTP);
        etOTP = view.findViewById(R.id.etOTP);

        tilNewPassword = view.findViewById(R.id.tilNewPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);

        tilConformPassword = view.findViewById(R.id.tilConformPassword);
        etConformPassword = view.findViewById(R.id.etConformPassword);
    }

    private void hookViews() {
        btnOTPForget.setOnClickListener(
                v -> {
                    // Check for username
                    if(!OTP.equals(etOTP.getText().toString())){
                        Toast.makeText(getContext(), "Validated", Toast.LENGTH_SHORT).show();
                    }
                    if (validateNewPassword() && validateConformPassword() && validateOTP())
                     {
                        Toast.makeText(getContext(), "Validated", Toast.LENGTH_SHORT).show();
                        forgetViewModel.startOTPForgetProcess(Email,etNewPassword.getText().toString(),etConformPassword.getText().toString(),etOTP.getText().toString());
                    }
                });
    }
    private void hookIntoViewModel() {
        forgetViewModel
                .getForgetStatus()
                .observe(
                        this,
                        aBoolean -> {
                            if (aBoolean) {
                                Toast.makeText(getContext().getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext().getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private boolean validateNewPassword() {
        final String data = etNewPassword.getText().toString();
        String numbers = "(.*[0-9].*)";
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        // Check if username is entered
        if (data == "") {
            if (!tilNewPassword.isErrorEnabled()) {
                tilNewPassword.setErrorEnabled(true);
            }
            tilNewPassword.setError("Please enter New Password");
            return false;
        }else if (data.length() < 4) {
            if (!tilNewPassword.isErrorEnabled()) {
                tilNewPassword.setErrorEnabled(true);
            }
            tilNewPassword.setError("Password must contant minimum one number and one charecter and minimum length of password  5 ");
            return false;
        }else if (!data.matches(numbers )) {
            if (!tilNewPassword.isErrorEnabled()) {
                tilNewPassword.setErrorEnabled(true);
            }
            tilNewPassword.setError("Password must contant minimum one number and one charecter and minimum length of password  5 ");
            return false;
        }else if ((!data.matches(upperCaseChars )) || (!data.matches(lowerCaseChars))) {
            if (!tilNewPassword.isErrorEnabled()) {
                tilNewPassword.setErrorEnabled(true);
            }
            tilNewPassword.setError("Password must contant minimum one number and one charecter and minimum length of password  5 ");
            return false;
        }
        return true;
    }

    private boolean validateConformPassword() {
        final String data = etConformPassword.getText().toString();
        final String newdata = etNewPassword.getText().toString();
        String numbers = "(.*[0-9].*)";
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        // Check if username is entered
        if (data != newdata) {
            if (!tilConformPassword.isErrorEnabled()) {
                tilConformPassword.setErrorEnabled(true);
            }
            tilConformPassword.setError("New Password And Conform Passwor must be same");
            return false;
        }
        return true;
    }

    private boolean validateOTP() {
        final String data = etOTP.getText().toString();

        // Check if username is entered
        if (data != OTP) {
            if (!tilOTP.isErrorEnabled()) {
                tilOTP.setErrorEnabled(true);
            }
            tilOTP.setError("Security code invalid");
            return false;
        }
        return true;
    }
}
