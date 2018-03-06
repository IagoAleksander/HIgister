package com.alks_ander.higister.ui.profile;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import com.alks_ander.higister.R;
import com.alks_ander.higister.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class ProfileActivity extends BaseActivity{

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }

}
