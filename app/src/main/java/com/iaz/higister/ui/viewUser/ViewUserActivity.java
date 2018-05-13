package com.iaz.higister.ui.viewUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.iaz.higister.ui.main.MainActivity;
import com.iaz.higister.util.AppBarStateChangeListener;
import com.iaz.higister.util.SectionsPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.truizlop.fabreveallayout.CircularExpandingView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.*;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class ViewUserActivity extends BaseActivity implements SmartTabLayout.TabProvider {

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

    @BindView(R.id.container)
    ViewPager mViewPager;

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
        setContentView(R.layout.activity_view_user);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("user");
            favoritedListsId = getIntent().getExtras().getStringArrayList("myFavoritedListsId");
        }

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(fm, this, 1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(mViewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View tab = viewPagerTab.getTabAt(position);
            }
        });
    }


    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View tab = inflater.inflate(R.layout.custom_tab, container, false);
        TextView text = (TextView) tab.findViewById(R.id.custom_tab_header_text);

        switch (position) {
            case USER_LISTS_TAB_INDEX:
                text.setText("Created Lists");
                break;
            case USER_FAVOURITES_TAB_INDEX:
                text.setText("Favorited Lists");
                break;
            default:
                throw new IllegalStateException("Invalid position: " + position);
        }
        return tab;
    }

    public void updateUserInfo() {
        followersCounter.setText(Integer.toString(user.getFollowersNumber()));
        createdListsCounter.setText(Integer.toString(user.getListsCreatedNumber()));
        favoritedListsCounter.setText(Integer.toString(user.getListsFavouritedNumber()));

        if (user.getProfilePictureUri() != null)
            callGlide(Uri.parse(user.getProfilePictureUri()));

        getSupportActionBar().setTitle(user.getName());
//        setFab(PROFILE_TAB_INDEX);
//        mViewPager.setCurrentItem(PROFILE_TAB_INDEX);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        getApplicationContext().startActivity(intent);
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }

}
