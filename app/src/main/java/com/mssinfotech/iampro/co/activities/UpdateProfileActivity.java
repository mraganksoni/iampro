package com.mssinfotech.iampro.co.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.ParcelFileDescriptor;
import android.provider.CalendarContract.CalendarAlerts;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.databinding.ActivityUpdateProfileBinding;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.viewmodels.SearchViewModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import okhttp3.Response;
import org.json.JSONObject;

public class UpdateProfileActivity extends AppCompatActivity {
  /* ******************************
   * Constants
   ****************************** */
  public static final String TAG = "UpdateProfileActivity";
  public static final String ACTION_UPDATE_PROFILE = "action_update_profile";
  private static final int REQ_CODE_BANNER_IMAGE = 50001;
  private static final int REQ_CODE_PROFILE_IMAGE = 50002;

  /* ******************************
   * Dialogs
   ****************************** */
  AlertDialog.Builder categoryDialog;
  DatePickerDialog datePickerDialog = null;

  /* ******************************
   * Adapters
   ****************************** */
  ArrayAdapter<String> categoryAdapter;

  /* ******************************
   * Bindings
   ****************************** */
  ActivityUpdateProfileBinding binding;

  /* ******************************
   * ViewModels
   ****************************** */
  SearchViewModel searchViewModel;

  /* ******************************
   * SharedPreferences
   ****************************** */
  SharedPreferences loginSharedPreferences;

  /* ******************************
   * State Tracking objects
   ****************************** */
  UserDetails userDetails;
  private String dob;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    AndroidViewModelFactory factory = new AndroidViewModelFactory(getApplication());
    searchViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel.class);

    loadDataFromSp();
    initObjects();
    initViews();
    updateUi();
    processIntent(getIntent());
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

  private void initObjects() {
    categoryDialog = new Builder(this);
    categoryDialog.setNegativeButton("Cancel", null);
    categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);

    searchViewModel
        .getFriendSubCategories()
        .observe(
            this,
            strings -> {
              if (strings != null) {
                int selectedIndex = -1;
                if (strings.contains(userDetails.getCategory()))
                  selectedIndex = strings.indexOf(userDetails.getCategory());
                categoryDialog.setSingleChoiceItems(
                    strings.toArray(new String[0]),
                    selectedIndex,
                    (dialog, which) -> {
                      if (userDetails != null) {
                        userDetails.setCategory(strings.get(which));
                        binding.tvCategoryTitle.setText(userDetails.getCategory());
                        Log.d(TAG, "Selected category " + strings.get(which));
                      }
                      dialog.dismiss();
                    });
              }
            });
  }

  private void initViews() {
    // Setting onClick listeners on buttons
    // Image Button Banner
    binding.ibtnChangeBannerImage.setOnClickListener(
        v -> {
          ImagePicker.create(this)
              .single()
              .showCamera(false)
              .returnMode(ReturnMode.GALLERY_ONLY)
              .includeVideo(false)
              .start(REQ_CODE_BANNER_IMAGE);
        });

    // Image Button Avatar
    binding.ibtnChangeProfileImage.setOnClickListener(
        v -> {
          ImagePicker.create(this)
              .single()
              .showCamera(false)
              .returnMode(ReturnMode.GALLERY_ONLY)
              .includeVideo(false)
              .start(REQ_CODE_PROFILE_IMAGE);
        });

    // Category label Click
    binding.flCategory.setOnClickListener(
        v -> {
          categoryDialog.show();
        });

    // Button update click listener
    binding.btnUpdate.setOnClickListener(
        v -> {
          if (validateFields()) {
            uploadProfileData();
          }
        });

    // Button select date
    binding.ibtnDate.setOnClickListener(
        v -> {
          if (datePickerDialog == null) {
            final Calendar calendar = Calendar.getInstance();
            datePickerDialog =
                new DatePickerDialog(
                    this,
                    new OnDateSetListener() {
                      @Override
                      public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob = year + "-" + (month + 1) + "-" + dayOfMonth;
                        userDetails.setDob(dob);
                        binding.tietDate.setText(dob);
                      }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
          }
          datePickerDialog.show();
        });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQ_CODE_BANNER_IMAGE:
        if (resultCode == Activity.RESULT_OK) {
          processBannerImageActivityResult(data);
        } else {
          Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }
        break;
      case REQ_CODE_PROFILE_IMAGE:
        if (resultCode == Activity.RESULT_OK) {
          processProfileImageActivityResult(data);
        } else {
          Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

  private void updateUi() {
    // Filling banner background
    Picasso.get()
        .load(ApiEndpoints.DIR_AVATAR + userDetails.getBannerImage())
        .fit()
        .centerInside()
        .into(binding.ivBanner);

    // Filling avatar image
    Picasso.get()
        .load(ApiEndpoints.DIR_AVATAR + userDetails.getAvatar())
        .fit()
        .centerInside()
        .into(binding.civAvatar);

    // Category labels
    binding.tvCategoryTitle.setText(userDetails.getCategory());
    // Username
    binding.tietUsername.setText(userDetails.getUsername());
    // FirstName
    binding.tietFirstName.setText(userDetails.getFirstName());
    // LastName
    binding.tietLastName.setText(userDetails.getLastName());
    // Mobile
    binding.tietMobile.setText(userDetails.getMobile());
    // Email
    binding.tietEmail.setText(userDetails.getEmail());
    // Date of Birth
    binding.tietDate.setText(userDetails.getDob());
    // Identity
    binding.tietIdentityNumber.setText(userDetails.getIdentityNumber());
    // About Me
    binding.tietAboutMe.setText(userDetails.getAboutMe());
    // TagLine
    binding.tietTagLine.setText(userDetails.getTagLine());
    // Address
    binding.tietAddress.setText(userDetails.getAddress());
    // City
    binding.tietCity.setText(userDetails.getCity());
    // State
    binding.tietState.setText(userDetails.getState());
    // Country
    binding.tietCountry.setText(userDetails.getCountry());

    Log.d(
        TAG,
        "updateUi: Banner image url: " + ApiEndpoints.DIR_AVATAR + userDetails.getBannerImage());
    Log.d(TAG, "updateUi: Avatar image url: " + ApiEndpoints.DIR_AVATAR + userDetails.getAvatar());
  }

  private void processIntent(Intent intent) {
    // If intent is null we cant process data then just finish this
    if (intent == null) {
      Log.d(TAG, "processIntent: null intent found exiting method");
      finish();
      return;
    }

    // Check if intent contains action
    // if it doesn't have action then we wont process data
    if (intent.getAction() == null) {
      Log.d(TAG, "processIntent: Action not provided exiting method");
      finish();
      return;
    } else {
      if (intent.getAction().isEmpty()) {
        Log.d(TAG, "processIntent: Empty action provided exiting method");
        finish();
        return;
      }
    }

    final String action = intent.getAction();
    switch (action) {
      case ACTION_UPDATE_PROFILE:
        break;
      default:
        Log.d(TAG, "processIntent: Unhadled intent action found");
        finish();
        return;
    }
  }

  private boolean validateFields() {
    boolean flag = true;

    // Username
    if (binding.tietUsername.getText().toString().isEmpty()) {
      flag = false;
      binding.tilUsername.setError("UserName Required");
      binding.tilUsername.setErrorEnabled(true);
    } else {
      binding.tilUsername.setErrorEnabled(false);
    }
    // FirstName
    if (binding.tietFirstName.getText().toString().isEmpty()) {
      flag = false;
      binding.tilFirstName.setError("First Name Required");
      binding.tilFirstName.setErrorEnabled(true);
    } else {
      binding.tilFirstName.setErrorEnabled(false);
    }
    // LastName
    if (binding.tietLastName.getText().toString().isEmpty()) {
      flag = false;
      binding.tilLastName.setError("Last Name Required");
      binding.tilLastName.setErrorEnabled(true);
    } else {
      binding.tilLastName.setErrorEnabled(false);
    }
    // Mobile
    if (binding.tietMobile.getText().toString().isEmpty()) {
      flag = false;
      binding.tilMobile.setError("Mobile Number Required");
      binding.tilMobile.setErrorEnabled(true);
    } else {
      binding.tilMobile.setErrorEnabled(false);
    }
    // Email
    if (binding.tietEmail.getText().toString().isEmpty()) {
      flag = false;
      binding.tilEmail.setError("Email Required");
      binding.tilEmail.setErrorEnabled(true);
    } else {
      binding.tilEmail.setErrorEnabled(false);
    }
    // Date of Birth
    if (binding.tietDate.getText().toString().isEmpty()) {
      flag = false;
      binding.tilDate.setError("Date of Birth Required");
      binding.tilDate.setErrorEnabled(true);
    } else {
      binding.tilDate.setErrorEnabled(false);
    }
    // Identity
    if (binding.tietIdentityNumber.getText().toString().isEmpty()) {
      flag = false;
      binding.tilIdentityNumber.setError("Identity Number Required");
      binding.tilIdentityNumber.setErrorEnabled(true);
    } else {
      binding.tilIdentityNumber.setErrorEnabled(false);
    }
    // About Me
    if (binding.tietAboutMe.getText().toString().isEmpty()) {
      flag = false;
      binding.tilAboutMe.setError("About Me Required");
      binding.tilAboutMe.setErrorEnabled(true);
    } else {
      binding.tilAboutMe.setErrorEnabled(false);
    }
    // TagLine
    if (binding.tietTagLine.getText().toString().isEmpty()) {
      flag = false;
      binding.tilTagLine.setError("TagLine Required");
      binding.tilTagLine.setErrorEnabled(true);
    } else {
      binding.tilTagLine.setErrorEnabled(false);
    }
    // Address
    if (binding.tietAddress.getText().toString().isEmpty()) {
      flag = false;
      binding.tilAddress.setError("Address Required");
      binding.tilAddress.setErrorEnabled(true);
    } else {
      binding.tilAddress.setErrorEnabled(false);
    }
    // City
    if (binding.tietCity.getText().toString().isEmpty()) {
      flag = false;
      binding.tilCity.setError("City Required");
      binding.tilCity.setErrorEnabled(true);
    } else {
      binding.tilCity.setErrorEnabled(false);
    }
    // State
    if (binding.tietState.getText().toString().isEmpty()) {
      flag = false;
      binding.tilState.setError("State Required");
      binding.tilState.setErrorEnabled(true);
    } else {
      binding.tilState.setErrorEnabled(false);
    }
    // Country
    if (binding.tietCountry.getText().toString().isEmpty()) {
      flag = false;
      binding.tilCountry.setError("Country Required");
      binding.tilCountry.setErrorEnabled(true);
    } else {
      binding.tilCountry.setErrorEnabled(false);
    }

    return flag;
  }

  private void processBannerImageActivityResult(Intent data) {
    Image imageMeta = ImagePicker.getFirstImageOrNull(data);
    if (imageMeta == null) {
      Log.e(
          TAG,
          "processBannerImageActivityResult: can't retrived data from intent, Image data null");
      return;
    }

    File file = new File(imageMeta.getPath());

    AndroidNetworking.upload("http://www.iampro.co/ajax/signup.php")
        .addMultipartParameter("type", "banner_img")
        .addMultipartParameter("process_type", "android")
        .addMultipartParameter("page_url", "page/update_profile.html")
        .addMultipartParameter("userid", String.valueOf(userDetails.getId()))
        .addMultipartFile("banner_img", file)
        .setPriority(Priority.HIGH)
        .build()
        .setUploadProgressListener((bytesUploaded, totalBytes) -> {})
        .getAsOkHttpResponseAndString(
            new OkHttpResponseAndStringRequestListener() {
              @Override
              public void onResponse(Response okHttpResponse, String response) {
                updateUi();
              }

              @Override
              public void onError(ANError anError) {
                Toast.makeText(
                        getApplicationContext(), "Banner image upload failed", Toast.LENGTH_SHORT)
                    .show();
              }
            });
  }

  private void processProfileImageActivityResult(Intent data) {
    Image imageMeta = ImagePicker.getFirstImageOrNull(data);
    if (imageMeta == null) {
      Log.e(
          TAG,
          "processProfileImageActivityResult: can't retrived data from intent, Image data null");
      return;
    }

    File file = new File(imageMeta.getPath());

    AndroidNetworking.upload("http://www.iampro.co/ajax/signup.php")
        .addMultipartParameter("type", "profile_pic")
        .addMultipartParameter("process_type", "android")
        .addMultipartParameter("page_url", "page/update_profile.html")
        .addMultipartParameter("userid", String.valueOf(userDetails.getId()))
        .addMultipartFile("avatar", file)
        .setPriority(Priority.HIGH)
        .build()
        .setUploadProgressListener((bytesUploaded, totalBytes) -> {})
        .getAsOkHttpResponseAndString(
            new OkHttpResponseAndStringRequestListener() {
              @Override
              public void onResponse(Response okHttpResponse, String response) {
                updateUi();
              }

              @Override
              public void onError(ANError anError) {
                Toast.makeText(
                        getApplicationContext(), "Profile image upload failed", Toast.LENGTH_SHORT)
                    .show();
              }
            });
  }

  private void uploadProfileData() {
    AndroidNetworking.get("http://www.iampro.co/ajax/signup.php")
        .addQueryParameter("type", "update_profile")
        .addQueryParameter("uid", String.valueOf(userDetails.getId()))
        .addQueryParameter("fname", userDetails.getFirstName())
        .addQueryParameter("lname", userDetails.getLastName())
        .addQueryParameter("mobile", userDetails.getMobile())
        .addQueryParameter("email", userDetails.getEmail())
        .addQueryParameter("dob", userDetails.getDob())
        .addQueryParameter("identity_number", userDetails.getIdentityNumber())
        .addQueryParameter("about_me", userDetails.getAboutMe())
        .addQueryParameter("tag_line", userDetails.getTagLine())
        .addQueryParameter("address", userDetails.getAddress())
        .addQueryParameter("city", userDetails.getCity())
        .addQueryParameter("state", userDetails.getState())
        .addQueryParameter("country", userDetails.getCountry())
        .build()
        .getAsOkHttpResponseAndJSONObject(
            new OkHttpResponseAndJSONObjectRequestListener() {
              @Override
              public void onResponse(Response okHttpResponse, JSONObject response) {
                final String status = response.optString("status", "");
                if (status.equalsIgnoreCase("success")) {
                  Toast.makeText(
                      getApplicationContext(),
                      "User details successfully updated",
                      Toast.LENGTH_SHORT)
                      .show();
                } else {
                  Toast.makeText(
                      getApplicationContext(),
                      "User details update failed",
                      Toast.LENGTH_SHORT)
                      .show();
                }
              }

              @Override
              public void onError(ANError anError) {
                Toast.makeText(
                        getApplicationContext(), "User details update failed", Toast.LENGTH_SHORT)
                    .show();
              }
            });
  }
}
