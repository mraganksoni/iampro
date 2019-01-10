package com.mssinfotech.iampro.co.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.AllImagesRvAdapter;
import com.mssinfotech.iampro.co.adapters.FriendSearchRvAdapter;
import com.mssinfotech.iampro.co.adapters.ImageSearchRvAdapter;
import com.mssinfotech.iampro.co.adapters.ImageSearchRvAdapter.ViewHolderWrapper;
import com.mssinfotech.iampro.co.adapters.ImageSliderAdapter;
import com.mssinfotech.iampro.co.adapters.ProductSearchRvAdapter;
import com.mssinfotech.iampro.co.adapters.VideoSearchRvAdapter;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.viewmodels.SearchViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A simple {@link Fragment} subclass. */
public class SearchFragment extends Fragment {
  private static final String TAG = "SearchFragment";

  /* **************************
   * State Tracker
   ************************* */
  private String lastSearchType;

  /* **************************
   * Variables
   ************************* */
  private ArrayList<String> imageCategoryList = new ArrayList<>();
  private ArrayList<String> videoCategoryList = new ArrayList<>();
  private ArrayList<String> friendCategoryList = new ArrayList<>();
  private ArrayList<String> productCategoryList = new ArrayList<>();
  private ArrayList<String> provideCategoryList = new ArrayList<>();
  private ArrayList<String> demandCategoryList = new ArrayList<>();

  private ArrayList<String> imageSlidersList;
  private ArrayList<String> videoSlidersList;
  private ArrayList<String> friendSlidersList;
  private ArrayList<String> productSlidersList;
  private ArrayList<String> provideSlidersList;
  private ArrayList<String> demandSlidersList;

  /* *************************
   * Adapters
   ************************** */
  private ArrayAdapter<String> searchCategoryAdapter;

  private ImageSliderAdapter imageSliderAdapter;
  private ImageSearchRvAdapter imageSearchRvAdapter;
  private VideoSearchRvAdapter videoSearchRvAdapter;
  private FriendSearchRvAdapter friendSearchRvAdapter;
  private ProductSearchRvAdapter productSearchRvAdapter;
  private ProductSearchRvAdapter provideSearchRvAdapter;
  private ProductSearchRvAdapter demandSearchRvAdapter;

  /* **************************
   * VIEW REFERENCES
   ************************* */
  private ViewPager viewPager;
  private TabLayout tabLayout;
  private Spinner spnrSearchType;
  private Spinner spnrCategory;
  private LinearLayout llRvContent;
  private Button btnSearch;
  private Group groupForm;
  private EditText etSearchData;
  private TextView tvTitle;
  private ImageButton ibtnBack;
  private ImageButton ibtnFilter;
  private RecyclerView rvContent;

  /* **************************
   * State Tracking Variables
   ************************* */
  private Map<String, List<ProductDetails>> searchList = new HashMap<>();
  public boolean isContentShowing = false;

  /* **************************
   * ViewModel
   ************************* */
  private SearchViewModel searchViewModel;

  public SearchFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_search, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // setting up search viewModel
    AndroidViewModelFactory androidViewModelFactory =
        new AndroidViewModelFactory(getActivity().getApplication());
    searchViewModel =
        ViewModelProviders.of(this, androidViewModelFactory).get(SearchViewModel.class);

    searchCategoryAdapter =
        new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
    searchCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // Initializing Image Slider Adapter
    imageSliderAdapter = new ImageSliderAdapter(getContext());

    // Initializing RecyclerViews Adapter
    imageSearchRvAdapter = new ImageSearchRvAdapter(getContext());
    videoSearchRvAdapter = new VideoSearchRvAdapter(getContext());
    friendSearchRvAdapter = new FriendSearchRvAdapter(getContext());
    productSearchRvAdapter = new ProductSearchRvAdapter(getContext());
    provideSearchRvAdapter = new ProductSearchRvAdapter(getContext());
    demandSearchRvAdapter = new ProductSearchRvAdapter(getContext());

    initViews(view);
    hookViews();
    hookInViewSearchViewModel();
  }

  private void initViews(View view) {
    spnrSearchType = view.findViewById(R.id.spnrSearchType);
    spnrCategory = view.findViewById(R.id.spnrCategory);
    llRvContent = view.findViewById(R.id.llRvContent);
    btnSearch = view.findViewById(R.id.btnSearch);
    groupForm = view.findViewById(R.id.groupForm);
    etSearchData = view.findViewById(R.id.etSearchData);
    tvTitle = view.findViewById(R.id.tvTitle);
    ibtnBack = view.findViewById(R.id.ibtnBack);
    ibtnFilter = view.findViewById(R.id.ibtnFilter);
    rvContent = view.findViewById(R.id.rvContent);
  }

  private void hookViews() {
    // First setup recycler view coz recycler view should be set first
    GridLayoutManager rvContentLM = (GridLayoutManager) rvContent.getLayoutManager();
    rvContentLM.setSpanSizeLookup(new SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        if (rvContent.getAdapter() instanceof ImageSearchRvAdapter) {
          final int itemType = imageSearchRvAdapter.getItemViewType(position);
          if (itemType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
              itemType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND ||
              itemType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEADER) {
            return 2;
          } else {
            return 1;
          }
        } else if (rvContent.getAdapter() instanceof VideoSearchRvAdapter) {
          final int itemType = videoSearchRvAdapter.getItemViewType(position);
          if (itemType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
              itemType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND ||
              itemType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEADER) {
            return 2;
          } else {
            return 1;
          }
        } else if (rvContent.getAdapter() instanceof FriendSearchRvAdapter) {
          return 2;
        } else if (rvContent.getAdapter().equals(productSearchRvAdapter)) {
          final int itemType = productSearchRvAdapter.getItemViewType(position);
          if (itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
              itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND ||
              itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_HEADER) {
            return 2;
          } else {
            return 1;
          }
        } else if (rvContent.getAdapter().equals(provideSearchRvAdapter)) {
          final int itemType = provideSearchRvAdapter.getItemViewType(position);
          if (itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
              itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND ||
              itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_HEADER) {
            return 2;
          } else {
            return 1;
          }
        } else if (rvContent.getAdapter().equals(provideSearchRvAdapter)) {
          final int itemType = provideSearchRvAdapter.getItemViewType(position);
          if (itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
              itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND ||
              itemType == ProductSearchRvAdapter.ViewHolderWrapper.TYPE_HEADER) {
            return 2;
          } else {
            return 1;
          }
        }
        return 1;
      }
    });

    // Search Spinner item selection listener
    spnrSearchType.setOnItemSelectedListener(
        new OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String[] catTypes = getContext().getResources().getStringArray(R.array.searchTypes);
            if (catTypes.length <= position) {
              return;
            }
            final String category = catTypes[position];
            switch (category) {
              default:
              case "Image":
                setImageCategories();
                lastSearchType = "Image";
                break;
              case "Video":
                setVideoCategories();
                lastSearchType = "Video";
                break;
              case "Friend":
                setFriendCategories();
                lastSearchType = "Friend";
                break;
              case "Product":
                setProductCategories();
                lastSearchType = "Product";
                break;
              case "Provide":
                setProvideCategories();
                lastSearchType = "Provide";
                break;
              case "Demand":
                setDemandCategories();
                lastSearchType = "Demand";
                break;
            }
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    spnrCategory.setAdapter(searchCategoryAdapter);

    // Button Search
    btnSearch.setOnClickListener(
        v -> {
          // Setting title in secondary toolbar
          final String title = lastSearchType + " Search";
          tvTitle.setText(title);
          // Setting image in image slider
          switch (lastSearchType) {
            default:
            case "Image":
              imageSliderAdapter.setImages(imageSlidersList);
              searchViewModel.loadImageSearch(etSearchData.getText().toString().toLowerCase());
              final String currentImageCategory = (String) spnrCategory.getSelectedItem();
              if (currentImageCategory != null && !currentImageCategory.isEmpty()) {
                imageSearchRvAdapter.setCategory(currentImageCategory);
              }
              imageSearchRvAdapter.setHeaderAdapter(imageSliderAdapter);
              rvContent.setAdapter(imageSearchRvAdapter);
              break;
            case "Video":
              imageSliderAdapter.setImages(videoSlidersList);
              searchViewModel.loadVideoSearch(etSearchData.getText().toString().toLowerCase());
              final String currentVideoCategory = (String) spnrCategory.getSelectedItem();
              if (currentVideoCategory != null && !currentVideoCategory.isEmpty()) {
                imageSearchRvAdapter.setCategory(currentVideoCategory);
              }
              videoSearchRvAdapter.setHeaderAdapter(imageSliderAdapter);
              rvContent.setAdapter(videoSearchRvAdapter);
              break;
            case "Friend":
              imageSliderAdapter.setImages(friendSlidersList);
              searchViewModel.loadFriendSearch(etSearchData.getText().toString().toLowerCase());
              final String currentFriendCategory = (String) spnrCategory.getSelectedItem();
              if (currentFriendCategory != null && !currentFriendCategory.isEmpty()) {
                friendSearchRvAdapter.setCategory(currentFriendCategory);
              }
              friendSearchRvAdapter.setHeaderAdapter(imageSliderAdapter);
              rvContent.setAdapter(friendSearchRvAdapter);
              break;
            case "Product":
              imageSliderAdapter.setImages(productSlidersList);
              searchViewModel.loadProductSearch(etSearchData.getText().toString().toLowerCase());
              final String currentProductCategory = (String) spnrCategory.getSelectedItem();
              if (currentProductCategory != null && !currentProductCategory.isEmpty()) {
                productSearchRvAdapter.setCategory(currentProductCategory);
              }
              productSearchRvAdapter.setHeaderAdapter(imageSliderAdapter);
              rvContent.setAdapter(productSearchRvAdapter);
              break;
            case "Provide":
              imageSliderAdapter.setImages(provideSlidersList);
              searchViewModel.loadProvideSearch(etSearchData.getText().toString().toLowerCase());
              final String currentProvideCategory = (String) spnrCategory.getSelectedItem();
              if (currentProvideCategory != null && !currentProvideCategory.isEmpty()) {
                provideSearchRvAdapter.setCategory(currentProvideCategory);
              }
              provideSearchRvAdapter.setHeaderAdapter(imageSliderAdapter);
              rvContent.setAdapter(provideSearchRvAdapter);
              break;
            case "Demand":
              imageSliderAdapter.setImages(demandSlidersList);
              searchViewModel.loadDemandSearch(etSearchData.getText().toString().toLowerCase());
              final String currentDemandCategory = (String) spnrCategory.getSelectedItem();
              if (currentDemandCategory != null && !currentDemandCategory.isEmpty()) {
                demandSearchRvAdapter.setCategory(currentDemandCategory);
              }
              demandSearchRvAdapter.setHeaderAdapter(imageSliderAdapter);
              rvContent.setAdapter(demandSearchRvAdapter);
              break;
          }
          showContent();
        });

    // Back Button click handler
    ibtnBack.setOnClickListener(v -> hideContent());

  }

  private void hookInViewSearchViewModel() {
    // Listening for categories changes
    searchViewModel
        .getImageSubCategories()
        .observe(
            this,
            strings -> {
              imageCategoryList = strings;
              setImageCategories();
            });
    searchViewModel.getVideoSubCategories().observe(this, strings -> videoCategoryList = strings);
    searchViewModel.getFriendSubCategories().observe(this, strings -> friendCategoryList = strings);
    searchViewModel
        .getProdutSubCategories()
        .observe(this, strings -> productCategoryList = strings);
    searchViewModel
        .getProvideSubCategories()
        .observe(this, strings -> provideCategoryList = strings);
    searchViewModel.getDemandSubCategories().observe(this, strings -> demandCategoryList = strings);

    // Listening for slider images changes
    searchViewModel.getImageSlidersList().observe(this, strings -> imageSlidersList = strings);
    searchViewModel.getVideoSlidersList().observe(this, strings -> videoSlidersList = strings);
    searchViewModel.getFriendSlidersList().observe(this, strings -> friendSlidersList = strings);
    searchViewModel.getProductSlidersList().observe(this, strings -> productSlidersList = strings);
    searchViewModel.getProvideSlidersList().observe(this, strings -> provideSlidersList = strings);
    searchViewModel.getDemandSlidersList().observe(this, strings -> demandSlidersList = strings);

    // Listening for Search result and setting them to RecyclerView Adapters
    searchViewModel.getImageSearchData().observe(this, stringListMap -> {
      imageSearchRvAdapter.setData(stringListMap);
    });
    searchViewModel.getVideoSearchData().observe(this, stringListMap -> {
      videoSearchRvAdapter.setData(stringListMap);
    });
    searchViewModel.getFriendSearchData().observe(this, userDetails -> {
      friendSearchRvAdapter.setData(userDetails);
    });
    searchViewModel.getProductSearchData().observe(this, stringListMap -> {
      productSearchRvAdapter.setData(stringListMap);
    });
    searchViewModel.getProvideSearchData().observe(this, stringListMap -> {
      provideSearchRvAdapter.setData(stringListMap);
    });
    searchViewModel.getDemandSearchData().observe(this, stringListMap -> {
      demandSearchRvAdapter.setData(stringListMap);
    });
  }

  // <editor-fold desc="Categories change methods">
  private void setImageCategories() {
    searchCategoryAdapter.clear();
    searchCategoryAdapter.addAll(imageCategoryList);
    searchCategoryAdapter.notifyDataSetChanged();
    spnrCategory.setSelection(0);
  }

  private void setVideoCategories() {
    searchCategoryAdapter.clear();
    searchCategoryAdapter.addAll(videoCategoryList);
    searchCategoryAdapter.notifyDataSetChanged();
    spnrCategory.setSelection(0);
  }

  private void setFriendCategories() {
    searchCategoryAdapter.clear();
    searchCategoryAdapter.addAll(friendCategoryList);
    searchCategoryAdapter.notifyDataSetChanged();
    spnrCategory.setSelection(0);
  }

  private void setProductCategories() {
    searchCategoryAdapter.clear();
    searchCategoryAdapter.addAll(productCategoryList);
    searchCategoryAdapter.notifyDataSetChanged();
    spnrCategory.setSelection(0);
  }

  private void setProvideCategories() {
    searchCategoryAdapter.clear();
    searchCategoryAdapter.addAll(provideCategoryList);
    searchCategoryAdapter.notifyDataSetChanged();
    spnrCategory.setSelection(0);
  }

  private void setDemandCategories() {
    searchCategoryAdapter.clear();
    searchCategoryAdapter.addAll(demandCategoryList);
    searchCategoryAdapter.notifyDataSetChanged();
    spnrCategory.setSelection(0);
  }
  // </editor-fold>

  // <editor-fold desc="Show Hide Methods">
  public void showContent() {
    final int cx = llRvContent.getWidth() / 2;
    final int cy = llRvContent.getHeight() / 2;
    final int radius = (int) Math.hypot(cx, cy);
    final Animator revealAnimator =
        ViewAnimationUtils.createCircularReveal(llRvContent, cx, cy, 0, radius);
    revealAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            revealAnimator.removeAllListeners();
            groupForm.setVisibility(View.GONE);
          }
        });
    llRvContent.setVisibility(View.VISIBLE);
    revealAnimator.start();
    isContentShowing = true;
  }

  public void hideContent() {
    final int cx = llRvContent.getWidth() / 2;
    final int cy = llRvContent.getHeight() / 2;
    final int radius = (int) Math.hypot(cx, cy);
    final Animator revealAnimator =
        ViewAnimationUtils.createCircularReveal(llRvContent, cx, cy, radius, 0);
    revealAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            revealAnimator.removeAllListeners();
            llRvContent.setVisibility(View.INVISIBLE);
          }
        });
    groupForm.setVisibility(View.VISIBLE);
    revealAnimator.start();
    isContentShowing = false;
  }
  // </editor-fold>
}
