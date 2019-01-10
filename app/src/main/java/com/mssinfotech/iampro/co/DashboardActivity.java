package com.mssinfotech.iampro.co;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mssinfotech.iampro.co.activities.LoginActivity;
import com.mssinfotech.iampro.co.activities.MainActivity;
import com.mssinfotech.iampro.co.fragments.SignupFragment;

public class DashboardActivity extends Activity implements View.OnClickListener {

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

                Intent i_login = new Intent(DashboardActivity.this,LoginActivity.class);
                DashboardActivity.this.startActivity(i_login);
                break;
            case R.id.imgsignup:

                Intent i_signup = new Intent(DashboardActivity.this,SignupFragment.class);
                DashboardActivity.this.startActivity(i_signup);
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
}
