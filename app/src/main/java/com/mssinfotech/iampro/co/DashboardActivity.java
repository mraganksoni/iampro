package com.mssinfotech.iampro.co;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.mssinfotech.iampro.co.activities.LoginActivity;
import com.mssinfotech.iampro.co.activities.MainActivity;
import com.mssinfotech.iampro.co.fragments.LoginFragment;
import com.mssinfotech.iampro.co.fragments.SignupFragment;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    LoginFragment loginFragment;
    SignupFragment signupFragment;

    ImageView btnsignin,btnsignup,btnhome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btnsignin = (ImageView)findViewById(R.id.imglogin);
        btnsignup = (ImageView)findViewById(R.id.imgsignup);
        btnhome = (ImageView)findViewById(R.id.imghome);

        btnsignin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        btnhome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.imglogin:
                showLoginFragment();
                break;
            case R.id.imgsignup:
                showSignupFragment();
                break;
            case R.id.imghome:
                Intent i_home = new Intent(DashboardActivity.this,MainActivity.class);
                DashboardActivity.this.startActivity(i_home);
                DashboardActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void showLoginFragment() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            Transition modeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
            Transition fadeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.fade);
            loginFragment.setSharedElementEnterTransition(modeTransition);
            loginFragment.setSharedElementReturnTransition(modeTransition);
            loginFragment.setEnterTransition(fadeTransition);
            loginFragment.setExitTransition(fadeTransition);
        }
        //final View sharedImageView = homeFragment.getView().findViewById(R.id.imageView);
        //final View sharedLoginButton = homeFragment.getView().findViewById(R.id.btnLogin);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, loginFragment, "LOGIN")
                .setTransition(android.R.transition.move)
                .addToBackStack("LOGIN")
                .commit();
    }

    private void showSignupFragment() {
        if (signupFragment == null) {
            signupFragment = new SignupFragment();
            Transition modeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
            Transition fadeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.fade);
            signupFragment.setSharedElementEnterTransition(modeTransition);
            signupFragment.setSharedElementReturnTransition(modeTransition);
            signupFragment.setEnterTransition(fadeTransition);
            signupFragment.setExitTransition(fadeTransition);

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, signupFragment, "SIGNUP")
                .setTransition(android.R.transition.move)
                .addToBackStack("SIGNUP")
                .commit();
    }
}
