package com.alks_ander.higister.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alks_ander.higister.ui.login.LogInFragment;
import com.alks_ander.higister.ui.login.SignUpFragment;
import com.alks_ander.higister.ui.profile.ProfileFragment;


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
                return new ProfileFragment();
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return 2;
    }
}
