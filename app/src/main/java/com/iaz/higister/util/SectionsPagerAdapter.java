package com.iaz.higister.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iaz.higister.ui.main.MyListsFragment;
import com.iaz.higister.ui.viewUser.UserListsFragment;

import static com.iaz.higister.util.Constants.*;


public class SectionsPagerAdapter extends FragmentPagerAdapter {


    Context mContext;
    int type;

    public SectionsPagerAdapter(FragmentManager fm, Context mContext, int mType) {
        super(fm);
        this.mContext = mContext;
        this.type = mType;

    }

    @Override
    public Fragment getItem(int position) {

        if (type == MY_LISTS) {
            switch (position) {
                case FEED_TAB_INDEX:
                    new MyListsFragment();
                    return MyListsFragment.newInstance("feed");
                case LISTS_TAB_INDEX:
                    new MyListsFragment();
                    return MyListsFragment.newInstance("created");
                case FAVOURITES_TAB_INDEX:
                    new MyListsFragment();
                    return MyListsFragment.newInstance("favorited");
                case SEARCH_TAB_INDEX:
                    new MyListsFragment();
                    return MyListsFragment.newInstance("search");
//                case PROFILE_TAB_INDEX:
//                    return new ProfileActivity();
                default:
                    return null;
            }
        }
        else {
            switch (position) {
                case USER_LISTS_TAB_INDEX:
                    new UserListsFragment();
                    return UserListsFragment.newInstance("created");
                case USER_FAVOURITES_TAB_INDEX:
                    new UserListsFragment();
                    return UserListsFragment.newInstance("favorited");
                default:
                    return null;
            }
        }
    }



    @Override
    public int getCount() {

        if (type == MY_LISTS) {
            return 4;
        }
        else {
            return 2;
        }
    }
}
