package com.mssinfotech.iampro.co.activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.databinding.ActivityImageGalleryBinding;
import com.mssinfotech.iampro.co.databinding.ActivityJoinFriendBinding;
import com.mssinfotech.iampro.co.fragments.AllFriendsFragment;
import com.mssinfotech.iampro.co.fragments.ImageGalleryAlbumFragment;
import com.mssinfotech.iampro.co.fragments.ImageGalleryFragment;
import com.mssinfotech.iampro.co.fragments.PendingFriendsFragment;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.json.JSONObject;

public class ImageGalleryActivity extends AppCompatActivity {
  private static final String TAG = "ImageGalleryActivity";

  /* *************************
   * CONSTANTS
   ************************* */
  public static final String ACTION_IMAGE_GALLERY = "action_image_gallery";

  /* *************************
   * BINDINGS
   ************************* */
  ActivityImageGalleryBinding binding;

  /* ******************************
   * SharedPreferences
   ****************************** */
  SharedPreferences loginSharedPreferences;

  /* ******************************
   * State Tracking objects
   ****************************** */
  UserDetails userDetails;

  /* ******************************
   * FRAGMENTS
   ****************************** */
  ImageGalleryFragment imageGalleryFragment;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_image_gallery);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    loadDataFromSp();
    showImageGalleryFragment();
  }

  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
      getSupportFragmentManager().popBackStack();
    } else {
      super.onBackPressed();
    }
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

  private void showImageGalleryFragment() {
    if (imageGalleryFragment == null) {
      imageGalleryFragment = ImageGalleryFragment.newInstance(userDetails.getId());
    }

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragmentContainer, imageGalleryFragment)
        .commit();
  }

  public void showAlbumFragment(JSONObject details) {

    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out )
        .replace(R.id.fragmentContainer, ImageGalleryAlbumFragment.newInstance(details))
        .addToBackStack(null)
        .commit();
  }
}
