package com.iaz.higister.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.util.AppBarStateChangeListener;
import com.iaz.higister.util.SectionsPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.FAVOURITES_TAB_INDEX;
import static com.iaz.higister.util.Constants.LISTS_TAB_INDEX;
import static com.iaz.higister.util.Constants.SEARCH_TAB_INDEX;
import static com.iaz.higister.util.Constants.PROFILE_TAB_INDEX;
import static com.iaz.higister.util.Constants.FEED_TAB_INDEX;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class MainActivity extends BaseActivity implements SmartTabLayout.TabProvider {

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

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.search_layout)
    LinearLayout searchLayout;

    @BindView(R.id.close_search_layout_button)
    ImageView closeSearchLayoutButton;

    @BindView(R.id.search_text)
    EditText searchText;

//    @BindView(R.id.tabs)
//    TabLayout mTabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    FragmentManager fm;

    Uri uri;

    public ArrayList<String> favoritedListsId = new ArrayList<>();
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        fm = getSupportFragmentManager();

        mSectionsPagerAdapter = new SectionsPagerAdapter(fm, this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(mViewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View tab = viewPagerTab.getTabAt(position);
                View mark = tab.findViewById(R.id.custom_tab_notification_mark);
                mark.setVisibility(View.GONE);

                setFab(position);
            }
        });

//        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.accent));
//        mTabLayout.setSelectedTabIndicatorHeight(7);
//
//        mTabLayout.getTabAt(0).setText("Friends");
//        mTabLayout.getTabAt(1).setText("Suggested Friends");
//        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPagerTab.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                setFab(0);
                viewPagerTab.removeOnLayoutChangeListener(this);
            }
        });

        closeSearchLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLayout.setVisibility(View.GONE);
            }
        });
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
                icon.setImageResource(R.drawable.ic_person_white_24dp);
                break;
            case LISTS_TAB_INDEX:
                icon.setImageResource(R.drawable.ic_list);
                break;
            case FAVOURITES_TAB_INDEX:
                icon.setImageResource(R.drawable.ic_action_star_10);
                break;
            case FEED_TAB_INDEX:
                icon.setImageResource(R.drawable.ic_home_white_24dp);
                break;
            case SEARCH_TAB_INDEX:
                icon.setImageResource(R.drawable.ic_search_white_24dp);
                break;
            default:
                throw new IllegalStateException("Invalid position: " + position);
        }
        return tab;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_edit, menu);
//
////        MenuItem editButton = menu.findItem(R.id.action_edit);
////        editButton.setVisible(mViewPager.getCurrentItem() == 0);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case android.R.id.home:
//                finish();
////                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
//                break;
//            case R.id.action_edit:
//                finish();
//                break;
//            default:
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void updateUserInfo() {
        followersCounter.setText(Integer.toString(user.getFollowersNumber()));
        createdListsCounter.setText(Integer.toString(user.getListsCreatedNumber()));
        favoritedListsCounter.setText(Integer.toString(user.getListsFavouritedNumber()));

        if (user.getProfilePictureUri() != null)
            callGlide(Uri.parse(user.getProfilePictureUri()));

//        setFab(PROFILE_TAB_INDEX);
//        mViewPager.setCurrentItem(PROFILE_TAB_INDEX);
    }

    public void setFab (int position) {

        searchLayout.setVisibility(View.GONE);
        Fragment fragment = fm.findFragmentByTag("android:switcher:" + R.id.container + ":" +position);

        if (position == PROFILE_TAB_INDEX) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_edit);
            fab.setOnClickListener(v -> {
                if (fragment != null && fragment instanceof ProfileFragment) {
                    ((ProfileFragment) fragment).swapBetweenDisplayAndEditProfileInfos();
                }
            });
            getSupportActionBar().setTitle("My Profile");
        } else if (position == LISTS_TAB_INDEX) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_add);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                MainActivity.this.startActivity(intent);
            });
            getSupportActionBar().setTitle("My Lists");
        } else if (position == FAVOURITES_TAB_INDEX) {
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Favorited Lists");
        } else if (position == FEED_TAB_INDEX) {
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle("My Feed");
        } else if (position == SEARCH_TAB_INDEX) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_search_white_24dp);
            fab.setOnClickListener(v -> {

                if (searchLayout.getVisibility() == View.GONE)
                    searchLayout.setVisibility(View.VISIBLE);
                else {
                    if (fragment != null && fragment instanceof MyListsFragment) {
                        ((MyListsFragment) fragment).mListsPresenter.search(searchText.getText().toString());
                    }
                }
            });
            getSupportActionBar().setTitle("Search");
        }
    }

    public void callGlide(Uri uri) {
        if (uri == null) {
            profileImage.setVisibility(View.GONE);
        }

        this.uri = uri;
        profileImage.setVisibility(View.VISIBLE);
        try {
            Glide.with(this)
                    .load(uri)
                    .into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void myNewGraphReq(String friendlistId) {
//        final String graphPath = "/"+friendlistId+"/members/";
//        AccessToken token = AccessToken.getCurrentAccessToken();
//        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//                JSONObject object = graphResponse.getJSONObject();
//                try {
//                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
//                /* Do something with the user list */
//                /* ex: get first user in list, "name" */
//                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
//                    String usersName = user.getString("name");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Bundle param = new Bundle();
//        param.putString("fields", "name");
//        request.setParameters(param);
//        request.executeAsync();
//    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
