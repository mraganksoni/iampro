package com.mssinfotech.iampro.co.viewmodels;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import okhttp3.Response;
import org.json.JSONObject;

public class JoinFriendViewModel extends AndroidViewModel {
  private static final String TAG = "JoinFriendViewModel";

  /* ******************************
   * SharedPreferences
   ****************************** */
  SharedPreferences loginSharedPreferences;

  /* ******************************
   * State Tracking objects
   ****************************** */
  UserDetails userDetails;

  /* ******************************
   * MUTEABLE LIVEDATA OBJECTS
   ****************************** */
  private MutableLiveData<List<JSONObject>> _allFriendsList = new MutableLiveData<>();

  public JoinFriendViewModel(
      @NonNull Application application) {
    super(application);

    loadDataFromSp();
  }
  private void loadDataFromSp() {
    loginSharedPreferences =
        getApplication().getSharedPreferences(LoginPreferencesConstants.PREF_NAME, MODE_PRIVATE);
    final String userDetailsStr =
        loginSharedPreferences.getString(LoginPreferencesConstants.KEY_USER_INFO, "");
    if (userDetailsStr.isEmpty()) {
      Log.d(TAG, "loadDataFromSp: No data found in shared preference something wrong");
      return;
    }

    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    try {
      userDetails = gson.fromJson(userDetailsStr, UserDetails.class);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    }
  }


  public void loadAllFriendsList() {
    AndroidNetworking
        .get("http://www.iampro.co/api/ajax.php")
        .addQueryParameter("type", "view_friend_list")
        .addQueryParameter("status", "2")
        .addQueryParameter("id", String.valueOf(userDetails.getId()))
        .build()
        .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
          @SuppressLint("CheckResult")
          @Override
          public void onResponse(Response okHttpResponse, JSONObject response) {
            Single.fromCallable((Callable<List<JSONObject>>) () -> {
              List<JSONObject> list = new ArrayList<>();
              for (Iterator<String> rootIterator = response.keys(); rootIterator.hasNext(); ){
                list.add(response.optJSONObject(rootIterator.next()));
              }
              return list;
            })
                .observeOn(Schedulers.computation())
                .subscribe(_allFriendsList::postValue);
          }

          @Override
          public void onError(ANError anError) {
            Log.e(TAG, "Failed to load all friends list", anError);
          }
        });
  }

  public LiveData<List<JSONObject>> getFriendsList() { return _allFriendsList; }
}
