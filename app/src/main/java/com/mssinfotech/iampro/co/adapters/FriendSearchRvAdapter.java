package com.mssinfotech.iampro.co.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.activities.UserDetailsActivity;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FriendSearchRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ViewHolderWrapper> mItemsList = new ArrayList<>();
  private List<UserDetails> originalList = new ArrayList<>();
  private final LayoutInflater inflater;
  private String category;
  private PagerAdapter vpSliderAdapter;

  public FriendSearchRvAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == ViewHolderWrapper.TYPE_BODY) {
      View view = inflater.inflate(R.layout.item_all_users_body, parent, false);
      return new BodyViewHolder(view);
    } else {
      View view = inflater.inflate(R.layout.item_rv_header_slider, parent, false);
      return new HeaderViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (mItemsList.get(position).type == ViewHolderWrapper.TYPE_BODY) {
      ((BodyViewHolder) holder).bindViews((UserDetails) mItemsList.get(position).payLoad, position);
    } else {
      ((HeaderViewHolder) holder).bindViews(vpSliderAdapter);
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

  public void setData(List<UserDetails> dataList) {
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

  public void setCategory(String category) {
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
    for (UserDetails userDetails : originalList) {
      mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_BODY, userDetails));
    }
  }

  private void parseWithFilter(String filter) {
    for (UserDetails userDetails : originalList) {
      if (userDetails.getCategory().contains(filter)) {
        mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_BODY, userDetails));
      }
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

  public static class BodyViewHolder extends RecyclerView.ViewHolder {
    CircularImageView civAvatar;
    TextView tvName;
    TextView tvNumber;
    TextView tvEmail;

    public BodyViewHolder(View itemView) {
      super(itemView);

      civAvatar = itemView.findViewById(R.id.civAvatar);
      tvName = itemView.findViewById(R.id.tvName);
      tvNumber = itemView.findViewById(R.id.tvNumber);
      tvEmail = itemView.findViewById(R.id.tvEmail);
    }

    public void bindViews(UserDetails userDetails, int position) {
      // Setting transition names in views
      civAvatar.setTransitionName("FriendSearchRvAdapter_civAvatar" + position);
      tvName.setTransitionName("FriendSearchRvAdapter_tvName" + position);

      // Setting avatar image
      Picasso.get()
          .load(ApiEndpoints.DIR_AVATAR + userDetails.getAvatar())
          .fit()
          .centerInside()
          .into(civAvatar);

      // Setting text fields
      tvName.setText(userDetails.getFullName());
      tvNumber.setText(userDetails.getMobile());
      tvEmail.setText(userDetails.getEmail());

      itemView.setOnClickListener(v -> {
        Intent intent = new Intent(itemView.getContext(), UserDetailsActivity.class);

        // Putting data in intent
        intent.setAction(UserDetailsActivity.ACTION_SHOW_USER);
        intent.putExtra(UserDetailsActivity.EXTRA_USER_DATA, userDetails);
        intent.putExtra(UserDetailsActivity.EXTRA_AVATAR_TRANSITION_NAME, civAvatar.getTransitionName());
        intent.putExtra(UserDetailsActivity.EXTRA_FULL_NAME_TRANSITION_NAME, tvName.getTransitionName());

        // Activity options for transition
        Pair<View, String> civAvatarPair = Pair.create(civAvatar, civAvatar.getTransitionName());
        Pair<View, String> tvNamePair = Pair.create(tvName, tvName.getTransitionName());
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
            (Activity) itemView.getContext(), civAvatarPair, tvNamePair).toBundle();
        itemView.getContext().startActivity(intent, bundle);
      });
    }
  }

  public static class ViewHolderWrapper {
    public static final int TYPE_BODY = 1;
    public static final int TYPE_HEADER = 10;

    public final int type;
    public final Object payLoad;

    public ViewHolderWrapper(int type, Object payLoad) {
      this.type = type;
      this.payLoad = payLoad;
    }
  }
}
