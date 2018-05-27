package com.iaz.HIgister.ui.viewUser;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.ui.base.BaseActivity;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.main.ProfileActivity;
import com.iaz.HIgister.util.AppBarStateChangeListener;
import com.iaz.HIgister.util.DialogFactory;
import com.iaz.HIgister.util.SectionsPagerAdapter;
import com.iaz.Higister.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.HIgister.util.Constants.USER_FAVOURITES_TAB_INDEX;
import static com.iaz.HIgister.util.Constants.USER_LISTS_TAB_INDEX;

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
    private Dialog mDialog;
    UserRepository userRepository = new UserRepository();

    Uri uri;

    //    public ArrayList<String> favoritedListsId = new ArrayList<>();
    public User user;
    String userId;

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
            userId = getIntent().getExtras().getString("userId");
//            favoritedListsId = getIntent().getExtras().getStringArrayList("myFavoritedListsId");
        }

        mDialog = DialogFactory.newDialog(this, "Loading profile...");
        mDialog.show();

        if (user != null) {
            initFrags();
        } else if (userId != null && !userId.isEmpty()) {
            recoverProfileInfo();
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
    }


    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View tab = inflater.inflate(R.layout.custom_tab, container, false);
        TextView text = tab.findViewById(R.id.custom_tab_header_text);

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

        if (user.getLikesReceived() > 0)
            followersCounter.setText(Integer.toString(user.getLikesReceived()));

        createdListsCounter.setText(Integer.toString(user.getListsCreatedNumber()));
        favoritedListsCounter.setText(Integer.toString(user.getListsFavouritedNumber()));

        if (user.getProfilePictureUri() != null)
            callGlide(Uri.parse(user.getProfilePictureUri()));

        getSupportActionBar().setTitle(user.getName());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
//
        MenuItem editButtonLayout = menu.findItem(R.id.action_profile);
        editButtonLayout.setVisible(true);

        return true;
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
            case R.id.action_profile:
                Intent intent2 = new Intent(ViewUserActivity.this, ProfileActivity.class);
                intent2.putExtra("user", user);
                ViewUserActivity.this.startActivity(intent2);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        getApplicationContext().startActivity(intent);
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }

    public void recoverProfileInfo() {

        userRepository.receiveProfileInfo(userId, new UserRepository.OnUpdateProfile() {
            @Override
            public void onSuccess(User mUser) {
                user = mUser;
                updateUserInfo();
                initFrags();
            }

            @Override
            public void onFailure(String exception) {
            }
        });
    }

    public void initFrags() {

        fm = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fm, ViewUserActivity.this, 1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(ViewUserActivity.this);
        viewPagerTab.setViewPager(mViewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View tab = viewPagerTab.getTabAt(position);
            }
        });

        DialogFactory.finalizeDialog(mDialog, true, "Profile updated with success", () -> {
        });

    }

}
