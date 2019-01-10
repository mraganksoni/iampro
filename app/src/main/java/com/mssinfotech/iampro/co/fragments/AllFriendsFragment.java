package com.mssinfotech.iampro.co.fragments;


import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.AllFriendsRvAdapter;
import com.mssinfotech.iampro.co.adapters.AllFriendsRvAdapter.loadNewDataListener;
import com.mssinfotech.iampro.co.databinding.FragmentAllFriendsBinding;
import com.mssinfotech.iampro.co.viewmodels.JoinFriendViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFriendsFragment extends Fragment {
  /* *************************
   * BINDINGS
   ************************* */
  FragmentAllFriendsBinding binding;

  /* *************************
   * BINDINGS
   ************************* */
  JoinFriendViewModel viewModel;

  /* *************************
   * Adapters
   ************************* */
  AllFriendsRvAdapter adapter;

  public AllFriendsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_friends, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initObjects();
    setupViewModel();
    hookIntoViewModel();
  }

  private void setupViewModel() {
    AndroidViewModelFactory factory = new AndroidViewModelFactory(getActivity().getApplication());
    viewModel = ViewModelProviders.of(this,factory).get(JoinFriendViewModel.class);
    viewModel.loadAllFriendsList();
  }

  private void hookIntoViewModel() {
    viewModel.getFriendsList().observe(this, jsonObjects -> {
      adapter.setFriendsList(jsonObjects);
    });
  }

  private void initObjects() {
    adapter = new AllFriendsRvAdapter(getContext());
    binding.rvMain.setAdapter(adapter);
    adapter.setLoadNewDataListener(() -> {
      viewModel.loadAllFriendsList();
    });
  }
}
