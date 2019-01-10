package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mssinfotech.iampro.co.activities.MainActivity;
import com.mssinfotech.iampro.co.activities.UserDetailsActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;

public class AllUsersRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final LayoutInflater inflater;
  private List<UserDetails> mUsersList = new ArrayList<>();

  public AllUsersRvAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.item_all_users_body, parent, false);
    return new BodyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ((BodyViewHolder) holder).bindViews(mUsersList.get(position), position);
  }

  @Override
  public int getItemCount() {
    return mUsersList.size();
  }

  public void setUserListData(List<UserDetails> userListData) {
    mUsersList = userListData;
    notifyDataSetChanged();
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
      // Setting transition name on views
      civAvatar.setTransitionName("AllUsersRvAdapter_ivAvatar" + position);
      tvName.setTransitionName("AllUsersRvAdapter_tvName" + position);

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

      // Attaching onClick listener on container item
      itemView.setOnClickListener(
          v -> {
            Intent intent = new Intent(itemView.getContext(), UserDetailsActivity.class);

            // Setting action of intent so activity will process user data
            intent.setAction(UserDetailsActivity.ACTION_SHOW_USER);

            // Putting extra intent data required for data processing
            intent.putExtra(
                UserDetailsActivity.EXTRA_AVATAR_TRANSITION_NAME, civAvatar.getTransitionName());
            intent.putExtra(
                UserDetailsActivity.EXTRA_FULL_NAME_TRANSITION_NAME, tvName.getTransitionName());
            intent.putExtra(UserDetailsActivity.EXTRA_USER_DATA, userDetails);

            // Generation transition animation bundle
            Pair<View, String> civAvatarPair =
                Pair.create(civAvatar, civAvatar.getTransitionName());
            Pair<View, String> tvNamePair = Pair.create(tvName, tvName.getTransitionName());
            Bundle bundle =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    ((MainActivity) itemView.getContext()), civAvatarPair, tvNamePair)
                    .toBundle();

            ActivityCompat.startActivity(itemView.getContext(), intent, bundle);
          });
    }
  }
}
