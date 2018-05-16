package com.iaz.higister.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.Profile;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.util.AppBarStateChangeListener;
import com.iaz.higister.util.DialogFactory;
import com.iaz.higister.util.SectionsPagerAdapter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.truizlop.fabreveallayout.CircularExpandingView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.ANIMES;
import static com.iaz.higister.util.Constants.BOOKS;
import static com.iaz.higister.util.Constants.COMICS;
import static com.iaz.higister.util.Constants.FAVOURITES_TAB_INDEX;
import static com.iaz.higister.util.Constants.LISTS_TAB_INDEX;
import static com.iaz.higister.util.Constants.MANGAS;
import static com.iaz.higister.util.Constants.MOVIES;
import static com.iaz.higister.util.Constants.MUSICS;
import static com.iaz.higister.util.Constants.SEARCH_TAB_INDEX;
import static com.iaz.higister.util.Constants.PROFILE_TAB_INDEX;
import static com.iaz.higister.util.Constants.FEED_TAB_INDEX;
import static com.iaz.higister.util.Constants.TV_SERIES;

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

    @BindView(R.id.search_filter_layout)
    LinearLayout searchFilterLayout;

    @BindView(R.id.filter_search_button_image)
    ImageView searchFilterButtonImage;

    @BindView(R.id.filter_search_button_text)
    TextView searchFilterButtonText;

    @BindView(R.id.toggle)
    RadioGroup toggle;

    @BindView(R.id.list_types)
    LinearLayout listTypes;

    @BindView(R.id.checkbox_movies)
    CheckBox checkboxMovies;

    @BindView(R.id.checkbox_series)
    CheckBox checkboxSeries;

    @BindView(R.id.checkbox_animes)
    CheckBox checkboxAnimes;

    @BindView(R.id.checkbox_mangas)
    CheckBox checkboxMangas;

    @BindView(R.id.checkbox_books)
    CheckBox checkboxBooks;

    @BindView(R.id.checkbox_music)
    CheckBox checkboxMusic;

    @BindView(R.id.checkbox_comics)
    CheckBox checkboxComics;

    @BindView(R.id.search_layout)
    LinearLayout searchLayout;

    @BindView(R.id.filter_search_layout_button)
    LinearLayout filterButton;

    @BindView(R.id.close_search_layout_button)
    LinearLayout closeSearchLayoutButton;

    @BindView(R.id.search_text)
    EditText searchText;

    @BindView(R.id.circular)
    CircularExpandingView circular;

    @BindView(R.id.insert_text_layout)
    CardView insertTextLayout;

//    @BindView(R.id.tabs)
//    TabLayout mTabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    FragmentManager fm;

    Uri uri;
    private Dialog mDialog;

    public ArrayList<String> favoritedListsId = new ArrayList<>();
    public User user;

    public boolean searchAlreadyClicked = false;
    UserRepository userRepository = new UserRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("user");

            if (user != null) {
                updateUserInfo();
            } else {
                recoverProfileInfo();
            }
        }
        else {
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

        fm = getSupportFragmentManager();

        mSectionsPagerAdapter = new SectionsPagerAdapter(fm, this, 0);
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
                setFilterLayout(true);
            }
        });

        setFilterLayout(false);
        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (toggle.indexOfChild(findViewById(toggle.getCheckedRadioButtonId())) == 0) {
                    listTypes.setVisibility(View.VISIBLE);
                }
                else {
                    listTypes.setVisibility(View.GONE);
                }
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
//                finish();
//                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
            case R.id.action_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                MainActivity.this.startActivity(intent);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUserInfo() {

        followersCounter.setText(Integer.toString(user.getFollowersNumber()));
        createdListsCounter.setText(Integer.toString(user.getListsCreatedNumber()));
        favoritedListsCounter.setText(Integer.toString(user.getListsFavouritedNumber()));

        if (user.getProfilePictureUri() != null)
            callGlide(Uri.parse(user.getProfilePictureUri()));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(user.getName());
        }
        logUserToCrashlitics();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userName", user.getName());
        editor.apply();
    }

    public void setFab(int position) {

        Fragment fragment = fm.findFragmentByTag("android:switcher:" + R.id.container + ":" + position);

        if (position == LISTS_TAB_INDEX) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_add);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                MainActivity.this.startActivity(intent);
                fragment.getActivity().overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            });
            closeSearch();
        } else if (position == FAVOURITES_TAB_INDEX) {
            fab.setVisibility(View.GONE);
            closeSearch();
        } else if (position == FEED_TAB_INDEX) {
            fab.setVisibility(View.GONE);
            closeSearch();
        } else if (position == SEARCH_TAB_INDEX) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_search_white_24dp);
            circular.setVisibility(View.VISIBLE);
            animate();
            fab.setOnClickListener(v -> {

                insertTextLayout.setVisibility(View.GONE);
                if (searchLayout.getVisibility() == View.GONE) {
                    circular.setVisibility(View.VISIBLE);
                    animate();
                } else {
                    if (fragment != null && fragment instanceof MyListsFragment) {
                        if (searchText.getText().toString().trim().length() > 2) {
                            if (toggle.indexOfChild(findViewById(toggle.getCheckedRadioButtonId())) == 0) {
                                ((MyListsFragment) fragment).mListsPresenter.search(searchText.getText().toString().trim(), createArrayTypes());
                            } else {
                                ((MyListsFragment) fragment).mListsPresenter.search(searchText.getText().toString().trim(), null);
                            }
                            searchAlreadyClicked = true;
                        }
                        else {
                            MaterialDialog.Builder dialog = DialogFactory.newMaterialDialog(MainActivity.this);
                            dialog.show();
                        }
                    }
                }
            });
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void animate() {
        circular.setColor(ContextCompat.getColor(MainActivity.this, R.color.primary_light));
        Animator expandAnimator = circular.expand();

        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                circular.setVisibility(View.GONE);
                circular.contract();
                searchLayout.setVisibility(View.VISIBLE);
                if (!searchAlreadyClicked)
                    insertTextLayout.setVisibility(View.VISIBLE);
            }
        });
        expandAnimator.start();
    }

    private void logUserToCrashlitics() {
        Crashlytics.setUserIdentifier(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null)
            Crashlytics.setUserEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (user != null)
            Crashlytics.setUserName(user.getName());
    }

    public void recoverProfileInfo() {
        mDialog = DialogFactory.newDialog(this, "Loading profile...");
        mDialog.show();

        userRepository.receiveProfileInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserRepository.OnUpdateProfile() {
            @Override
            public void onSuccess(User mUser) {
                user = mUser;

                DialogFactory.finalizeDialog(mDialog, true, "Profile updated with success", new DialogFactory.OnDialogButtonClicked() {
                    @Override
                    public void onClick() {
                        updateUserInfo();
                    }
                });

            }

            @Override
            public void onFailure(String exception) {
            }
        });
    }

    public void closeSearch() {
        insertTextLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.GONE);

        setFilterLayout(true);
    }

    public void setFilterLayout(boolean wasOpen) {
        if (wasOpen) {

            searchFilterLayout.setVisibility(View.GONE);
            searchFilterButtonImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_keyboard_arrow_up));
            searchFilterButtonText.setText(getResources().getString(R.string.open_filter));
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appBarLayout.setExpanded(false);
                    setFilterLayout(false);
                }
            });
        }
        else {
            searchFilterLayout.setVisibility(View.VISIBLE);
            searchFilterButtonImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_keyboard_arrow_down));
            searchFilterButtonText.setText(getResources().getString(R.string.close_filter));
            insertTextLayout.setVisibility(View.GONE);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appBarLayout.setExpanded(true);
                    setFilterLayout(true);
                }
            });
        }
    }

    public ArrayList<Integer> createArrayTypes() {

        ArrayList<Integer> typesSelected = new ArrayList<>();

        if (checkboxMovies.isChecked()) {
            typesSelected.add(MOVIES);
        }
        if (checkboxSeries.isChecked()) {
            typesSelected.add(TV_SERIES);
        }
        if (checkboxAnimes.isChecked()) {
            typesSelected.add(ANIMES);
        }
        if (checkboxMangas.isChecked()) {
            typesSelected.add(MANGAS);
        }
        if (checkboxBooks.isChecked()) {
            typesSelected.add(BOOKS);
        }
        if (checkboxMusic.isChecked()) {
            typesSelected.add(MUSICS);
        }
        if (checkboxComics.isChecked()) {
            typesSelected.add(COMICS);
        }

        return typesSelected;
    }

}
