package com.iaz.HIgister.ui.intro;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.ui.base.BaseActivity;
import com.iaz.HIgister.ui.listsTutorial.ListsTutorialActivity;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.util.CustomViewPager;
import com.iaz.HIgister.util.DialogFactory;
import com.iaz.Higister.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by alksander on 03/06/2018.
 */

public class IntroActivity extends BaseActivity {

    IntroPresenter mIntroPresenter = new IntroPresenter();
    User user;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.previous_button)
    TextView previousButton;
    @BindView(R.id.next_button)
    TextView nextButton;

    FragmentPagerAdapter adapterViewPager;

    public IntroPicture fragmentPicture;
    public IntroInfo fragmentInfo;
    public IntroInterests fragmentInterests;
    public IntroProfile fragmentProfile;

    int position = 0;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activityComponent().inject(this);
//        mViewItemPresenter.setActivity(this);

        mIntroPresenter.setActivity(this);

        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("user");
        }

        if (user == null) {
            user = new User();
        }

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapterViewPager);
        circleIndicator.setViewPager(viewPager);

        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(4);

        setSupportActionBar(mToolbar);

        nextButton.setOnClickListener(v -> setNext());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int positionT) {


                if (positionT == 0) {
                    previousButton.setVisibility(View.GONE);
                } else if (positionT == 4) {

                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);

                    nextButton.setText("Create list");
                    nextButton.setOnClickListener(v -> {

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("showIntro", false);
                        editor.apply();

                        Intent intent = new Intent(IntroActivity.this, ListsTutorialActivity.class);
                        startActivity(intent);
                    });
                    previousButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    previousButton.setText("Finish");
                    previousButton.setOnClickListener(v -> {

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("showIntro", false);
                        editor.apply();

                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    });
                } else {

                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);

                    previousButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left, 0, 0, 0);
                    previousButton.setText("Previous");
                    previousButton.setOnClickListener(v -> {

                        position--;
                        viewPager.setCurrentItem(position);
                    });

                    nextButton.setText("Next");
                    nextButton.setOnClickListener(v -> {

                        setNext();
                    });
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        IntroActivity activity;

        public MyPagerAdapter(FragmentManager fragmentManager, IntroActivity activity) {
            super(fragmentManager);
            this.activity = activity;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return 5;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new IntroWelcome();
                case 1:
                    if (activity.fragmentPicture == null)
                        activity.fragmentPicture = new IntroPicture();
                    return activity.fragmentPicture;
                case 2:
                    if (activity.fragmentInfo == null)
                        activity.fragmentInfo = new IntroInfo();
                    return activity.fragmentInfo;
                case 3:
                    if (activity.fragmentInterests == null)
                        activity.fragmentInterests = new IntroInterests();
                    return activity.fragmentInterests;
                case 4:
                    if (activity.fragmentProfile == null)
                        activity.fragmentProfile = new IntroProfile();
                    return activity.fragmentProfile;
                default:
                    if (activity.fragmentProfile == null)
                        activity.fragmentProfile = new IntroProfile();
                    return activity.fragmentProfile;
            }
        }

    }

    public void setNext() {
        if (viewPager.getCurrentItem() == 0) {
            position++;
            viewPager.setCurrentItem(position);
        }
        else if (viewPager.getCurrentItem() == 1) {
            user.setProfilePictureUri(fragmentPicture.uri);
            position++;
            viewPager.setCurrentItem(position);
        } else if (viewPager.getCurrentItem() == 2) {

            if (checkInfo()) {
                user.setName(fragmentInfo.mNameTextInput.getEditText().getText().toString());
                user.setBio(fragmentInfo.mDescriptionTextInput.getEditText().getText().toString());

                try {
                    user.setAge(Integer.parseInt(fragmentInfo.mAgeTextInput.getEditText().getText().toString()));
                } catch (NumberFormatException e) {
                    user.setAge(0);
                }

                position++;
                viewPager.setCurrentItem(position);
            }
        } else if (viewPager.getCurrentItem() == 3) {
            ArrayList<String> interests = new ArrayList<>();
            if (fragmentInterests.mCheckBoxMovies.isChecked())
                interests.add("Movies");
            if (fragmentInterests.mCheckBoxSeries.isChecked())
                interests.add("TV Series");
            if (fragmentInterests.mCheckBoxBooks.isChecked())
                interests.add("Books");
            if (fragmentInterests.mCheckBoxMusic.isChecked())
                interests.add("Music");
            if (fragmentInterests.mCheckBoxAnimes.isChecked())
                interests.add("Animes");
            if (fragmentInterests.mCheckBoxMangas.isChecked())
                interests.add("Mangas");
            if (fragmentInterests.mCheckBoxComics.isChecked())
                interests.add("Comics");

            user.setInterests(interests);
            saveProfile();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mIntroPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mIntroPresenter.activityResult(requestCode, resultCode, data);
    }

    public void showSnackBar(String msg) {
        Toast.makeText(IntroActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void saveProfile() {

        mDialog = DialogFactory.newDialog(IntroActivity.this, "Saving user profile information...");
        mDialog.show();

        UserRepository userRepository = new UserRepository();

        if (user.getProfilePictureUri() != null && !user.getProfilePictureUri().contains("http")) {
            userRepository.saveProfileImageOnStorage(user.getProfilePictureUri(), new UserRepository.OnImageUpload() {
                @Override
                public void onSuccess(Uri uri) {

                    user.setProfilePictureUri(uri.toString());

                    userRepository.saveProfileInfo(IntroActivity.this, user, new UserRepository.OnUpdateProfile() {
                        @Override
                        public void onSuccess(User userWithId) {


                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userName", user.getName());
                            editor.apply();

                            DialogFactory.finalizeDialogOnClick(mDialog, true, "Profile information saved successfully", () -> {
                                user = userWithId;
                                position++;
                                viewPager.setCurrentItem(position);
                                viewPager.setPagingEnabled(false);
                            });

                        }

                        @Override
                        public void onFailure(String exception) {
                            DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on saving user profile information", () -> {
                            });
                        }
                    });
                }

                @Override
                public void onFailure(String exception) {
                    DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on saving user profile information", () -> {
                    });
                }
            });
        } else {
            userRepository.saveProfileInfo(IntroActivity.this, user, new UserRepository.OnUpdateProfile() {
                @Override
                public void onSuccess(User userWithId) {

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userName", user.getName());
                    editor.apply();

                    DialogFactory.finalizeDialogOnClick(mDialog, true, "Profile information saved successfully", () -> {
                        user = userWithId;
                        position++;
                        viewPager.setCurrentItem(position);
                    });

                }

                @Override
                public void onFailure(String exception) {
                    DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on saving user profile information", () -> {
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (position > 0 && position < 3) {
            position--;
            viewPager.setCurrentItem(position);
        }
    }

    public boolean checkInfo() {

        if (fragmentInfo.mNameTextInput.getEditText().getText().toString().trim().isEmpty()) {
            fragmentInfo.mNameTextInput.setError("This field is required");
            fragmentInfo.mNameTextInput.requestFocus();
            return false;
        }
        else
            fragmentInfo.mNameTextInput.setError(null);

        return true;
    }

}
