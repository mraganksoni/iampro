package com.mssinfotech.iampro.co.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.mssinfotech.iampro.co.models.CurrentUser;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.utils.VolleyUtil;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginViewModel extends AndroidViewModel {
  /* ******************************************
   * Constants
   ****************************************** */
  private static final String TAG = "LoginViewModel";

  /* ******************************************
   * Mutable Live Data references
   ****************************************** */
  private final MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();

  public LoginViewModel(@NonNull Application application) {
    super(application);
  }

  @SuppressLint("CheckResult")
  public void startLoginProcess(String username, String password) {
    final String url =
        String.format(
            "http://www.iampro.co/ajax/signup.php?type=login&username=%s&pass=%s",
            username, password);
    final JsonObjectRequest request =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            (JSONObject response) -> {
              try {
                final String status = response.getString("status");
                if (status.equalsIgnoreCase("error")) {
                  loginStatus.setValue(false);
                } else if (status.equalsIgnoreCase("success")) {
                  final int userId = response.getInt("id");
                  getFullUserDetails(userId);
                  loginStatus.setValue(true);
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

  public LiveData<Boolean> getLoginStatus() {
    return loginStatus;
  }
}
