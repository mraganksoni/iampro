package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.TopSliderImageModel;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {

  private static final String TAG = "TopImageSliderAdapter";

  private final LayoutInflater layoutInflater;
  private List<String> modelList = new ArrayList<>();
  private String remoteDir;

  public ImageSliderAdapter(Context mContext) {
    this.layoutInflater = LayoutInflater.from(mContext);
    this.remoteDir = ApiEndpoints.DIR_SLIDER;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view = layoutInflater.inflate(R.layout.item_top_slider_image, container, false);
    ImageView imageView = view.findViewById(R.id.imageView);

    // Show default image if no items in adapter
    if (modelList.size() == 0) {
      Picasso.get().load(R.drawable.no_image_available).fit().centerCrop().into(imageView);
      container.addView(view);
      return view;
    }

    final String imagePath = remoteDir + modelList.get(position);
    Log.d(TAG, "instantiateItem: imageView: " + imagePath);
    Picasso.get()
        .load(imagePath)
        .fit()
        .centerInside()
        .into(imageView);
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View)object);
  }

  @Override
  public int getCount() {
    if (modelList.size() == 0) {
      return 1;
    }
    return modelList.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  public void setImages(List<String> imagesList) {
    this.modelList = imagesList;
    notifyDataSetChanged();
  }

  public void setRemoteDir(String remoteDir) {
    this.remoteDir = remoteDir;
  }
}
