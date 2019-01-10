package com.mssinfotech.iampro.co.fragments;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.AllUsersRvAdapter;
import com.mssinfotech.iampro.co.adapters.AllVideosRvAdapter;
import com.mssinfotech.iampro.co.viewmodels.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildUsersFragment extends Fragment {

    /***************************
     * VIEWMODEL REFERENCES
     **************************/
    MainViewModel viewModel;

    /***************************
     * VIEW REFERENCES
     **************************/
    RecyclerView rvUsersAll;

    /***************************
     * ADAPTER
     **************************/
    AllUsersRvAdapter allUsersRvAdapter;

    /***************************
     * LISTENERS
     **************************/


    public ChildUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Getting instance of MainViewModel
        ViewModelProvider.AndroidViewModelFactory viewModelFactory =
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        // Trigger load all users data in MainViewModel
        viewModel.loadAllUsersData();

        // Settingup All userser Recyclerview (rvUsersAll)
        rvUsersAll = view.findViewById(R.id.rvUsersAll);
        rvUsersAll.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        allUsersRvAdapter = new AllUsersRvAdapter(getContext());
        rvUsersAll.setAdapter(allUsersRvAdapter);

        // listening for all user dat|a from view model
        viewModel.getAllUsersList().observe(this, userDetails -> {
            allUsersRvAdapter.setUserListData(userDetails);
        });
    }
}
