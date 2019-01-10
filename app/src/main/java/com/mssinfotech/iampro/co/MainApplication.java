package com.mssinfotech.iampro.co;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.nio.file.Files;

import com.mssinfotech.iampro.co.utils.VolleyUtil;

public class MainApplication extends Application {

    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Leak Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //mRefWatcher = LeakCanary.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        VolleyUtil.getInstance(this).stopRequestQueue();
    }

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }
}
