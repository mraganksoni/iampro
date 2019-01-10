package com.mssinfotech.iampro.co.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.ContextThemeWrapper;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class VolleyUtil {
    private static VolleyUtil mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    public static synchronized VolleyUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyUtil(context);
        }
        return mInstance;
    }

    private VolleyUtil(Context context) {
        this.mContext = context.getApplicationContext();
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 33554432);

        Network network = new BasicNetwork(new HurlStack());

        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    public <T> void addRequest(Request<T> request) {
        mRequestQueue.add(request);
    }

    public void stopRequestQueue() {
        mRequestQueue.stop();
    }
}
