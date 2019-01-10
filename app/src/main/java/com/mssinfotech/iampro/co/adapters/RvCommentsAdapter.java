package com.mssinfotech.iampro.co.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.Comment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RvCommentsAdapter extends Adapter<ViewHolder> {
  public static final int TYPE_NO_ITEMS = 10;
  public static final int TYPE_NORMAL = 0;

  private final Context context;
  private final LayoutInflater layoutInflater;

  private ArrayList<Comment> mModelList = new ArrayList<>();

  public RvCommentsAdapter(Context context) {
    this.context = context;
    this.layoutInflater = LayoutInflater.from(context);
  }

  public RvCommentsAdapter(Context context, LayoutInflater layoutInflater) {
    this.context = context;
    this.layoutInflater = layoutInflater;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == TYPE_NO_ITEMS) {
      View view = layoutInflater.inflate(R.layout.item_comment_no_item_found, parent, false);
      return new NoItemViewHolder(view);
    }

    View view = layoutInflater.inflate(R.layout.item_comment, parent, false);
    return new NormalViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if (mModelList.size() == 0) return;

    ((NormalViewHolder) holder).bindViewHolder(mModelList.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    if (mModelList.size() == 0) {
      return TYPE_NO_ITEMS;
    }
    return TYPE_NORMAL;
  }

  @Override
  public int getItemCount() {
    if (mModelList.size() == 0) {
      return 1;
    }
    return mModelList.size();
  }

  public void setCommentsData(ArrayList<Comment> commentsList) {
    this.mModelList = commentsList;
    notifyDataSetChanged();
  }

  private class NormalViewHolder extends ViewHolder {
    private final CircularImageView civAvatar;
    private final TextView tvTitle;
    private final TextView tvSecondLine;
    private final TextView tvThirdLine;

    public NormalViewHolder(View itemView) {
      super(itemView);

      civAvatar = itemView.findViewById(R.id.civAvatar);
      tvTitle = itemView.findViewById(R.id.tvTitle);
      tvSecondLine = itemView.findViewById(R.id.tvSecondLine);
      tvThirdLine = itemView.findViewById(R.id.tvThirdLine);
    }

    public void bindViewHolder(Comment itemModel) {
      if (itemModel.getUserImage() != null && !itemModel.getUserImage().isEmpty()) {
        Picasso.get().load(itemModel.getUserImage()).fit().centerCrop(Gravity.CENTER).into(civAvatar);
      }
      if (itemModel.getFullName() != null && !itemModel.getFullName().isEmpty()) {
        tvTitle.setText(itemModel.getFullName());
      } else {
        tvTitle.setText("Guest User");
      }
      if (itemModel.getAgo() != null && !itemModel.getAgo().isEmpty()) {
        tvSecondLine.setText(itemModel.getAgo());
      }
      if (itemModel.getComment() != null && !itemModel.getComment().isEmpty()) {
        tvThirdLine.setText(itemModel.getComment());
      } else  {
        tvThirdLine.setText("Ahhh... Crap, Empty Comment");
      }
    }
  }

  private class NoItemViewHolder extends ViewHolder {

    public NoItemViewHolder(View itemView) {
      super(itemView);
    }
  }
}
