package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.TopSliderImageModel;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;

public class TopImageSliderAdapter extends PagerAdapter {
    private static final String TAG = "TopImageSliderAdapter";

    private final Context           mContext;
    private final LayoutInflater    layoutInflater;
    private List<TopSliderImageModel> modelList = new ArrayList<>();

    public TopImageSliderAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_top_slider_image, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        final String imagePath = ApiEndpoints.DIR_SLIDER + modelList.get(position).getImage();
        Log.d(TAG, "instantiateItem: imageView: " + imagePath);
        Picasso.get()
                .load(imagePath)
                //.resize(500, 0)
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
        return modelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setImagesList(List<TopSliderImageModel> imagesList) {
        if (imagesList == null) {
            return;
        }
        this.modelList = imagesList;
        notifyDataSetChanged();
    }
}
