package com.mssinfotech.iampro.co.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.activities.ImageGalleryActivity;
import com.mssinfotech.iampro.co.adapters.ImageGalleryRvAdapter;
import com.mssinfotech.iampro.co.adapters.ImageGalleryRvAdapter.ImageGalleryAdapterListeners;
import com.mssinfotech.iampro.co.databinding.FragmentImageGalleryBinding;
import com.mssinfotech.iampro.co.viewmodels.ImageGalleryViewModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.esafirm.imagepicker.model.Image;
import java.util.List;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGalleryFragment extends Fragment implements ImageGalleryAdapterListeners {
  private static final String TAG = "ImageGalleryFragment";
  private static final String ARG_UID = "arg_uid";

  /* *************************
   * BINDINGS
   ************************* */
  FragmentImageGalleryBinding binding;

  /* *************************
   * ViewModels
   ************************* */
  ImageGalleryViewModel viewModel;

  /* *************************
   * ADAPTERS
   ************************* */
  ImageGalleryRvAdapter adapter;

  public static ImageGalleryFragment newInstance(int uid) {
    final Bundle bundle = new Bundle();
    bundle.putInt(ARG_UID, uid);

    ImageGalleryFragment fragment = new ImageGalleryFragment();
    fragment.setArguments(bundle);

    return fragment;
  }

  public ImageGalleryFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_gallery, container, false);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Image Gallery");
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initObjects();
    setupVM();
  }

  private void initObjects() {
    adapter = new ImageGalleryRvAdapter(getContext());
    adapter.setListeners(this);
    binding.rvMain.setAdapter(adapter);
  }

  private void setupVM() {
    AndroidViewModelFactory factory = new AndroidViewModelFactory(getActivity().getApplication());
    viewModel = ViewModelProviders.of(this, factory).get(ImageGalleryViewModel.class);

    int uid = getArguments().getInt(ARG_UID);
    viewModel.loadImageGalleryDetails(uid);
    viewModel.getImageGalleryDetails().observe(this, jsonObjects -> {
      adapter.setImageData(jsonObjects);
    });
  }

  @Override
  public void viewAlbumClicked(JSONObject details) {
    ((ImageGalleryActivity)getActivity()).showAlbumFragment(details);
  }

  @Override
  public void deleteAlbumClicked(JSONObject details) {
    final String id = details.optString("id");
    new AlertDialog.Builder(getActivity())
        .setTitle("Caution")
        .setMessage("Delete Album")
        .setNegativeButton("cancel", null)
        .setPositiveButton("delete", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            AndroidNetworking.get("http://www.iampro.co/ajax/profile.php")
                .addQueryParameter("type", "deleteAlbem")
                .addQueryParameter("id", id)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                  @Override
                  public void onResponse(Response okHttpResponse, JSONObject response) {
                    int uid = getArguments().getInt(ARG_UID);
                    viewModel.loadImageGalleryDetails(uid);
                  }

                  @Override
                  public void onError(ANError anError) {
                    Log.e(TAG, "deleteAlbumClicked failed", anError);
                  }
                });
          }
        })
        .show();
  }
}
