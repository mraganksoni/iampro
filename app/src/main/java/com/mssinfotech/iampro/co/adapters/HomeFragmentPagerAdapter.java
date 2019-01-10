package com.mssinfotech.iampro.co.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.fragments.ChildDemandFragment;
import com.mssinfotech.iampro.co.fragments.ChildHomeFragment;
import com.mssinfotech.iampro.co.fragments.ChildImagesFragment;
import com.mssinfotech.iampro.co.fragments.ChildProductsFragment;
import com.mssinfotech.iampro.co.fragments.ChildProvideFragment;
import com.mssinfotech.iampro.co.fragments.ChildUsersFragment;
import com.mssinfotech.iampro.co.fragments.ChildVideosFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private ChildHomeFragment       childHomeFragment;
    private ChildImagesFragment     childImagesFragment;
    private ChildVideosFragment     childVideosFragment;
    private ChildUsersFragment      childUsersFragment;
    private ChildProductsFragment   childProductsFragment;
    private ChildProvideFragment    childProvideFragment;
    private ChildDemandFragment     childDemandFragment;

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            if (childHomeFragment == null) {
                this.childHomeFragment = new ChildHomeFragment();
            }
            return childHomeFragment;
        } else if (position == 1) {
            if (childImagesFragment == null) {
                this.childImagesFragment = new ChildImagesFragment();
            }
            return childImagesFragment;
        } else if (position == 2) {
            if (childVideosFragment == null) {
                this.childVideosFragment = new ChildVideosFragment();
            }
            return childVideosFragment;
        } else if (position == 3) {
            if (childUsersFragment == null) {
                this.childUsersFragment = new ChildUsersFragment();
            }
            return childUsersFragment;
        } else if (position == 4) {
            if (childProductsFragment == null) {
                this.childProductsFragment = new ChildProductsFragment();
            }
            return childProductsFragment;
        } else if (position == 5) {
            if (childProvideFragment == null) {
                this.childProvideFragment = new ChildProvideFragment();
            }
            return childProvideFragment;
        } else {
            if (childDemandFragment == null) {
                this.childDemandFragment = new ChildDemandFragment();
            }
            return childDemandFragment;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Home";
        } else if (position == 1) {
            return "Images";
        } else if (position == 2) {
            return "Videos";
        } else if (position == 3) {
            return "Users";
        } else if (position == 4) {
            return "Products";
        } else if (position == 5) {
            return "Provide";
        } else {
            return "demand";
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    public int getIcon(int position) {
        if (position == 0){
            return R.drawable.ic_home_black_24dp;
        } else if (position == 1) {
            return R.drawable.ic_image_black_24dp;
        } else if (position == 2) {
            return R.drawable.ic_video_library_black_24dp;
        } else if (position == 3) {
            return R.drawable.ic_user_group_black_24dp;
        } else if (position == 4) {
            return R.drawable.ic_shopping_basket_black_24dp;
        } else if (position == 5) {
            return R.drawable.ic_file_document_black_24dp;
        } else {
            return R.drawable.ic_bow_tie_black_24dp;
        }
    }
}
