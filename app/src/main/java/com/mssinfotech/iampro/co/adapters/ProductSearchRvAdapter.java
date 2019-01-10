package com.mssinfotech.iampro.co.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.mssinfotech.iampro.co.activities.ProductDetailActivity;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductSearchRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ViewHolderWrapper> mItemsList = new ArrayList<>();
  private Map<String, List<ProductDetails>> originalList = new HashMap<>();
  private final LayoutInflater inflater;
  private String category;
  private PagerAdapter vpSliderAdapter;

  public ProductSearchRvAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == ViewHolderWrapper.TYPE_HEAD) {
      View view = inflater.inflate(R.layout.item_all_images_head, parent, false);
      return new HeadViewHolder(view);
    } else if (viewType == ViewHolderWrapper.TYPE_BODY) {
      View view = inflater.inflate(R.layout.item_all_products_body, parent, false);
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
      ((BodyViewHolder) holder).bindViews((ProductDetails) mItemsList.get(position).payLoad, position);
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

  public void setData(Map<String, List<ProductDetails>> dataList) {
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
    Set<String> categoriesList = originalList.keySet();
    for (String category : categoriesList) {
      mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_HEAD, category));

      // if category list don't have anything then just show
      // no item view holder
      if (originalList.get(category).size() <= 0) {
        mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_NO_ITEM_FOUND, null));
        // continue;
      }

      for (ProductDetails productDetail : originalList.get(category)) {
        mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_BODY, productDetail));
      }
    }
  }

  private void parseWithFilter(String filter) {
    Set<String> categoriesList = originalList.keySet();
    for (String category : categoriesList) {
      // Filter data according to categories
      if (!category.equalsIgnoreCase(filter)) {
        continue;
      }

      // Add Category name to items list
      mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_HEAD, category));

      // if category list don't have anything then just show
      // no item view holder
      if (originalList.get(category).size() <= 0) {
        mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_NO_ITEM_FOUND, null));
        // continue;
      }

      for (ProductDetails productDetail : originalList.get(category)) {
        mItemsList.add(new ViewHolderWrapper(ViewHolderWrapper.TYPE_BODY, productDetail));
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
      // Setting transition names on views
      ivMain.setTransitionName("ProductSearchRvAdapter_ivMain" + position);
      tvProductName.setTransitionName("ProductSearchRvAdapter_tvProductName" + position);

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

      itemView.setOnClickListener(
          v -> {
            Intent intent = new Intent(itemView.getContext(), ProductDetailActivity.class);

            // Putting product data in intent
            intent.setAction(ProductDetailActivity.ACTION_SINGLE_PRODUCT);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, item);
            intent.putExtra(ProductDetailActivity.EXTRA_IMAGE_TRANSITION_NAME, ivMain.getTransitionName());
            intent.putExtra(ProductDetailActivity.EXTRA_NAME_TRANSITION_NAME, tvProductName.getTransitionName());

            Pair<View, String> ivMainPair = Pair.create(ivMain, ivMain.getTransitionName());
            Pair<View, String> tvProductNamePair = Pair.create(tvProductName, tvProductName.getTransitionName());
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                (Activity) itemView.getContext(), ivMainPair, tvProductNamePair).toBundle();

            itemView.getContext().startActivity(intent, bundle);
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
    public static final int TYPE_HEADER = 10;

    public final int type;
    public final Object payLoad;

    public ViewHolderWrapper(int type, Object payLoad) {
      this.type = type;
      this.payLoad = payLoad;
    }
  }
}
