package com.mssinfotech.iampro.co.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.ImageGalleryAlbumRvAdapter;
import com.mssinfotech.iampro.co.databinding.FragmentImageGalleryAlbumBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass. Use the {@link ImageGalleryAlbumFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class ImageGalleryAlbumFragment extends Fragment {

  private static final String ARG_ALBUM_DETAILS = "album_details";

  private FragmentImageGalleryAlbumBinding binding;
  private JSONObject details;
  private ImageGalleryAlbumRvAdapter adapter;

  public ImageGalleryAlbumFragment() {
    // Required empty public constructor
  }

  public static ImageGalleryAlbumFragment newInstance(JSONObject details) {
    ImageGalleryAlbumFragment fragment = new ImageGalleryAlbumFragment();
    Bundle args = new Bundle();
    args.putString(ARG_ALBUM_DETAILS, details.toString());
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      final String detailsJsonString = getArguments().getString(ARG_ALBUM_DETAILS);
      try {
        details = new JSONObject(detailsJsonString);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentImageGalleryAlbumBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.tvAlbumTitle.setText(details.optString("name"));

    adapter = new ImageGalleryAlbumRvAdapter(getContext());
    adapter.setData(details.optJSONArray("pics"));
    binding.rvMain.setAdapter(adapter);
  }
}
