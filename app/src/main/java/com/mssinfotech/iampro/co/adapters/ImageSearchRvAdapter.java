package com.mssinfotech.iampro.co.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.activities.MainActivity;
import com.mssinfotech.iampro.co.activities.PhotoDetailActivity;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageSearchRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ImageSearchRvAdapter.ViewHolderWrapper> mItemsList   = new ArrayList<>();
  private Map<String, List<ImageDetails>> originalList = new HashMap<>();
  private final LayoutInflater inflater;
  private String category;
  private PagerAdapter vpSliderAdapter;

  public ImageSearchRvAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD) {
      View view = inflater.inflate(R.layout.item_all_images_head, parent, false);
      return new ImageSearchRvAdapter.HeadViewHolder(view);
    } else if (viewType == ImageSearchRvAdapter.ViewHolderWrapper.TYPE_BODY) {
      View view = inflater.inflate(R.layout.item_all_images_body, parent, false);
      return new BodyViewHolder(view);
    } else if (viewType == ViewHolderWrapper.TYPE_HEADER) {
      View view = inflater.inflate(R.layout.item_rv_header_slider, parent, false);
      return new HeaderViewHolder(view);
    } else {
      View view = inflater.inflate(R.layout.item_all_images_no_item_found, parent, false);
      return new NoItemFoundViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (mItemsList.get(position).type == ViewHolderWrapper.TYPE_HEAD) {
      ((HeadViewHolder) holder).bindViews((String) mItemsList.get(position).payLoad);
    } else if (mItemsList.get(position).type == ViewHolderWrapper.TYPE_BODY) {
      ((BodyViewHolder) holder).bindViews((ImageDetails) mItemsList.get(position).payLoad, originalList, position);
    } else if (mItemsList.get(position).type == ViewHolderWrapper.TYPE_HEADER) {
      ((HeaderViewHolder) holder).bindViews(vpSliderAdapter);
    } else {
      // bind NoItemFoundViewHolder here
    }
  }

  @Override
  public int getItemCount() {
    return mItemsList.size();
  }

  @Override
  public int getItemViewType(int position) {
    return mItemsList.get(position).type;
  }

  public void setData(Map<String, List<ImageDetails>> dataList) {
    this.originalList = dataList;
    mItemsList.clear();
    mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_HEADER, null));
    if (category == null || category.isEmpty()) {
      parseWithoutFilter();
    } else {
      parseWithFilter(category);
    }
    notifyDataSetChanged();
  }
  
  public void setCategory(String category){
    if (category == null) {
      return;
    }
    this.category = category;
//    mItemsList.clear();
//    mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_HEADER, null));
//    parseWithFilter(category);
//    notifyDataSetChanged();
  }
  
  private void parseWithoutFilter() {
    Set<String> categoriesList = originalList.keySet();
    for (String category : categoriesList) {
      mItemsList.add(new ImageSearchRvAdapter.ViewHolderWrapper(
          ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD, category));

      // if category list don't have anything then just show
      // no item view holder
      if (originalList.get(category).size() <= 0) {
        mItemsList.add(new ImageSearchRvAdapter.ViewHolderWrapper(
            ImageSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND, null));
        //continue;
      }

      for (ImageDetails imageDetail : originalList.get(category)) {
        mItemsList.add(new ImageSearchRvAdapter.ViewHolderWrapper(
            ImageSearchRvAdapter.ViewHolderWrapper.TYPE_BODY, imageDetail));
      }
    }
  }
  
  private void parseWithFilter(String filter) {
    Set<String> categoriesList = originalList.keySet();
    for (String category : categoriesList) {
      // Filter data according to categories
      if (!category.equalsIgnoreCase(filter)){
        continue;
      }
      
      // Add Category name to items list
      mItemsList.add(new ImageSearchRvAdapter.ViewHolderWrapper(
          ImageSearchRvAdapter.ViewHolderWrapper.TYPE_HEAD, category));

      // if category list don't have anything then just show
      // no item view holder
      if (originalList.get(category).size() <= 0) {
        mItemsList.add(new ImageSearchRvAdapter.ViewHolderWrapper(
            ImageSearchRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND, null));
        //continue;
      }

      for (ImageDetails imageDetail : originalList.get(category)) {
        mItemsList.add(new ImageSearchRvAdapter.ViewHolderWrapper(
            ImageSearchRvAdapter.ViewHolderWrapper.TYPE_BODY, imageDetail));
      }
      
      // if only want all categories matched by filter then comment below lines
      break;
    }
  }

  public void setHeaderAdapter(PagerAdapter adapter) {
    this.vpSliderAdapter = adapter;
  }

  public static class HeaderViewHolder extends ViewHolder {
    ViewPager vpSlider;
    public HeaderViewHolder(View itemView) {
      super(itemView);
      vpSlider = itemView.findViewById(R.id.vpSlider);
    }

    public void bindViews(PagerAdapter adapter) {
      vpSlider.setAdapter(adapter);
    }
  }

  public static class HeadViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvCategoryTitle;

    public HeadViewHolder(View itemView) {
      super(itemView);
      tvCategoryTitle = itemView.findViewById(R.id.tvCategoryTitle);
    }

    public void bindViews(String title) {
      tvCategoryTitle.setText(title);
    }
  }

  public static class BodyViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivMain;
    private final CircularImageView civAvatar;
    private final TextView          tvUserName;
    private final TextView          tvImageName;

    public BodyViewHolder(View itemView) {
      super(itemView);
      ivMain = itemView.findViewById(R.id.ivMain);
      civAvatar = itemView.findViewById(R.id.civAvatar);
      tvUserName = itemView.findViewById(R.id.tvUserName);
      tvImageName = itemView.findViewById(R.id.tvImageName);
    }

    public void bindViews(ImageDetails item, Map<String, List<ImageDetails>> originalList, int position) {
      Picasso.get().load(ApiEndpoints.DIR_PHOTOS + item.getImageName()).resize(300, 0).centerInside().into(ivMain);
      itemView.setTransitionName("allImagesItem" + position);
      if (item.getUserDetail() != null) {
        Picasso.get().load(ApiEndpoints.DIR_AVATAR + item.getUserDetail().getAvatar()).resize(100, 0).centerInside().into(civAvatar);
        tvImageName.setText(item.getName());
        tvUserName.setText(item.getUserDetail().getFullName());
      }
      itemView.setOnClickListener(v -> {
        Intent intent = new Intent(v.getContext(), PhotoDetailActivity.class);

        // sending type to category to categoryImages so activity will show
        // result acording to this type
        intent.putExtra(PhotoDetailActivity.EXTRA_TYPE, PhotoDetailActivity.TYPE_CATEGORIES_IMAGES);

        // Passing images list so i cant be shown in detail activity
        final ArrayList<ImageDetails> imageDetails = (ArrayList<ImageDetails>) originalList.get(item.getCategory());
        intent.putParcelableArrayListExtra(PhotoDetailActivity.EXTRA_IMAGES_LIST, imageDetails);
        intent.putExtra(PhotoDetailActivity.EXTRA_CURRENT_IMAGE_INDEX, imageDetails.indexOf(item));
        intent.putExtra(PhotoDetailActivity.EXTRA_TRANSITION_NAME, itemView.getTransitionName());
        intent.putExtra(PhotoDetailActivity.EXTRA_WINDOW_TITLE, item.getCategory() + " images");

        // Generating activity options for transitions
        Bundle activityOptions = ActivityOptions
            .makeSceneTransitionAnimation((MainActivity)itemView.getContext(), itemView, itemView.getTransitionName()).toBundle();
        v.getContext().startActivity(intent, activityOptions);
      });
    }
  }

  public static class NoItemFoundViewHolder extends RecyclerView.ViewHolder {

    public NoItemFoundViewHolder(View itemView) {
      super(itemView);
    }
  }

  public static class ViewHolderWrapper {
    public static final int TYPE_HEAD          = 0;
    public static final int TYPE_BODY          = 1;
    public static final int TYPE_NO_ITEM_FOUND = 2;
    public static final int TYPE_HEADER        = 10;

    public final int    type;
    public final Object payLoad;

    public ViewHolderWrapper(int type, Object payLoad) {
      this.type = type;
      this.payLoad = payLoad;
    }
  }
}
