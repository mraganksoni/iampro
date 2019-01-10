package com.mssinfotech.iampro.co.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.ImageSliderAdapter;
import com.mssinfotech.iampro.co.adapters.RvCommentsAdapter;
import com.mssinfotech.iampro.co.databinding.ActivityProductDetailBinding;
import com.mssinfotech.iampro.co.dialogs.WriteCommentDialog;
import com.mssinfotech.iampro.co.dialogs.WriteCommentDialog.CommentStatusListener;
import com.mssinfotech.iampro.co.models.Comment;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.utils.VolleyUtil;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {
  public static final String TAG = "ProductDetailActivity";

  public static final String ACTION_SINGLE_PRODUCT = "action_single_product";
  public static final String EXTRA_PRODUCT = "extra_product";

  public static final String EXTRA_IMAGE_TRANSITION_NAME = "extra_image_transition_name";
  public static final String EXTRA_NAME_TRANSITION_NAME = "extra_name_transition_name";

  /* **********************************************
   * Adapters
   ********************************************** */
  ImageSliderAdapter productImageSliderAdapter;
  RvCommentsAdapter rvCommentsAdapter;

  /* **********************************************
   * Bindings
   ********************************************** */
  ActivityProductDetailBinding binding;

  /* **********************************************
   * State Tracking Objects
   ********************************************** */
  private ProductDetails mProductDetails;
  private UserDetails userDetails;

  /* **********************************************
   * Dialog Objects
   ********************************************** */
  private WriteCommentDialog writeCommentDialog;

  /* **********************************************
   * Shared Preferences
   ********************************************** */
  SharedPreferences loginSharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Getting default shared preferences
    loginSharedPreferences = getSharedPreferences(LoginPreferencesConstants.PREF_NAME, MODE_PRIVATE);

    // Processing shared element data and setting it to views
    if (getIntent() != null) {
      Intent intent = getIntent();
      if (intent.hasExtra(EXTRA_IMAGE_TRANSITION_NAME)) {
        binding.vpProductImages.setTransitionName(
            intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME));
      }
      if (intent.hasExtra(EXTRA_NAME_TRANSITION_NAME)) {
        binding.tvProductName.setTransitionName(intent.getStringExtra(EXTRA_NAME_TRANSITION_NAME));
      }
    }

    // Setting product image slider settings
    productImageSliderAdapter = new ImageSliderAdapter(this);
    productImageSliderAdapter.setRemoteDir(ApiEndpoints.DIR_PRODUCTS);
    binding.vpProductImages.setAdapter(productImageSliderAdapter);

    // Setting comments recycler view
    rvCommentsAdapter = new RvCommentsAdapter(this, getLayoutInflater());
    binding.rvComments.addItemDecoration(
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    binding.rvComments.setAdapter(rvCommentsAdapter);

    // Loading user details in state tracking variables
    processIntent(getIntent());

    loadUserDetails();

    setupViews();
  }

  private void setupViews() {
    binding.btnWriteReview.setOnClickListener(v -> {
      // If comment dialog isn't initialized then initialize it
      if (writeCommentDialog == null) {
        writeCommentDialog = new WriteCommentDialog();
        writeCommentDialog.setStyle(R.style.AppTheme_WriteCommentDialog, DialogFragment.STYLE_NO_TITLE);
        writeCommentDialog.setCommentStatusListener(new CommentStatusListener() {
          @Override
          public void onSuccess() {
            loadCommentData(mProductDetails.getId());
          }

          @Override
          public void onError(String error) {

          }
        });
      }

      // Putting userId and productId in Dialog
      writeCommentDialog.setArguments(userDetails != null ? userDetails.getId() : 0 , mProductDetails != null ? mProductDetails.getId() : 0);
      writeCommentDialog.show(getSupportFragmentManager(), "write-comment-dialog");
    });
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    processIntent(intent);
  }

  private void processIntent(Intent intent) {
    // if intent is null then just log message and return
    // no need to process data
    if (intent == null) {
      Log.d(TAG, "processIntent: Failed Null intent found");
      return;
    }

    // Check for intent action if intent action is null
    // then we don't have to process data anymore
    final String intentAction = intent.getAction();
    if (intentAction == null) {
      Log.d(TAG, "processIntent: Failed intent with null action");
      return;
    }

    // Everything working fine here we can process data now
    switch (intentAction) {
      case ACTION_SINGLE_PRODUCT:
        ProductDetails productDetails = intent.getParcelableExtra(EXTRA_PRODUCT);
        if (productDetails != null) {
          mProductDetails = productDetails;
          setProductDetails(productDetails);
        }
        break;
      default:
        Log.d(TAG, "processIntent: Unmanaged intent action found");
    }
  }

  private void setProductDetails(ProductDetails productDetails) {
    // Setting product name
    binding.tvProductName.setText(productDetails.getName());

    // Setting Product Images
    ArrayList<String> productImagesList = new ArrayList<>();
    if (productDetails.getImage() != null && !productDetails.getImage().isEmpty()) {
      productImagesList.add(productDetails.getImage());
    }
    if (productDetails.getOtherImage() != null && !productDetails.getOtherImage().isEmpty()) {
      String[] otherImages = productDetails.getOtherImage().split(",");
      productImagesList.addAll(Arrays.asList(otherImages));
    }
    productImageSliderAdapter.setImages(productImagesList);

    // Setting product info data
    binding.tvProductName2.setText(productDetails.getName());
    binding.tvCategory.setText(productDetails.getCategory());
    binding.tvProductCost.setText(String.valueOf(productDetails.getSellingCost()));

    // Filling product description
    binding.tvProductDescription.setText(productDetails.getDetail());

    // Filling service provider details
    if (productDetails.getUserName() != null && !productDetails.getUserName().isEmpty()) {
      binding.tvUserName.setText(productDetails.getUserName());
    } else {
      binding.tvUserName.setText(
          productDetails.getUserDetails() != null
              ? productDetails.getUserDetails().getFullName()
              : "Anonymous User");
    }
    if (productDetails.getUserDetails() != null) {
      binding.tvEmail2.setText(productDetails.getUserDetails().getEmail());
    }
    if (productDetails.getCity() != null) {
      binding.tvContact.setText(productDetails.getCity());
    } else {
      binding.tvContact.setText(
          productDetails.getUserDetails() != null
              ? productDetails.getUserDetails().getCity()
              : "Mobile Number Not found");
    }

    // Load and fill comment data
    loadCommentData(productDetails.getId());
  }

  private void loadCommentData(int id) {
    Uri uri =
        new Uri.Builder()
            .scheme("http")
            .authority("www.iampro.co")
            .appendPath("api")
            .appendPath("ajax.php")
            .appendQueryParameter("type", "getProductReview")
            .appendQueryParameter("id", String.valueOf(id))
            .build();

    Log.d(TAG, "loadCommentData: uri : " + uri.toString());
    JsonArrayRequest commentsRequest =
        new JsonArrayRequest(
            uri.toString(),
            response -> {
              Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
              final int size = response.length();
              final ArrayList<Comment> commentsList = new ArrayList<>();
              for (int i = 0; i < size; ++i) {
                try {
                  JSONObject jsonObject = response.getJSONObject(i);
                  Comment comment = gson.fromJson(jsonObject.toString(), Comment.class);
                  commentsList.add(comment);
                } catch (JSONException | JsonSyntaxException e) {
                  e.printStackTrace();
                }
              }
              rvCommentsAdapter.setCommentsData(commentsList);
            },
            error -> {
              Log.e(TAG, "commentsRequest : Failed", error.getCause());
            });
    VolleyUtil.getInstance(getApplicationContext()).addRequest(commentsRequest);
  }

  private void loadUserDetails() {
    if (loginSharedPreferences.contains(LoginPreferencesConstants.KEY_USER_INFO)) {
      final String userJsonStr = loginSharedPreferences.getString(LoginPreferencesConstants.KEY_USER_INFO, "");
      if (!userJsonStr.isEmpty()) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
          userDetails = gson.fromJson(userJsonStr, UserDetails.class);
        } catch (JsonSyntaxException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
