package com.mssinfotech.iampro.co.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.AllImagesRvAdapter;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.viewmodels.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildImagesFragment extends Fragment {

    /***************************
     * VIEWMODEL REFERENCES
     **************************/
    MainViewModel viewModel;

    /***************************
     * VIEW REFERENCES
     **************************/
    RecyclerView rvImagesAll;

    /***************************
     * ADAPTER
     **************************/
    AllImagesRvAdapter allImagesRvAdapter;

    /***************************
     * LISTENERS
     **************************/


    public ChildImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory =
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        // Request viewmodel to load images
        viewModel.loadAllImagesData();

        // Setting RecyclerView
        rvImagesAll = view.findViewById(R.id.rvImagesAll);
        allImagesRvAdapter = new AllImagesRvAdapter(getContext());
        GridLayoutManager rvImagesAllLM = ((GridLayoutManager)rvImagesAll.getLayoutManager());
        rvImagesAllLM.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final int itemType = allImagesRvAdapter.getItemViewType(position);
                if (itemType == AllImagesRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
                        itemType == AllImagesRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        rvImagesAll.setAdapter(allImagesRvAdapter);

        viewModel.getAllImageList().observe(this, new Observer<Map<String, List<ImageDetails>>>() {
            @Override
            public void onChanged(@Nullable Map<String, List<ImageDetails>> stringListMap) {
                allImagesRvAdapter.setData(stringListMap);
            }
        });
    }
}
