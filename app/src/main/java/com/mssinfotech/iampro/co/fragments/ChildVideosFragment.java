package com.mssinfotech.iampro.co.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.AllImagesRvAdapter;
import com.mssinfotech.iampro.co.adapters.AllVideosRvAdapter;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.viewmodels.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildVideosFragment extends Fragment {

    /***************************
     * VIEWMODEL REFERENCES
     **************************/
    MainViewModel viewModel;

    /***************************
     * VIEW REFERENCES
     **************************/
    RecyclerView rvVideosAll;

    /***************************
     * ADAPTER
     **************************/
    AllVideosRvAdapter allVideosRvAdapter;

    /***************************
     * LISTENERS
     **************************/


    public ChildVideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelProvider.AndroidViewModelFactory viewModelFactory =
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        // triggle video data load in viewmodel
        viewModel.loadAllVideosData();

        // Finding and setting up recycler view
        rvVideosAll = view.findViewById(R.id.rvVideosAll);
        allVideosRvAdapter = new AllVideosRvAdapter(getContext());
        rvVideosAll.setAdapter(allVideosRvAdapter);


        // getting videos information from viewmodel
        viewModel.getAllVideoList().observe(this, new Observer<Map<String, List<ImageDetails>>>() {
            @Override
            public void onChanged(@Nullable Map<String, List<ImageDetails>> stringListMap) {
                allVideosRvAdapter.setData(stringListMap);
            }
        });
    }
}
