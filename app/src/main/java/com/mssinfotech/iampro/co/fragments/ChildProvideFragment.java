package com.mssinfotech.iampro.co.fragments;


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

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.AllProductsRvAdapter;
import com.mssinfotech.iampro.co.viewmodels.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildProvideFragment extends Fragment {

    /***************************
     * VIEWMODEL REFERENCES
     **************************/
    MainViewModel viewModel;

    /***************************
     * VIEW REFERENCES
     **************************/
    RecyclerView rvProvideAll;

    /***************************
     * ADAPTER
     **************************/
    AllProductsRvAdapter allProvidesRvAdapter;

    /***************************
     * LISTENERS
     **************************/


    public ChildProvideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_provide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory =
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        // trigger data update in viewModel
        viewModel.loadAllProvidesData();

        // Setting recycler view and also adding adapter to it
        rvProvideAll = view.findViewById(R.id.rvProvideAll);
        allProvidesRvAdapter = new AllProductsRvAdapter(getContext());
        GridLayoutManager rvProvidesAllLM = ((GridLayoutManager)rvProvideAll.getLayoutManager());
        rvProvidesAllLM.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final int itemType = allProvidesRvAdapter.getItemViewType(position);
                if (itemType == AllProductsRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
                        itemType == AllProductsRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rvProvideAll.setAdapter(allProvidesRvAdapter);

        // Listening for all products data changes
        viewModel.getAllProviders().observe(this, stringListMap -> {
            allProvidesRvAdapter.setData(stringListMap);
        });


    }
}
