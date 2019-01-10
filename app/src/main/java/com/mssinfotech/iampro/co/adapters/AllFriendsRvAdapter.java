package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mssinfotech.iampro.co.adapters.AllFriendsRvAdapter.loadNewDataListener;
import com.mssinfotech.iampro.co.databinding.ItemAllFriendsBinding;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Response;
import org.json.JSONObject;

public class AllFriendsRvAdapter extends RecyclerView.Adapter<ViewHolder> {
  private static final String TAG = "AllFriendsRvAdapter";

  private List<JSONObject> mList = new ArrayList<>();
  private final LayoutInflater inflater;
  private final Context mContext;
  private loadNewDataListener loadNewDataListener;

  public AllFriendsRvAdapter(Context context) {
    inflater = LayoutInflater.from(context);
    mContext = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemAllFriendsBinding binding = ItemAllFriendsBinding.inflate(inflater, parent, false);
    return new AllFriendsVH(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((AllFriendsVH) holder).bind(mList.get(position));
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  public void setFriendsList(List<JSONObject> list) {
    if (list == null) return;
    this.mList = list;
    notifyDataSetChanged();
  }

  public void setLoadNewDataListener(
      AllFriendsRvAdapter.loadNewDataListener loadNewDataListener) {
    this.loadNewDataListener = loadNewDataListener;
  }

  private class AllFriendsVH extends ViewHolder {
    private final ItemAllFriendsBinding binding;

    public AllFriendsVH(ItemAllFriendsBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

      // Changing tint of all button icons
      this.binding.btnImages.getCompoundDrawables()[1].setTint(Color.BLACK);
      this.binding.btnVideos.getCompoundDrawables()[1].setTint(Color.BLACK);
      this.binding.btnProducts.getCompoundDrawables()[1].setTint(Color.BLACK);
      this.binding.btnFriends.getCompoundDrawables()[1].setTint(Color.BLACK);
      this.binding.btnProvide.getCompoundDrawables()[1].setTint(Color.BLACK);
      this.binding.btnDemand.getCompoundDrawables()[1].setTint(Color.BLACK);

    }

    public void bind(JSONObject details) {
      JSONObject userDetails = details.optJSONObject("user_detail");
      if (userDetails != null) {
        // Username
        binding.tvUserName.setText(userDetails.optString("fullname", "Name Not Found"));
        binding.tvCategory.setText(userDetails.optString("category", ""));

        // Avatar Image
        Picasso.get()
            .load(ApiEndpoints.DIR_AVATAR + userDetails.optString("avatar"))
            .centerInside()
            .fit()
            .into(binding.civAvatar);

        // Address
        binding.tvAddress.setText(userDetails.optString("address"));
      }

      // Filling Counts
      binding.btnImages.setText(userDetails.optString("total_img") + " Images");
      binding.btnVideos.setText(userDetails.optString("total_video") + " Videos");
      binding.btnProducts.setText(userDetails.optString("total_product") + " Products");
      binding.btnFriends.setText(userDetails.optString("total_friend") + " Friends");
      binding.btnProvide.setText(userDetails.optString("total_product_provide") + " Provide");
      binding.btnDemand.setText(userDetails.optString("total_product_demand") + " Demand");

      binding.btnRemoveFriend.setOnClickListener(v -> {
        new AlertDialog.Builder(mContext)
            .setTitle("Caution")
            .setMessage("Delete Friend")
            .setNegativeButton("cancel", null)
            .setPositiveButton("delete", new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                AndroidNetworking.get("http://www.iampro.co/ajax/profile.php?type=delete_friend&id=14&tid=6")
                    .addQueryParameter("type", "delete_friend")
                    .addQueryParameter("id", details.optString("user_id"))
                    .addQueryParameter("tid", details.optString("id"))
                    .build()
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                      @Override
                      public void onResponse(Response okHttpResponse, JSONObject response) {
                        if (response.optString("status").equalsIgnoreCase("success")) {
                          Toast.makeText(mContext.getApplicationContext(), "Friend Removed", Toast.LENGTH_SHORT).show();
                          if (loadNewDataListener != null) loadNewDataListener.loadNewData();
                        }
                      }

                      @Override
                      public void onError(ANError anError) {
                        Log.e(TAG, "Friend remove failed", anError);
                        Toast.makeText(mContext.getApplicationContext(), "Friend Remove Failed", Toast.LENGTH_SHORT).show();
                      }
                    });
              }
            }).show();
      });
    }
  }

  public interface loadNewDataListener {
    void loadNewData();
  }
}
