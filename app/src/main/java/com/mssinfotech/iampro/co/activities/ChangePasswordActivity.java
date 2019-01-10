package com.mssinfotech.iampro.co.activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.databinding.ActivityChangePasswordBinding;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import okhttp3.Response;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
  private static final String TAG = "ChangePasswordActivity";

  /* *************************
   * BINDINGS
   ************************* */
  ActivityChangePasswordBinding binding;

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
    binding = DataBindingUtil.setContentView(this , R.layout.activity_change_password);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    hookViews();
    loadDataFromSp();
  }

  private void hookViews() {
    binding.acbtnChangePassword.setOnClickListener(v -> {
      if (validateFields()) {
        AndroidNetworking.get("http://www.iampro.co/ajax/signup.php")
            .addQueryParameter("type", "changePass")
            .addQueryParameter("opass", binding.tietCurrentPassword.getText().toString())
            .addQueryParameter("npass", binding.tietNewPassword.getText().toString())
            .addQueryParameter("cpass", binding.tietConfirmPassword.getText().toString())
            .addQueryParameter("uid", String.valueOf(userDetails.getId()))
            .build()
            .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
              @Override
              public void onResponse(Response okHttpResponse, JSONObject response) {
                final String statusMessage = response.optString("status");
                if ("success".equalsIgnoreCase(statusMessage)) {
                  Toast.makeText(getApplicationContext(), "Password Changes Successfully", Toast.LENGTH_SHORT).show();
                  clearAllFields();
                } else {
                  Toast.makeText(getApplicationContext(), "Please Check password", Toast.LENGTH_SHORT).show();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.e(TAG, "Change password request failed", anError);
              }
            });
      }
    });
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

  private boolean validateFields() {
    boolean flag = true;

    if (binding.tietCurrentPassword.length() == 0) {
      binding.tilCurrentPassword.setError("Current Required");
      flag = false;
    } else {
      if (binding.tilCurrentPassword.isErrorEnabled()) binding.tilCurrentPassword.setErrorEnabled(false);
    }
    if (binding.tietNewPassword.length() == 0) {
      binding.tilNewPassword.setError("NewPassword Required");
      flag = false;
    } else {
      if (binding.tilNewPassword.isErrorEnabled()) binding.tilNewPassword.setErrorEnabled(false);
    }
    if (!binding.tietConfirmPassword.getText().toString().equals(binding.tietNewPassword.getText().toString())) {
      binding.tilConfirmPassword.setError("Password not matching");
      flag = false;
    } else {
      if (binding.tilConfirmPassword.isErrorEnabled()) binding.tilConfirmPassword.setErrorEnabled(false);
    }

    return flag;
  }

  private void clearAllFields() {
    binding.tietCurrentPassword.setText("");
    binding.tietNewPassword.setText("");
    binding.tietConfirmPassword.setText("");
  }
}
