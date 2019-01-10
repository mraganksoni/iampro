package com.mssinfotech.iampro.co.dialogs;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.databinding.DialogWriteReviewBinding;
import com.mssinfotech.iampro.co.utils.VolleyUtil;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

public class WriteCommentDialog extends DialogFragment {
  public static final String TAG = "WriteCommentDialog";

  public static final String ARG_USER_ID = "arg_user_id";
  public static final String ARG_PRODUCT_ID = "arg_product_id";

  /* **************************************
   * Listeners
   * **************************************/
  private CommentStatusListener commentStatusListener;

  DialogWriteReviewBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.dialog_write_review, container, true);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

    int accentColorDark = getResources().getColor(R.color.colorAccentDark);
    getDialog()
        .getWindow()
        .setBackgroundDrawable(
            new ColorDrawable(ColorUtils.setAlphaComponent(accentColorDark, 150)));

    return binding.getRoot();
  }

  public static synchronized WriteCommentDialog newInstance(int userId, int productId) {
    WriteCommentDialog instance = new WriteCommentDialog();

    instance.setArguments(userId, productId);
    return instance;
  }

  public void setArguments(int userId, int productId) {
    Bundle bundle = new Bundle();

    // putting data in bundle
    bundle.putInt(ARG_PRODUCT_ID, productId);
    bundle.putInt(ARG_USER_ID, userId);

    setArguments(bundle);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.ibtnClose.setOnClickListener(
        v -> {
          dismiss();
        });

    binding.btnSend.setOnClickListener(
        v -> {
          if (validate()) {
            binding.flProgress.setVisibility(View.VISIBLE);
            Bundle arguments = getArguments();
            sendCommentData(
                binding.etComment.getText().toString(),
                arguments.getInt(ARG_PRODUCT_ID),
                arguments.getInt(ARG_USER_ID));
          }
        });
  }

  @Override
  public void onStart() {
    super.onStart();

    getDialog().getWindow().setWindowAnimations(R.style.DialogFadeAnimation);
  }

  public void setCommentStatusListener(CommentStatusListener listener) {
    this.commentStatusListener = listener;
  }

  private boolean validate() {
    boolean flag = true;

    final String comment = binding.etComment.getText().toString();
    if (comment.isEmpty()) {
      flag = false;
      if (!binding.tilComment.isErrorEnabled()) binding.tilComment.setErrorEnabled(true);
      binding.tilComment.setError("Comment Required");
    } else {
      if (binding.tilComment.isErrorEnabled()) binding.tilComment.setErrorEnabled(false);
    }

    return flag;
  }

  private void sendCommentData(String comment, int productId, int userId) {

    Uri uri =
        new Uri.Builder()
            .scheme("http")
            .authority("www.iampro.co")
            .appendPath("ajax")
            .appendPath("product.php")
            .appendQueryParameter("type", "product_review")
            .appendQueryParameter("id", userId == 0 ? "" : Integer.toString(userId))
            .appendQueryParameter("pid", Integer.toString(productId))
            .appendQueryParameter("comment", comment)
            .build();
    JsonObjectRequest commentRequest =
        new JsonObjectRequest(
            uri.toString(),
            null,
            response -> {
              Log.d(TAG, "commentRequest: successfully saved comment ");
              Toast.makeText(getContext(), "Comment added", Toast.LENGTH_SHORT).show();
              if (commentStatusListener != null) commentStatusListener.onSuccess();
              dismiss();
            },
            error -> {
              Log.e(TAG, "commentRequest failed", error.getCause());
              if (commentStatusListener != null) commentStatusListener.onError(error.getMessage());
              Toast.makeText(getContext(), "Comment add failed", Toast.LENGTH_SHORT).show();
              dismiss();
            });
    VolleyUtil.getInstance(getActivity().getApplicationContext()).addRequest(commentRequest);
  }

  public interface CommentStatusListener {
    void onSuccess();
    void onError(String error);
  }
}
