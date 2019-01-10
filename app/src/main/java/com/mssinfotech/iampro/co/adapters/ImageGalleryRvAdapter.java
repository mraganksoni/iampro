package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.mssinfotech.iampro.co.databinding.ItemImageGalleryBinding;

public class ImageGalleryRvAdapter extends Adapter<ViewHolder> {

  private final LayoutInflater inflater;
  private Context mContext;
  private List<JSONObject> mList = new ArrayList<>();
  private ImageGalleryAdapterListeners imageGalleryAdapterListeners;

  public ImageGalleryRvAdapter(Context context) {
    this.mContext = context;
    this.inflater = LayoutInflater.from(this.mContext);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ImageGalleryVH(ItemImageGalleryBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((ImageGalleryVH) holder).bind(mList.get(position));
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  public void setImageData(List<JSONObject> jsonObjectList) {
    if (jsonObjectList == null) return;

    this.mList = jsonObjectList;
    notifyDataSetChanged();
  }

  public void setListeners(ImageGalleryAdapterListeners listeners) {
    this.imageGalleryAdapterListeners = listeners;
  }

  private class ImageGalleryVH extends ViewHolder {
    private final ItemImageGalleryBinding binding;

    public ImageGalleryVH(ItemImageGalleryBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(JSONObject details) {
      // MainImage
      Picasso.get()
          .load(ApiEndpoints.DIR_PHOTOS + details.optString("album_image"))
          .fit()
          .centerInside()
          .into(binding.ivMain);

      binding.tvName.setText(details.optString("name"));
      binding.tvCount.setText(details.optString("total_pics") + " photo");
      binding.tvCommentCount.setText(details.optString("total_comment"));
      binding.rbMain.setRating((float)details.optDouble("allrating"));

      binding.btnViewAlbum.setOnClickListener(v -> {
        if (imageGalleryAdapterListeners != null) {
          imageGalleryAdapterListeners.viewAlbumClicked(details);
        }
      });

      binding.btnDeleteAlbum.setOnClickListener(v -> {
        if (imageGalleryAdapterListeners != null) {
          imageGalleryAdapterListeners.deleteAlbumClicked(details);
        }
      });
    }
  }

  public interface ImageGalleryAdapterListeners {
    void viewAlbumClicked(JSONObject details);
    void deleteAlbumClicked(JSONObject details);
  }
}
