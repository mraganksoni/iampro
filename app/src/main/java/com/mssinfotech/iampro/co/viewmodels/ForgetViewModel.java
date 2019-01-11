package com.mssinfotech.iampro.co.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.utils.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetViewModel extends AndroidViewModel {
    /* ******************************************
     * Constants
     ****************************************** */
    private static final String TAG = "ForgetViewModel";

    /* ******************************************
     * Mutable Live Data references
     ****************************************** */
    private final MutableLiveData<Boolean> forgetStatus = new MutableLiveData<>();

    public ForgetViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public  void startForgetProcess(String email) {
        final String url =
                String.format(
                        "http://www.iampro.co/api/app_service.php?type=ForgetPassword&email=%s&process_type=android",
                        email);
        final JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        (JSONObject response) -> {
                            try {
                                final String status = response.getString("status");
                                if (status.equalsIgnoreCase("error")) {
                                    forgetStatus.setValue(false);
                                } else if (status.equalsIgnoreCase("success")) {
                                    final String vcode = response.getString("vcode");
                                    redirectToOTPForget(email,vcode);
                                    //getFullUserDetails(userId);
                                    forgetStatus.setValue(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Log.e(TAG, "startLoginProcess: a", error.getCause());
                        });
        VolleyUtil.getInstance(getApplication()).addRequest(request);
    }
    private void redirectToOTPForget(String email,String vcode){

    }
    /*
    private void getFullUserDetails(int userId) {
        final String url = "http://www.iampro.co/api/ajax.php?type=get_users_all_detail&uid=" + userId;
        final JsonObjectRequest userDetailsRequest = new JsonObjectRequest(url, null, response -> {
            SharedPreferences loginSp =
                    getApplication()
                            .getSharedPreferences(
                                    LoginPreferencesConstants.PREF_NAME, Context.MODE_PRIVATE);
            loginSp
                    .edit()
                    .putString(LoginPreferencesConstants.KEY_USER_INFO, response.toString())
                    .commit();
        }, error -> {
            Log.e(TAG, "getFullUserDetails: ", error.getCause());
        });

        VolleyUtil.getInstance(getApplication()).addRequest(userDetailsRequest);
    }
    */
    public LiveData<Boolean> getForgetStatus() {
        return forgetStatus;
    }
}
