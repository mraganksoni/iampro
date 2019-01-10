package com.mssinfotech.iampro.co.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapters.HomeFragmentPagerAdapter;
import com.mssinfotech.iampro.co.events.CircleMenuButtonClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    /***************************
     * ADAPTERS
     **************************/
    HomeFragmentPagerAdapter pagerAdapter;

    /***************************
     * VIEW REFERENCES
     **************************/
    ViewPager viewPager;
    TabLayout tabLayout;

    /***************************
     * LISTENERS
     **************************/


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        viewPager.setOffscreenPageLimit(3);
        pagerAdapter = new HomeFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        updateTabItems();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void updateTabItems() {
        final int count = pagerAdapter.getCount();
        final int color = getResources().getColor(R.color.textOnPrimary);
        for (int i=0; i<count; ++i) {
            Drawable drawable = getResources().getDrawable(pagerAdapter.getIcon(i));
            DrawableCompat.setTint(drawable, color);
            tabLayout.getTabAt(i).setIcon(drawable);
        }
    }

    @Subscribe
    public void onCircleMenuButtonClicked(CircleMenuButtonClick e) {
        switch (e.tag) {
            case "images":
                viewPager.setCurrentItem(1);
                break;
            case "videos":
                viewPager.setCurrentItem(2);
                break;
            case "members":
                viewPager.setCurrentItem(3);
                break;
            case "products":
                viewPager.setCurrentItem(4);
                break;
            case "provide":
                viewPager.setCurrentItem(5);
                break;
            case "demand":
                viewPager.setCurrentItem(6);
                break;
        }
    }

}
