package com.mssinfotech.iampro.co.activities;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import java.util.Locale;

import static android.view.Gravity.CENTER;

public class PhotoDetailActivity extends AppCompatActivity {
  private final String TAG = "PhotoDetailActivity";

  public static final String EXTRA_TYPE = "extra_type";
  public static final String TYPE_SINGLE_IMAGE = "type_single_image";
  public static final String TYPE_CATEGORIES_IMAGES = "type_categories_images";
  public static final String EXTRA_IMAGE_URL = "extra_image_url";
  public static final String EXTRA_IMAGES_LIST = "extra_images_list";
  public static final String EXTRA_CURRENT_IMAGE_INDEX = "extra_current_image_index";
  public static final String EXTRA_TRANSITION_NAME = "extra_transition_name";
  public static final String EXTRA_WINDOW_TITLE = "extra_window_title";

  private int listSize = 0;

  private Toolbar toolbar;

  private ViewPager viewPager;
  private ImageButton ibtnPrevious;
  private ImageButton ibtnNext;
  private TextView tvStatus;
  private Locale locale;

  private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      final String status = String.format(locale, "%d of %d", position + 1, listSize);
      tvStatus.setText(status);
      Log.d(TAG, status);

      // If listSize is equal to last position then disable the previous button
      if ((listSize - 1) == position) {
        ibtnNext.setEnabled(false);
      } else {
        if (!ibtnNext.isEnabled()) ibtnNext.setEnabled(true);
      }

      // If viewPager is at start position then disable it
      if (position == 0) {
        ibtnPrevious.setEnabled(false);
      } else {
        if (!ibtnPrevious.isEnabled()) ibtnPrevious.setEnabled(true);
      }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_detail);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    initViews();
    hookViews();

    if (VERSION.SDK_INT >= VERSION_CODES.N) {
      locale = getResources().getConfiguration().getLocales().get(0);
    } else {
      locale = getResources().getConfiguration().locale;
    }

    final Intent intent = getIntent();

    // Check if intent have window title
    if (intent.hasExtra(EXTRA_WINDOW_TITLE)) {
      final String windowTitle = intent.getStringExtra(EXTRA_WINDOW_TITLE);
      if (windowTitle != null && !windowTitle.isEmpty()) {
        getSupportActionBar().setTitle(windowTitle);
      }
    }

    if (intent.hasExtra(EXTRA_TYPE)) {
      String extraType = intent.getStringExtra(EXTRA_TYPE);

      switch (extraType) {
        case TYPE_SINGLE_IMAGE:
          if (intent.hasExtra(EXTRA_IMAGE_URL)) {
            postponeEnterTransition();
            if (intent.hasExtra(EXTRA_TRANSITION_NAME)) {
              ViewCompat.setTransitionName(viewPager, intent.getStringExtra(EXTRA_TRANSITION_NAME));
            }
            String imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
            viewPager.setAdapter(
                new PagerAdapter() {
                  @NonNull
                  @Override
                  public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    ImageView imageView = new ImageView(container.getContext());
                    LayoutParams layoutParams = new LayoutParams();
                    layoutParams.height = LayoutParams.MATCH_PARENT;
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = CENTER;
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                    container.addView(imageView);
                    imageView.setOnTouchListener(
                        new ImageMatrixTouchHandler(imageView.getContext()));
                    Picasso.get()
                        .load(imageUrl)
                        .noFade()
                        .into(imageView);
                    return imageView;
                  }

                  @Override
                  public void destroyItem(
                      @NonNull ViewGroup container, int position, @NonNull Object object) {
                    container.removeView((View) object);
                  }

                  @Override
                  public int getCount() {
                    return 1;
                  }

                  @Override
                  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                    return view == object;
                  }
                });
            ibtnNext.setEnabled(false);
            ibtnPrevious.setEnabled(false);
            startPostponedEnterTransition();
          }
          break;

          // if gallery images are showing then follow this logic
        case TYPE_CATEGORIES_IMAGES:
          if (intent.hasExtra(EXTRA_IMAGES_LIST)) {
            if (intent.hasExtra(EXTRA_TRANSITION_NAME)) {
              postponeEnterTransition();
              viewPager.setTransitionName(intent.getStringExtra(EXTRA_TRANSITION_NAME));
            }
            final ArrayList<ImageDetails> imageDetails =
                intent.getParcelableArrayListExtra(EXTRA_IMAGES_LIST);
            final int imagesUrlsSize = imageDetails.size();
            listSize = imagesUrlsSize;
            viewPager.addOnPageChangeListener(pageChangeListener);
            viewPager.setAdapter(
                new PagerAdapter() {
                  @NonNull
                  @Override
                  public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    ImageView imageView = new ImageView(container.getContext());
                    LayoutParams layoutParams = new LayoutParams();
                    layoutParams.height = LayoutParams.MATCH_PARENT;
                    layoutParams.width = LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = CENTER;
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                    container.addView(imageView);
                    imageView.setOnTouchListener(
                        new ImageMatrixTouchHandler(imageView.getContext()));
                    Picasso.get()
                        .load(ApiEndpoints.DIR_PHOTOS + imageDetails.get(position).getImageName())
                        .noFade()
                        .into(
                            imageView,
                            new Callback() {
                              @Override
                              public void onSuccess() {
                                startPostponedEnterTransition();
                              }

                              @Override
                              public void onError(Exception e) {
                                startPostponedEnterTransition();
                              }
                            });
                    return imageView;
                  }

                  @Override
                  public void destroyItem(
                      @NonNull ViewGroup container, int position, @NonNull Object object) {
                    container.removeView((View) object);
                  }

                  @Override
                  public int getCount() {
                    return imagesUrlsSize;
                  }

                  @Override
                  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                    return view == object;
                  }
                });

            // Setting default status text and disable previous button then
            // it wont show when viewPager on start position
            final String status = String.format(locale, "1 of %d", listSize);
            tvStatus.setText(status);
            ibtnPrevious.setEnabled(false);
          }
          if (intent.hasExtra(EXTRA_CURRENT_IMAGE_INDEX)) {
            final int index = intent.getIntExtra(EXTRA_CURRENT_IMAGE_INDEX, 0);
            viewPager.setCurrentItem(index);
          }
          break;
      }
    }
  }

  private void initViews() {
    viewPager = findViewById(R.id.vpMain);
    ibtnPrevious = findViewById(R.id.ibtnSkipPrevious);
    ibtnNext = findViewById(R.id.ibtnSkipNext);
    tvStatus = findViewById(R.id.tvStatus);
  }

  private void hookViews() {
    ibtnNext.setOnClickListener(v -> {
      viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    });

    ibtnPrevious.setOnClickListener(v -> {
      viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    viewPager.removeOnPageChangeListener(pageChangeListener);
  }
}
