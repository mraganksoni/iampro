package com.mssinfotech.iampro.co.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.activities.PhotoDetailActivity;
import com.mssinfotech.iampro.co.activities.ProductDetailActivity;
import com.mssinfotech.iampro.co.activities.UserDetailsActivity;
import com.mssinfotech.iampro.co.activities.VideoDetailActivity;
import com.mssinfotech.iampro.co.adapters.TopImageSliderAdapter;
import com.mssinfotech.iampro.co.events.CircleMenuButtonClick;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.models.TopSliderImageModel;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.models.VideoDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.ThumbnailHelper;
import com.mssinfotech.iampro.co.viewmodels.MainViewModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

/** A simple {@link Fragment} subclass. */
public class ChildHomeFragment extends Fragment {
  private static final String TAG = "ChildHomeFragment";

  /** ************************* VIEWMODEL REFERENCES ************************ */
  MainViewModel viewModel;

  /** ************************* VIEW REFERENCES ************************ */
  ViewPager topImageSlider;

  LinearLayout llLatestVideos;
  LinearLayout llSmartMembers;
  LinearLayout llCircleButtonsHolder;
  GridLayout glLatestPhotos;
  GridLayout glLatestProducts;
  GridLayout glProvides;
  GridLayout glDemands;

  /** ************************* ADAPTER ************************ */
  TopImageSliderAdapter topImageSliderAdapter;

  /** ************************* LISTENERS ************************ */
  View.OnClickListener circleButtonsOnClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          final String tag = (String) v.getTag();
          EventBus.getDefault().post(new CircleMenuButtonClick(tag));
        }
      };

  public ChildHomeFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return inflater.inflate(R.layout.fragment_child_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ViewModelProvider.AndroidViewModelFactory viewModelFactory =
        new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

    // Top ImageSlider
    // setting topImageSlider offscreen page limit to 10, so images will show without any jitter
    topImageSlider = view.findViewById(R.id.topImageSlider);
    topImageSlider.setOffscreenPageLimit(10);
    topImageSliderAdapter = new TopImageSliderAdapter(getContext());
    topImageSlider.setAdapter(topImageSliderAdapter);

    viewModel
        .getTopSliderImages()
        .observe(
            this,
            new Observer<List<TopSliderImageModel>>() {
              @Override
              public void onChanged(@Nullable List<TopSliderImageModel> topSliderImageModels) {
                Log.d(TAG, "getTopSliderImages: onChanged: ");
                topImageSliderAdapter.setImagesList(topSliderImageModels);
              }
            });

    // Adding click listeners to the circle buttons
    // also adding same Click listener, buttons get sorted by their tags which are already
    //  added in layout xml
    llCircleButtonsHolder = view.findViewById(R.id.llCircleButtonsHolder);
    final int llCircleButtonsHolderChildCount = llCircleButtonsHolder.getChildCount();
    for (int i = 0; i < llCircleButtonsHolderChildCount; ++i) {
      llCircleButtonsHolder.getChildAt(i).setOnClickListener(circleButtonsOnClickListener);
    }

    // Latest photos grid
    glLatestPhotos = view.findViewById(R.id.glLatestPhotos);

    viewModel
        .getLatesPhotosImages()
        .observe(
            this,
            new Observer<List<ImageDetails>>() {
              @Override
              public void onChanged(@Nullable List<ImageDetails> latestPhotosItemModels) {
                Log.d(TAG, "getLatesPhotosImages: onChanged: ");
                // rvLatestPhotosAdapter.setPhotosList(latestPhotosItemModels);
                fillGlLatestPhotos(latestPhotosItemModels);
              }
            });

    // LatestVideos List
    llLatestVideos = view.findViewById(R.id.llLatestVideos);

    viewModel
        .getLatestVideos()
        .observe(
            this,
            new Observer<List<VideoDetails>>() {
              @Override
              public void onChanged(@Nullable List<VideoDetails> latestVideosItemModels) {
                Log.d(TAG, "getLatestVideos: onChanged: ");
                fillLlLatestVideos(latestVideosItemModels);
              }
            });

    // Smart Members List
    llSmartMembers = view.findViewById(R.id.llSmartMembers);

    viewModel
        .getSmartMembers()
        .observe(
            this,
            new Observer<List<UserDetails>>() {
              @Override
              public void onChanged(@Nullable List<UserDetails> smartMembersItemModels) {
                Log.d(TAG, "getSmartMembers: onChanged: ");
                fillLlSmartMembers(smartMembersItemModels);
              }
            });

    // Latest Products Grid
    glLatestProducts = view.findViewById(R.id.glLatestProducts);

    viewModel
        .getLatestProducts()
        .observe(
            this,
            new Observer<List<ProductDetails>>() {
              @Override
              public void onChanged(@Nullable List<ProductDetails> latestProductsItemModels) {
                Log.d(TAG, "getLatestProducts: onChanged: ");
                fillGlLatestProducts(latestProductsItemModels);
              }
            });

    // Provides and Demands Grid Section
    glProvides = view.findViewById(R.id.glProvides);
    glDemands = view.findViewById(R.id.glDemands);

    viewModel
        .getProvideAndDemands()
        .observe(
            this,
            provideAndDemandList -> {
              Log.d(TAG, "getProvideAndDemands: onChanged: ");
              fillGlProvidesAndDemands(provideAndDemandList);
            });

    ((ScrollView) view).scrollTo(0, 0);
  }

  private void fillGlLatestPhotos(List<ImageDetails> items) {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    final int totalItems = items.size();
    final int columns = glLatestPhotos.getColumnCount();
    final int rows = totalItems / columns;
    int currentItem = 0;

    for (int r = 0; r < rows; ++r) {
      for (int c = 0; c < columns; c++) {
        View view = inflater.inflate(R.layout.item_latest_photos, glLatestPhotos, false);
        bindGlLatestPhotosItem(view, items.get(currentItem), currentItem);

        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(c, 1.0f);
        layoutParams.rowSpec = GridLayout.spec(r, 1.0f);
        layoutParams.setMargins(10, 10, 10, 10);
        view.setLayoutParams(layoutParams);

        glLatestPhotos.addView(view);
        currentItem++;
      }
    }
  }

  private void bindGlLatestPhotosItem(View itemView, ImageDetails model, int position) {
    final ImageView imageView = itemView.findViewById(R.id.imageView);
    final CircularImageView civAvatar = itemView.findViewById(R.id.civAvatar);
    final TextView tvName = itemView.findViewById(R.id.tvName);

    Picasso.get()
        .load(ApiEndpoints.DIR_PHOTOS + model.getImageName())
        .fit()
        .centerInside()
        .into(imageView);
    Picasso.get()
        .load(ApiEndpoints.DIR_AVATAR + model.getUserDetail().getAvatar())
        .fit()
        .centerInside()
        .into(civAvatar);
    tvName.setText(model.getUserDetail().getFullName());

    Map<String, String> map = new HashMap<>();
    map.put("imageUrl", ApiEndpoints.DIR_PHOTOS + model.getImageName());
    imageView.setTag(map);

    // Setting transition names on views
    ViewCompat.setTransitionName(imageView, "latestphotos" + position);
    ViewCompat.setTransitionName(civAvatar, "civAvatarlatestphotos" + position);
    ViewCompat.setTransitionName(tvName, "tvNamelatestphotos" + position);

    // Assigning click listeners to the main item view
    imageView.setOnClickListener(
        v -> {
          startDetailedPhotoActivity(ApiEndpoints.DIR_PHOTOS + model.getImageName(), imageView);
        });

    // Assinging click listener to civ and user text name so it will open user detail info
    View.OnClickListener onClickListener =
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            startUserDetailActivity(civAvatar, tvName, model.getUserDetail());
          }
        };
    civAvatar.setOnClickListener(onClickListener);
    tvName.setOnClickListener(onClickListener);
  }

  private void fillLlLatestVideos(List<VideoDetails> latestVideosItemModels) {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    final int totalItems = latestVideosItemModels.size();

    for (int row = 0; row < totalItems; ++row) {
      View view = inflater.inflate(R.layout.item_latest_videos, llLatestVideos, false);
      bindLlLatestVideosItem(view, latestVideosItemModels.get(row), row);

      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
      layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
      layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

      llLatestVideos.addView(view);
    }
  }

  private void bindLlLatestVideosItem(View view, VideoDetails model, int position) {
    TextView tvUserName = view.findViewById(R.id.tvName);
    CircularImageView civAvatar = view.findViewById(R.id.civAvatar);
    ImageView ivMain = view.findViewById(R.id.ivMain);

    civAvatar.setTransitionName("civAvatarLatestVideos" + position);
    tvUserName.setTransitionName("tvUserNameLatestVideos" + position);
    ivMain.setTransitionName("ivMainLatestVideos" + position);

    if (model.getUserDetails() != null) {
      UserDetails userDetails = model.getUserDetails();

      // display name
      if (userDetails.getFullName() != null) {
        tvUserName.setText(userDetails.getFullName());
      } else {
        tvUserName.setText("Anonymous User");
      }

      // Avatar image
      if (userDetails.getAvatar() != null) {
        Picasso.get()
            .load(ApiEndpoints.DIR_AVATAR + userDetails.getAvatar())
            .fit()
            .centerCrop()
            .into(civAvatar);
      }
    }

    if (model.getVideoName() != null) {
      ThumbnailHelper.getInstance()
          .getThumbnailsFromUrl(ApiEndpoints.DIR_VIDEOS + model.getVideoName(), 100)
          .subscribe(
              bitmap -> {
                ivMain.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        ivMain.setImageBitmap(bitmap);
                      }
                    });
              });
    }

    // Setting transition names on elements

    // Start video detail activity when clicked on main image
    ivMain.setOnClickListener(
        v -> {
          startDetailVideoActivity(
              Uri.parse(ApiEndpoints.DIR_VIDEOS + model.getVideoName()), ivMain);
        });

    // Start user detail activity when clicked on name of avatar
    View.OnClickListener onClickListener =
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            startUserDetailActivity(civAvatar, tvUserName, model.getUserDetails());
          }
        };
    civAvatar.setOnClickListener(onClickListener);
    tvUserName.setOnClickListener(onClickListener);
  }

  private void fillLlSmartMembers(List<UserDetails> smartMembersItemModels) {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    final int totalItems = smartMembersItemModels.size();

    for (int row = 0; row < totalItems; ++row) {
      View view = inflater.inflate(R.layout.item_smart_member, llSmartMembers, false);
      bindLlSmartMembersItem(view, smartMembersItemModels.get(row), row);

      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
      layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
      layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

      llSmartMembers.addView(view);
    }
  }

  private void bindLlSmartMembersItem(View view, UserDetails model, int position) {
    CircularImageView civAvatar = view.findViewById(R.id.civAvatar);
    TextView tvName = view.findViewById(R.id.tvName);

    // Setting Transition names on views
    civAvatar.setTransitionName("civAvatarLatestSmartMembers" + position);
    tvName.setTransitionName("tvNameLatestSmartMembers" + position);

    if (model.getAvatar() != null) {
      Picasso.get()
          .load(ApiEndpoints.DIR_AVATAR + model.getAvatar())
          .fit()
          .centerInside()
          .into(civAvatar);
    }
    if (model.getFullName() != null) {
      tvName.setText(model.getFullName());
    }
    if (model.getEmail() != null) {
      TextView textView = view.findViewById(R.id.tvEmail);
      textView.setText(model.getEmail());
    }
    if (model.getMobile() != null) {
      TextView textView = view.findViewById(R.id.tvPhoneNumber);
      textView.setText(model.getMobile());
    }

    view.setOnClickListener(
        v -> {
          startUserDetailActivity(civAvatar, tvName, model);
        });
  }

  private void fillGlLatestProducts(List<ProductDetails> models) {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    final int totalItems = Math.min(models.size(), 10);
    final int columns = glLatestProducts.getColumnCount();
    final int rows = totalItems / columns;
    int currentItem = 0;

    for (int r = 0; r < rows; ++r) {
      for (int c = 0; c < columns; c++) {
        View view = inflater.inflate(R.layout.item_latest_products, glLatestProducts, false);
        bindGlLatestProductsItem(view, models.get(currentItem), currentItem);

        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(c, 1.0f);
        layoutParams.rowSpec = GridLayout.spec(r, 1.0f);
        layoutParams.setMargins(10, 10, 10, 10);
        view.setLayoutParams(layoutParams);

        glLatestProducts.addView(view);
        currentItem++;
      }
    }
  }

  private void bindGlLatestProductsItem(View view, ProductDetails model, int position) {
    ImageView imageView = view.findViewById(R.id.imageView);
    TextView tvProductName = view.findViewById(R.id.tvProductName);
    TextView tvPurchaseCost = view.findViewById(R.id.tvPurchaseCost);
    TextView tvSellingCost = view.findViewById(R.id.tvSellingCost);

    // Setting Transition names on views
    imageView.setTransitionName("imageViewLatestProducts" + position);
    tvProductName.setTransitionName("tvProductNameLatestProducts" + position);

    // Loading Image View
    if (model.getImage() != null && !model.getImage().isEmpty()) {
      Picasso.get()
          .load(ApiEndpoints.DIR_PRODUCTS + model.getImage())
          .resize(300, 0)
          .centerInside()
          .into(imageView);
    }

    // filling data in purchase textview selling cost
    final String purchaseCost = model.getPurchaseCost() + " Rs";
    tvPurchaseCost.setText(purchaseCost);
    tvPurchaseCost.setPaintFlags(tvPurchaseCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    // fliing data in tv Selling cost
    final String sellingCost = model.getSellingCost() + " Rs";
    tvSellingCost.setText(sellingCost);

    // filling product name
    if (model.getName() != null && !model.getName().isEmpty()) {
      tvProductName.setText(model.getName());
    }

    // open Product Detail Activity on click main item
    view.setOnClickListener(
        v -> {
          startProductDetailActivity(imageView, tvProductName, model);
        });
  }

  private void fillGlProvidesAndDemands(List<ProductDetails> modelList) {
    // Separating demands and provides
    final List<ProductDetails> provideList = new ArrayList<>();
    final List<ProductDetails> demandList = new ArrayList<>();

    for (ProductDetails item : modelList) {
      if (item.getProductType().isEmpty()) {
        continue;
      }

      if (item.getProductType().equals("DEMAND")) {
        demandList.add(item);
        continue;
      }
      if (item.getProductType().equals("PROVIDE")) {
        provideList.add(item);
        continue;
      }
    }

    LayoutInflater inflater = LayoutInflater.from(getContext());

    final int providerListSize = Math.min(provideList.size(), 6);
    final int glProvidersColumns = glProvides.getColumnCount();
    final int glProvidesRows = providerListSize / glProvidersColumns;
    int currentItem = 0;

    for (int r = 0; r < glProvidesRows; ++r) {
      for (int c = 0; c < glProvidersColumns; c++) {
        View view = inflater.inflate(R.layout.item_latest_products, glProvides, false);
        bindGlProvideItem(view, provideList.get(currentItem), currentItem);

        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(c, 1.0f);
        layoutParams.rowSpec = GridLayout.spec(r, 1.0f);
        layoutParams.setMargins(10, 10, 10, 10);
        view.setLayoutParams(layoutParams);

        glProvides.addView(view);
        currentItem++;
      }
    }

    final int demandListSize = Math.min(demandList.size(), 6);
    final int glDemandsColumns = glDemands.getColumnCount();
    final int glDemandsRows = demandListSize / glDemandsColumns;
    currentItem = 0;

    for (int r = 0; r < glDemandsRows; ++r) {
      for (int c = 0; c < glDemandsColumns; c++) {
        View view = inflater.inflate(R.layout.item_latest_products, glDemands, false);
        bindGlDemandItem(view, demandList.get(currentItem), currentItem);

        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(c, 1.0f);
        layoutParams.rowSpec = GridLayout.spec(r, 1.0f);
        layoutParams.setMargins(10, 10, 10, 10);
        view.setLayoutParams(layoutParams);

        glDemands.addView(view);
        currentItem++;
      }
    }
  }

  private void bindGlProvideItem(View view, ProductDetails model, int position) {
    ImageView imageView = view.findViewById(R.id.imageView);
    TextView tvSellingCost = view.findViewById(R.id.tvSellingCost);
    TextView tvProductName = view.findViewById(R.id.tvProductName);

    // Setting transition name on view
    imageView.setTransitionName("imageViewProvideItem" + position);
    tvProductName.setTransitionName("tvProductNameProvideItem" + position);

    // Loading Image View
    if (model.getImage() != null && !model.getImage().isEmpty()) {
      Picasso.get()
          .load(ApiEndpoints.DIR_PRODUCTS + model.getImage())
          .fit()
          .centerCrop(Gravity.CENTER)
          .into(imageView);
    }

    // filling data in tv Selling cost
    final String data = model.getSellingCost() + " Rs";
    tvSellingCost.setText(data);

    // filling product name
    if (model.getName() != null && !model.getName().isEmpty()) {
      tvProductName.setText(model.getName());
    }

    // Setting onClick listener for main container
    view.setOnClickListener(
        v -> {
          startProductDetailActivity(imageView, tvProductName, model);
        });
  }

  private void bindGlDemandItem(View view, ProductDetails model, int position) {
    ImageView imageView = view.findViewById(R.id.imageView);
    TextView tvSellingCost = view.findViewById(R.id.tvSellingCost);
    TextView tvProductName = view.findViewById(R.id.tvProductName);

    // Setting transition names on views
    imageView.setTransitionName("imageViewDemandItem" + position);
    tvProductName.setTransitionName("tvProductNameDemandItem" + position);

    // Loading Image View
    if (model.getImage() != null && !model.getImage().isEmpty()) {
      Picasso.get()
          .load(ApiEndpoints.DIR_PRODUCTS + model.getImage())
          .resize(300, 0)
          .centerInside()
          .into(imageView);
    }

    // fliing data in tv Selling cost
    final String data = model.getSellingCost() + " Rs";
    tvSellingCost.setText(data);

    // filling product name
    if (model.getName() != null && !model.getName().isEmpty()) {
      tvProductName.setText(model.getName());
    }

    // Setting on click listeners on view
    view.setOnClickListener(
        v -> {
          startProductDetailActivity(imageView, tvProductName, model);
        });
  }

  private void startDetailedPhotoActivity(String imageUrl, View view) {
    final String sharedElementName = ViewCompat.getTransitionName(view);
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, sharedElementName)
            .toBundle();
    Intent intent = new Intent(getContext(), PhotoDetailActivity.class);
    intent.putExtra(PhotoDetailActivity.EXTRA_TYPE, PhotoDetailActivity.TYPE_SINGLE_IMAGE);
    intent.putExtra(PhotoDetailActivity.EXTRA_IMAGE_URL, imageUrl);
    intent.putExtra(PhotoDetailActivity.EXTRA_TRANSITION_NAME, sharedElementName);
    intent.putExtra(PhotoDetailActivity.EXTRA_WINDOW_TITLE, "Latest Photos");
    ActivityCompat.startActivity(getActivity(), intent, bundle);
  }

  private void startDetailVideoActivity(Uri uri, View view) {
    final String sharedElementName = view.getTransitionName();
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, sharedElementName)
            .toBundle();
    Intent intent = new Intent(getContext(), VideoDetailActivity.class);
    intent.putExtra(VideoDetailActivity.EXTRA_VIDEO_URI, uri);
    intent.putExtra(VideoDetailActivity.EXTRA_TRANSITION_NAME, sharedElementName);
    intent.putExtra(VideoDetailActivity.EXTRA_WINDOW_TITLE, "Latest Videos");
    ActivityCompat.startActivity(getContext(), intent, bundle);
  }

  private void startUserDetailActivity(View avatar, View fullName, UserDetails userDetails) {
    // Creating intent for UserDetailActivity
    Intent intent = new Intent(getContext(), UserDetailsActivity.class);

    // getting and passing data of shared elements to intent
    Pair<View, String> avatarPair = Pair.create(avatar, avatar.getTransitionName());
    Pair<View, String> fullNamePair = Pair.create(fullName, fullName.getTransitionName());
    intent.putExtra(UserDetailsActivity.EXTRA_AVATAR_TRANSITION_NAME, avatar.getTransitionName());
    intent.putExtra(
        UserDetailsActivity.EXTRA_FULL_NAME_TRANSITION_NAME, fullName.getTransitionName());

    // Generating shared element transitions for provided views
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), avatarPair, fullNamePair)
            .toBundle();

    // Setting action of intent and putting payload data in intent
    intent.setAction(UserDetailsActivity.ACTION_SHOW_USER);
    intent.putExtra(UserDetailsActivity.EXTRA_USER_DATA, userDetails);

    ActivityCompat.startActivity(getContext(), intent, bundle);
  }

  private void startProductDetailActivity(
      View imageView, View productName, ProductDetails productDetails) {
    Intent intent = new Intent(getContext(), ProductDetailActivity.class);

    // Adding Transition details in intent
    Pair<View, String> imagePair = Pair.create(imageView, imageView.getTransitionName());
    Pair<View, String> productNamePair = Pair.create(productName, productName.getTransitionName());
    Bundle bundle =
        ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), imagePair, productNamePair)
            .toBundle();
    intent.putExtra(
        ProductDetailActivity.EXTRA_IMAGE_TRANSITION_NAME, imageView.getTransitionName());
    intent.putExtra(
        ProductDetailActivity.EXTRA_NAME_TRANSITION_NAME, productName.getTransitionName());

    // Setting data intent
    intent.setAction(ProductDetailActivity.ACTION_SINGLE_PRODUCT);
    intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, productDetails);
    intent.putExtra(PhotoDetailActivity.EXTRA_WINDOW_TITLE, "Latest Photos");
    ActivityCompat.startActivity(getContext(), intent, bundle);
  }
}
