package com.mssinfotech.iampro.co.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mssinfotech.iampro.co.databinding.ActivityUserDetailsBinding;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.utils.VolleyUtil;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;

import com.mssinfotech.iampro.co.R;
import com.squareup.picasso.Picasso;
import org.json.JSONException;

public class UserDetailsActivity extends AppCompatActivity {
  private static final String TAG = "UserDetailsActivity";

  public static final String ACTION_SHOW_USER = "action_show_user";
  public static final String ACTION_MY_PROFILE = "action_my_profile";
  public static final String EXTRA_USER_DATA = "extra_user_data";
  public static final String EXTRA_AVATAR_TRANSITION_NAME = "extra_avatar_transition_name";
  public static final String EXTRA_FULL_NAME_TRANSITION_NAME = "extra_full_name_transition_name";

  /* *********************************
   * Binding References
   ********************************* */
  ActivityUserDetailsBinding binding;

  /* *********************************
   * Shared Preferences
   ********************************* */
  SharedPreferences loginSharedPreferences;

  private boolean isAvatarLoaded = false;
  private boolean isBannerLoaded = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details);
    setSupportActionBar(binding.toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    setupViews();

    final Intent intent = getIntent();

    // Setting shared elements transition names
    if (intent.hasExtra(EXTRA_AVATAR_TRANSITION_NAME)) {
      binding.civAvatar.setTransitionName(intent.getStringExtra(EXTRA_AVATAR_TRANSITION_NAME));
    }
    if (intent.hasExtra(EXTRA_FULL_NAME_TRANSITION_NAME)) {
      binding.tvFullName.setTransitionName(EXTRA_FULL_NAME_TRANSITION_NAME);
    }

    processIntent(intent);
  }

  private void setupViews() {
    // Changing tint of all button icons
    binding.btnImages.getCompoundDrawables()[1].setTint(Color.BLACK);
    binding.btnVideos.getCompoundDrawables()[1].setTint(Color.BLACK);
    binding.btnProducts.getCompoundDrawables()[1].setTint(Color.BLACK);
    binding.btnFriends.getCompoundDrawables()[1].setTint(Color.BLACK);
    binding.btnProvide.getCompoundDrawables()[1].setTint(Color.BLACK);
    binding.btnDemand.getCompoundDrawables()[1].setTint(Color.BLACK);
  }

  private void processIntent(Intent intent) {
    final String action = intent.getAction();
    if (action != null && !action.isEmpty()) {
      switch (action) {
        case ACTION_SHOW_USER:
          if (intent.hasExtra(EXTRA_USER_DATA)) {
            UserDetails userDetails = intent.getParcelableExtra(EXTRA_USER_DATA);
            if (userDetails != null) {
              displayUserData(userDetails);
            }
          }
          break;
        case ACTION_MY_PROFILE:
          if (loginSharedPreferences == null)
            loginSharedPreferences =
                getSharedPreferences(LoginPreferencesConstants.PREF_NAME, MODE_PRIVATE);

          final String userDetailsStr =
              loginSharedPreferences.getString(LoginPreferencesConstants.KEY_USER_INFO, "");
          if (userDetailsStr.isEmpty()) {
            finish();
            return;
          }
          binding.llMessageAddFriend.setVisibility(View.GONE);

          Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
          UserDetails userDetails = gson.fromJson(userDetailsStr, UserDetails.class);
          displayUserData(userDetails);
          break;
        default:
          Log.d(TAG, "processIntent: Unknown intent action found");
      }
    }
  }

  private void displayUserData(UserDetails userDetails) {
    // Setting banner image
    Picasso.get()
        .load(ApiEndpoints.DIR_AVATAR + userDetails.getBannerImage())
        .fit()
        .centerCrop(Gravity.CENTER)
        .into(binding.ivBanner);

    // Setting user image
    Picasso.get()
        .load(ApiEndpoints.DIR_AVATAR + userDetails.getAvatar())
        .fit()
        .centerCrop(Gravity.CENTER)
        .into(binding.civAvatar);

    // Setting name of user
    binding.tvFullName.setText(userDetails.getFullName());

    // getting counts of images, videos, products, friends, provides and demands
    JsonObjectRequest imagesCountRequest =
        new JsonObjectRequest(
            "http://www.iampro.co/api/ajax.php?type=total_item&data_type=image&uid="
                + userDetails.getId(),
            null,
            response -> {
              if (response.has("total_count")) {
                try {
                  final String count = response.getString("total_count");
                  final String text = count + " Images";
                  binding.btnImages.setText(text);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            error -> {
              Log.e(TAG, "imagesCountRequest : Failed", error.getCause());
            });

    JsonObjectRequest videosCountRequest =
        new JsonObjectRequest(
            "http://www.iampro.co/api/ajax.php?type=total_item&data_type=video&uid="
                + userDetails.getId(),
            null,
            response -> {
              if (response.has("total_count")) {
                try {
                  final String count = response.getString("total_count");
                  final String text = count + " Videos";
                  binding.btnVideos.setText(text);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            error -> {
              Log.e(TAG, "videosCountRequest : Failed", error.getCause());
            });

    JsonObjectRequest productCountRequest =
        new JsonObjectRequest(
            "http://www.iampro.co/api/ajax.php?type=total_item&data_type=product&uid="
                + userDetails.getId(),
            null,
            response -> {
              if (response.has("total_count")) {
                try {
                  final String count = response.getString("total_count");
                  final String text = count + " Products";
                  binding.btnProducts.setText(text);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            error -> {
              Log.e(TAG, "productCountRequest : Failed", error.getCause());
            });

    JsonObjectRequest friendCountRequest =
        new JsonObjectRequest(
            "http://www.iampro.co/api/ajax.php?type=total_friend&uid=" + userDetails.getId(),
            null,
            response -> {
              if (response.has("total_count")) {
                try {
                  final String count = response.getString("total_count");
                  final String text = count + " Friends";
                  binding.btnFriends.setText(text);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            error -> {
              Log.e(TAG, "friendCountRequest : Failed", error.getCause());
            });

    JsonObjectRequest provideCountRequest =
        new JsonObjectRequest(
            "http://www.iampro.co/api/ajax.php?type=total_item&data_type=PROVIDE&uid="
                + userDetails.getId(),
            null,
            response -> {
              if (response.has("total_count")) {
                try {
                  final String count = response.getString("total_count");
                  final String text = count + " Provides";
                  binding.btnProvide.setText(text);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            error -> {
              Log.e(TAG, "provideCountRequest : Failed", error.getCause());
            });

    JsonObjectRequest demandCountRequest =
        new JsonObjectRequest(
            "http://www.iampro.co/api/ajax.php?type=total_item&data_type=DEMAND&uid="
                + userDetails.getId(),
            null,
            response -> {
              if (response.has("total_count")) {
                try {
                  final String count = response.getString("total_count");
                  final String text = count + " Demand";
                  binding.btnDemand.setText(text);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            error -> {
              Log.e(TAG, "demandCountRequest : Failed", error.getCause());
            });

    VolleyUtil.getInstance(getApplicationContext()).addRequest(imagesCountRequest);
    VolleyUtil.getInstance(getApplicationContext()).addRequest(videosCountRequest);
    VolleyUtil.getInstance(getApplicationContext()).addRequest(productCountRequest);
    VolleyUtil.getInstance(getApplicationContext()).addRequest(friendCountRequest);
    VolleyUtil.getInstance(getApplicationContext()).addRequest(provideCountRequest);
    VolleyUtil.getInstance(getApplicationContext()).addRequest(demandCountRequest);
  }
}
