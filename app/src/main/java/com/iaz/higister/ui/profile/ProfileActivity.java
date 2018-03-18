package com.iaz.higister.ui.profile;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.base.MvpView;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.login.AuthActivity;
import com.iaz.higister.ui.login.AuthPresenter;
import com.iaz.higister.util.AppBarStateChangeListener;
import com.iaz.higister.util.SectionsPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.FAVOURITES_TAB_INDEX;
import static com.iaz.higister.util.Constants.LISTS_TAB_INDEX;
import static com.iaz.higister.util.Constants.PEOPLE_TAB_INDEX;
import static com.iaz.higister.util.Constants.PROFILE_TAB_INDEX;
import static com.iaz.higister.util.Constants.SEARCH_TAB_INDEX;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class ProfileActivity extends BaseActivity implements SmartTabLayout.TabProvider {

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.info_layout)
    LinearLayout mUserInfoLayout;

    @BindView(R.id.user_followers_number)
    TextView followersCounter;

    @BindView(R.id.user_created_lists_number)
    TextView createdListsCounter;

    @BindView(R.id.user_favorited_lists_number)
    TextView favoritedListsCounter;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.container)
    ViewPager mViewPager;

    User user;

//    @BindView(R.id.tabs)
//    TabLayout mTabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int i) {

                float fraction = (i / 400.0f) / 2;
                mUserInfoLayout.setAlpha(0.5f + fraction);

                if (state == State.COLLAPSED) {
                    mUserInfoLayout.setVisibility(View.GONE);
                } else {
                    mUserInfoLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(mViewPager);


        fab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_edit, null));
        viewPagerTab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View tab = viewPagerTab.getTabAt(position);
                View mark = tab.findViewById(R.id.custom_tab_notification_mark);
                mark.setVisibility(View.GONE);

                if (position == PROFILE_TAB_INDEX) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_edit, null));
                } else if (position == LISTS_TAB_INDEX) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_add, null));
                } else if (position == FAVOURITES_TAB_INDEX) {
                    fab.setVisibility(View.GONE);
                } else if (position == SEARCH_TAB_INDEX) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_search_white_24dp, null));
                } else if (position == PEOPLE_TAB_INDEX) {
                    fab.setVisibility(View.GONE);
                }
            }
        });

//        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.accent));
//        mTabLayout.setSelectedTabIndicatorHeight(7);
//
//        mTabLayout.getTabAt(0).setText("Friends");
//        mTabLayout.getTabAt(1).setText("Suggested Friends");
//        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        Resources res = container.getContext().getResources();
        View tab = inflater.inflate(R.layout.custom_tab_icon_and_notification_mark, container, false);
        View mark = tab.findViewById(R.id.custom_tab_notification_mark);
        mark.setVisibility(View.GONE);
        ImageView icon = (ImageView) tab.findViewById(R.id.custom_tab_icon);
        switch (position) {
            case PROFILE_TAB_INDEX:
                icon.setImageDrawable(res.getDrawable(R.drawable.ic_home_white_24dp));
                break;
            case LISTS_TAB_INDEX:
                icon.setImageDrawable(res.getDrawable(R.drawable.ic_list));
                break;
            case FAVOURITES_TAB_INDEX:
                icon.setImageDrawable(res.getDrawable(R.drawable.ic_action_star_10));
                break;
            case SEARCH_TAB_INDEX:
                icon.setImageDrawable(res.getDrawable(R.drawable.ic_search_white_24dp));
                break;
            case PEOPLE_TAB_INDEX:
                icon.setImageDrawable(res.getDrawable(R.drawable.ic_person_white_24dp));
                break;
            default:
                throw new IllegalStateException("Invalid position: " + position);
        }
        return tab;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

//        MenuItem editButton = menu.findItem(R.id.action_edit);
//        editButton.setVisible(mViewPager.getCurrentItem() == 0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
//                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
            case R.id.action_edit:
                finish();
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUserInfo (User user) {
        followersCounter.setText(Integer.toString(user.followersNumber));
        createdListsCounter.setText(Integer.toString(user.listsCreatedNumber));
        favoritedListsCounter.setText(Integer.toString(user.listsFavouritedNumber));
    }
}
