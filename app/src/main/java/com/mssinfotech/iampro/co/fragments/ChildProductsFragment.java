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
import com.mssinfotech.iampro.co.adapters.AllImagesRvAdapter;
import com.mssinfotech.iampro.co.adapters.AllProductsRvAdapter;
import com.mssinfotech.iampro.co.viewmodels.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildProductsFragment extends Fragment {

    /***************************
     * VIEWMODEL REFERENCES
     **************************/
    MainViewModel viewModel;

    /***************************
     * VIEW REFERENCES
     **************************/
    RecyclerView rvProductsAll;

    /***************************
     * ADAPTER
     **************************/
    AllProductsRvAdapter allProductsRvAdapter;

    /***************************
     * LISTENERS
     **************************/



    public ChildProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider.AndroidViewModelFactory viewModelFactory =
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        // trigger data update in viewModel
        viewModel.loadAllProductsData();

        // Setting recycler view and also adding adapter to it
        rvProductsAll = view.findViewById(R.id.rvProductsAll);
        allProductsRvAdapter = new AllProductsRvAdapter(getContext());
        GridLayoutManager rvProductsAllLM = ((GridLayoutManager)rvProductsAll.getLayoutManager());
        rvProductsAllLM.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                final int itemType = allProductsRvAdapter.getItemViewType(position);
                if (itemType == AllImagesRvAdapter.ViewHolderWrapper.TYPE_HEAD ||
                        itemType == AllImagesRvAdapter.ViewHolderWrapper.TYPE_NO_ITEM_FOUND) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rvProductsAll.setAdapter(allProductsRvAdapter);

        // Listening for all products data changes
        viewModel.getAllProducts().observe(this, stringListMap -> {
            allProductsRvAdapter.setData(stringListMap);
        });

    }
}
