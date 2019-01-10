package com.mssinfotech.iampro.co;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(FirstActivity.this, DashboardActivity.class);
                FirstActivity.this.startActivity(mainIntent);
                FirstActivity.this.finish();
            }
        }, 3000);
    }
}
