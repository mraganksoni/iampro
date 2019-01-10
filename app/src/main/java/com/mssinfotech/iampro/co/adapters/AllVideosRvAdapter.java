package com.mssinfotech.iampro.co.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.activities.MainActivity;
import com.mssinfotech.iampro.co.activities.VideoDetailActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;

public class AllVideosRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<AllVideosRvAdapter.ViewHolderWrapper> mItemsList = new ArrayList<>();
  private Map<String, List<ImageDetails>> originalList = new HashMap<>();
  private final LayoutInflater inflater;

  public AllVideosRvAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == AllVideosRvAdapter.ViewHolderWrapper.TYPE_HEAD) {
      View view = inflater.inflate(R.layout.item_all_images_head, parent, false);
      return new AllVideosRvAdapter.HeadViewHolder(view);
    } else if (viewType == AllVideosRvAdapter.ViewHolderWrapper.TYPE_BODY) {
      View view = inflater.inflate(R.layout.item_all_videos_body, parent, false);
      return new AllVideosRvAdapter.BodyViewHolder(view);
    } else {
      View view = inflater.inflate(R.layout.item_all_images_no_item_found, parent, false);
      return new AllVideosRvAdapter.NoItemFoundViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (mItemsList.get(position).type == AllVideosRvAdapter.ViewHolderWrapper.TYPE_HEAD) {
      ((AllVideosRvAdapter.HeadViewHolder) holder)
          .bindViews((String) mItemsList.get(position).payLoad);
    } else if (mItemsList.get(position).type == AllVideosRvAdapter.ViewHolderWrapper.TYPE_BODY) {
      ((AllVideosRvAdapter.BodyViewHolder) holder)
          .bindViews((ImageDetails) mItemsList.get(position).payLoad, position);
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
    Set<String> categoriesList = dataList.keySet();
    for (String category : categoriesList) {
      mItemsList.add(
          new AllVideosRvAdapter.ViewHolderWrapper(
              AllVideosRvAdapter.ViewHolderWrapper.TYPE_HEAD, category));

      // if category list don't have anything then just show
      // no item view holder
      if (dataList.get(category).size() <= 0) {
        mItemsList.add(
            new AllVideosRvAdapter.ViewHolderWrapper(
                AllVideosRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND, null));
        // continue;
      }

      for (ImageDetails imageDetail : dataList.get(category)) {
        mItemsList.add(
            new AllVideosRvAdapter.ViewHolderWrapper(
                AllVideosRvAdapter.ViewHolderWrapper.TYPE_BODY, imageDetail));
      }
    }
    notifyDataSetChanged();
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
    private final TextView tvUserName;

    public BodyViewHolder(View itemView) {
      super(itemView);
      ivMain = itemView.findViewById(R.id.ivMain);
      civAvatar = itemView.findViewById(R.id.civAvatar);
      tvUserName = itemView.findViewById(R.id.tvUserName);
    }

    public void bindViews(
        ImageDetails item,  int position) {
      itemView.setTransitionName("AllVideosRvAdapter" + position);
      Picasso.get()
          .load(ApiEndpoints.DIR_VIDEOS_THUMBS + item.getVideoThumbnail())
          .fit()
          .centerInside()
          .into(ivMain);
      if (item.getUserDetail() != null) {
        Picasso.get()
            .load(ApiEndpoints.DIR_AVATAR + item.getUserDetail().getAvatar())
            .resize(100, 0)
            .centerInside()
            .into(civAvatar);
        tvUserName.setText(item.getUserDetail().getFullName());
      }

      itemView.setOnClickListener(
          v -> {
            //                Toast.makeText(itemView.getContext(), "yet to implement",
            // Toast.LENGTH_SHORT).show();
            //                return;
            Intent intent = new Intent(v.getContext(), VideoDetailActivity.class);

            // Putting extra data in intent
            intent.putExtra(VideoDetailActivity.EXTRA_TRANSITION_NAME, itemView.getTransitionName());
            intent.putExtra(VideoDetailActivity.EXTRA_VIDEO_URI, ApiEndpoints.DIR_VIDEOS + item.getImageName());

            // Generating bundle for shared element animation transition
            Bundle activityOptions =
                ActivityOptions.makeSceneTransitionAnimation(
                        (MainActivity) itemView.getContext(),
                        itemView,
                        itemView.getTransitionName())
                    .toBundle();
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
    public static final int TYPE_HEAD = 0;
    public static final int TYPE_BODY = 1;
    public static final int TYPE_NO_ITEM_FOUND = 2;

    public final int type;
    public final Object payLoad;

    public ViewHolderWrapper(int type, Object payLoad) {
      this.type = type;
      this.payLoad = payLoad;
    }
  }
}
