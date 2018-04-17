package com.iaz.higister.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iaz.higister.ui.main.ListsFragment;
import com.iaz.higister.ui.main.MyListsFragment;
import com.iaz.higister.ui.main.ProfileFragment;


/**
 * Created by sgobbe on 17/07/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new ProfileFragment();
            case 1:
                return new MyListsFragment();
            case 2:
                return new ListsFragment();
            case 3:
                return new ListsFragment();
            case 4:
                return new ListsFragment();
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return 5;
    }
}
