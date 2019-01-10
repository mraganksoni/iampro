package com.mssinfotech.iampro.co.activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.databinding.ActivityChangePasswordBinding;
import com.mssinfotech.iampro.co.databinding.ActivityJoinFriendBinding;
import com.mssinfotech.iampro.co.fragments.AllFriendsFragment;
import com.mssinfotech.iampro.co.fragments.PendingFriendsFragment;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class JoinFriendActivity extends AppCompatActivity {
  private static final String TAG = "JoinFriendActivity";

  /* *************************
   * BINDINGS
   ************************* */
  ActivityJoinFriendBinding binding;

  /* ******************************
   * SharedPreferences
   ****************************** */
  SharedPreferences loginSharedPreferences;

  /* ******************************
   * State Tracking objects
   ****************************** */
  UserDetails userDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_join_friend);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    hookViews();
    loadDataFromSp();
  }

  private void hookViews() {
    binding.vpMain.setAdapter(
        new FragmentPagerAdapter(getSupportFragmentManager()) {
          @Override
          public Fragment getItem(int position) {
            switch (position) {
              case 0:
                return new AllFriendsFragment();
              case 1:
                return new PendingFriendsFragment();
              default:
                return null;
            }
          }

          @Override
          public int getCount() {
            return 2;
          }

          @Nullable
          @Override
          public CharSequence getPageTitle(int position) {
            switch (position) {
              case 0:
                return "All Friends";
              case 1:
                return "Pending Friends";
              default:
                return super.getPageTitle(position);
            }
          }
        });

    binding.tlMain.setupWithViewPager(binding.vpMain);
  }

  private void loadDataFromSp() {
    loginSharedPreferences =
        getSharedPreferences(LoginPreferencesConstants.PREF_NAME, MODE_PRIVATE);
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
}
