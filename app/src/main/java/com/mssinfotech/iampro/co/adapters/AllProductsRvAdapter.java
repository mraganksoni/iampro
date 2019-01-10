package com.mssinfotech.iampro.co.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.activities.MainActivity;
import com.mssinfotech.iampro.co.activities.ProductDetailActivity;
import com.mssinfotech.iampro.co.activities.UserDetailsActivity;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;

public class AllProductsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<AllProductsRvAdapter.ViewHolderWrapper> mItemsList = new ArrayList<>();
  private Map<String, List<ProductDetails>> originalList = new HashMap<>();
  private final LayoutInflater inflater;

  public AllProductsRvAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == AllProductsRvAdapter.ViewHolderWrapper.TYPE_HEAD) {
      View view = inflater.inflate(R.layout.item_all_images_head, parent, false);
      return new AllProductsRvAdapter.HeadViewHolder(view);
    } else if (viewType == AllProductsRvAdapter.ViewHolderWrapper.TYPE_BODY) {
      View view = inflater.inflate(R.layout.item_all_products_body, parent, false);
      return new AllProductsRvAdapter.BodyViewHolder(view);
    } else {
      View view = inflater.inflate(R.layout.item_all_images_no_item_found, parent, false);
      return new AllProductsRvAdapter.NoItemFoundViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (mItemsList.get(position).type == AllProductsRvAdapter.ViewHolderWrapper.TYPE_HEAD) {
      ((AllProductsRvAdapter.HeadViewHolder) holder)
          .bindViews((String) mItemsList.get(position).payLoad);
    } else if (mItemsList.get(position).type == AllProductsRvAdapter.ViewHolderWrapper.TYPE_BODY) {
      ((AllProductsRvAdapter.BodyViewHolder) holder)
          .bindViews((ProductDetails) mItemsList.get(position).payLoad, position);
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

  public void setData(Map<String, List<ProductDetails>> dataList) {
    this.originalList = dataList;
    Set<String> categoriesList = dataList.keySet();
    for (String category : categoriesList) {
      mItemsList.add(
          new AllProductsRvAdapter.ViewHolderWrapper(
              AllProductsRvAdapter.ViewHolderWrapper.TYPE_HEAD, category));

      // if category list don't have anything then just show
      // no item view holder
      if (dataList.get(category).size() <= 0) {
        mItemsList.add(
            new AllProductsRvAdapter.ViewHolderWrapper(
                AllProductsRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND, null));
        // continue;
      }

      for (ProductDetails productDetail : dataList.get(category)) {
        mItemsList.add(
            new AllProductsRvAdapter.ViewHolderWrapper(
                AllProductsRvAdapter.ViewHolderWrapper.TYPE_BODY, productDetail));
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
    private final TextView tvProductName;
    private final TextView tvPurchaseCost;
    private final TextView tvSellingCost;

    public BodyViewHolder(View itemView) {
      super(itemView);
      ivMain = itemView.findViewById(R.id.ivMain);
      civAvatar = itemView.findViewById(R.id.civAvatar);
      tvUserName = itemView.findViewById(R.id.tvUserName);
      tvProductName = itemView.findViewById(R.id.tvProductName);
      tvPurchaseCost = itemView.findViewById(R.id.tvPurchaseCost);
      tvSellingCost = itemView.findViewById(R.id.tvSellingCost);

      tvPurchaseCost.setPaintFlags(tvPurchaseCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void bindViews(ProductDetails item, int position) {
      // Setting transition names on the views
      civAvatar.setTransitionName("AllProductsRvAdapter_civAvatar" + position);
      tvUserName.setTransitionName("AllProductsRvAdapter_tvUserName" + position);
      ivMain.setTransitionName("AllProductsRvAdapter_ivMain" + position);
      tvProductName.setTransitionName("AllProductsRvAdapter_tvProductName" + position);

      // Setting product image into viewHolder
      Picasso.get()
          .load(ApiEndpoints.DIR_PRODUCTS + item.getImage())
          .fit()
          .centerInside()
          .into(ivMain);

      // Setting product name
      tvProductName.setText(item.getName());

      // Setting purchase cost
      final String purchaseCost = item.getPurchaseCost() + " Rs";
      tvPurchaseCost.setText(purchaseCost);

      // Setting selling cost
      final String sellingCost = item.getSellingCost() + " Rs";
      tvSellingCost.setText(sellingCost);

      // Checking if user details exists
      if (item.getUserDetails() != null) {
        // Setting image in avatar
        Picasso.get()
            .load(ApiEndpoints.DIR_AVATAR + item.getUserDetails().getAvatar())
            .fit()
            .centerInside()
            .into(civAvatar);

        // Setting user name
        tvUserName.setText(item.getUserDetails().getFullName());
      }

      // OnClick listener for all item
      // Should show product details when user clicks on it
      itemView.setOnClickListener(
          v -> {
            // Intent to start new ProductDetailActivity
            Intent intent = new Intent(itemView.getContext(), ProductDetailActivity.class);

            // Setting intent action
            intent.setAction(ProductDetailActivity.ACTION_SINGLE_PRODUCT);

            // Putting required data in intent
            intent.putExtra(
                ProductDetailActivity.EXTRA_IMAGE_TRANSITION_NAME, ivMain.getTransitionName());
            intent.putExtra(
                ProductDetailActivity.EXTRA_NAME_TRANSITION_NAME,
                tvProductName.getTransitionName());
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, item);

            // Shared elements pairs
            Pair<View, String> ivMainPair = Pair.create(ivMain, ivMain.getTransitionName());
            Pair<View, String> tvProductPair =
                Pair.create(tvProductName, tvProductName.getTransitionName());

            // Generating shared element transitions animation bundle
            Bundle bundle =
                ActivityOptions.makeSceneTransitionAnimation(
                        (MainActivity) itemView.getContext(), ivMainPair, tvProductPair)
                    .toBundle();

            itemView.getContext().startActivity(intent, bundle);
          });

      // Don't set onClick listener if user if null
      if (item.getUserDetails() == null) return;

      // OnClick Listener for username and avatar
      // Should show user details when click on either avatar or username
      OnClickListener userOnClickListener =
          v -> {
            // Intent to start new UserDetailActivity
            Intent intent = new Intent(itemView.getContext(), UserDetailsActivity.class);

            // Setting intent action
            intent.setAction(UserDetailsActivity.ACTION_SHOW_USER);

            // Putting required data in intent
            intent.putExtra(
                UserDetailsActivity.EXTRA_AVATAR_TRANSITION_NAME, civAvatar.getTransitionName());
            intent.putExtra(
                UserDetailsActivity.EXTRA_FULL_NAME_TRANSITION_NAME,
                tvUserName.getTransitionName());
            intent.putExtra(UserDetailsActivity.EXTRA_USER_DATA, item.getUserDetails());

            // Shared elements pairs
            Pair<View, String> civAvatarPair = Pair.create(civAvatar, civAvatar.getTransitionName());
            Pair<View, String> tvUserNamePair =
                Pair.create(tvUserName, tvUserName.getTransitionName());

            // Generating shared element transitions animation bundle
            Bundle bundle =
                ActivityOptions.makeSceneTransitionAnimation(
                    (MainActivity) itemView.getContext(), civAvatarPair, tvUserNamePair)
                    .toBundle();

            itemView.getContext().startActivity(intent, bundle);
          };

      civAvatar.setOnClickListener(userOnClickListener);
      tvUserName.setOnClickListener(userOnClickListener);
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
