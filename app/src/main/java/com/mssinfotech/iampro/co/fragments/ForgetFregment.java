package com.mssinfotech.iampro.co.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.R;
//import com.mssinfotech.iampro.co.events.NewLoginEvent;
//import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.viewmodels.ForgetViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class ForgetFregment extends Fragment {

    /* ***********************************
     * View References
     *********************************** */
    private Button btnForget;
    private TextInputLayout tilEmail;
    private EditText etEmail;

    /* ***********************************
     * ViewModels
     *********************************** */
    ForgetViewModel forgetViewModel;

    /* ***********************************
     * SharedPreferences
     *********************************** */
    SharedPreferences loginSharedPreferences;

    public ForgetFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fregment_forget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        forgetViewModel = ViewModelProviders.of(this, factory).get(ForgetViewModel.class);

        //loginSharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(LoginPreferencesConstants.PREF_NAME, Context.MODE_PRIVATE);

        initViews(view);
        hookViews();
        hookIntoViewModel();
    }

    private void initViews(View view) {
        btnForget = view.findViewById(R.id.btnForget);
        tilEmail = view.findViewById(R.id.tilEmail);
        etEmail = view.findViewById(R.id.etEmail);
    }

    private void hookViews() {
        btnForget.setOnClickListener(
                v -> {
                    // Check for username
                    if (validateUsername()) {
                        Toast.makeText(getContext(), "Validated", Toast.LENGTH_SHORT).show();
                        forgetViewModel.startForgetProcess(etEmail.getText().toString());
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

    private boolean validateUsername() {
        final String data = etEmail.getText().toString();
        // Check if username is entered
        if (data.length() == 0) {
            if (!tilEmail.isErrorEnabled()) {
                tilEmail.setErrorEnabled(true);
            }
            tilEmail.setError("Email Required");
            return false;
        } else {
            if (tilEmail.isErrorEnabled()) {
                tilEmail.setErrorEnabled(false);
            }
            return true;
        }
    }

}
