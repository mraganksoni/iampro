package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mssinfotech.iampro.co.databinding.ItemImageGalleryAlbumBinding;

public class ImageGalleryAlbumRvAdapter extends Adapter<ViewHolder> {

  private final LayoutInflater inflater;
  private Context mContext;
  private List<JSONObject> mList = new ArrayList<>();

  public ImageGalleryAlbumRvAdapter(Context context) {
    mContext = context;
    inflater = LayoutInflater.from(mContext);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ImageGalleryAlbumVH(ItemImageGalleryAlbumBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((ImageGalleryAlbumVH) holder).bind(mList.get(position));
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  public void setData(List<JSONObject> dataList) {
    if (dataList == null) return;
    mList = dataList;
    notifyDataSetChanged();
  }

  public void setData(JSONArray jsonArray) {
    if (jsonArray == null) return;
    mList.clear();
    for (int i = 0; i < jsonArray.length(); ++i) {
      mList.add(jsonArray.optJSONObject(i));
    }
    notifyDataSetChanged();
  }

  private class ImageGalleryAlbumVH extends ViewHolder {
    private final ItemImageGalleryAlbumBinding binding;

    public ImageGalleryAlbumVH(ItemImageGalleryAlbumBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(JSONObject details) {
      Picasso.get()
          .load(ApiEndpoints.DIR_PHOTOS + details.optString("image"))
          .fit()
          .centerInside()
          .into(binding.ivMain);

      binding.tvTitle.setText(details.optString("name"));
      //binding.tvCategory.setText(details.optString());
      binding.rbMain.setRating(3.0f);
    }
  }
}
