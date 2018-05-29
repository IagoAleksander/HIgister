package com.iaz.HIgister.ui.intro;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.main.ProfileActivity;
import com.iaz.HIgister.util.DialogFactory;
import com.iaz.Higister.R;

import java.util.ArrayList;

public class IntroActivity extends AppIntro {

    IntroPresenter mIntroPresenter = new IntroPresenter();
    public FragmentManager fm;
    User user;
    public static final int CONTENT_VIEW_ID = 10101010;
    IntroPicture fragmentPicture;
    IntroInfo fragmentInfo;
    IntroInterests fragmentInterests;
    IntroProfile fragmentProfile;

    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntroPresenter.setActivity(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("user");
        }

        if (user == null) {
            user = new User();
        }
        // Note here that we DO NOT use setContentView();


        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new IntroPicture());
        askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        addSlide(new IntroInfo());
        addSlide(new IntroInterests());
        addSlide(new IntroProfile());
//        addSlide(SampleSlide.newInstance(R.layout.your_slide_here));
//        addSlide(SampleSlide.newInstance(R.layout.your_slide_here));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance("test", "testDescription", R.drawable.large_movie_poster, ContextCompat.getColor(IntroActivity.this, R.color.accent)));
//        addSlide(AppIntroFragment.newInstance("test", "testDescription", R.drawable.large_movie_poster, ContextCompat.getColor(IntroActivity.this, R.color.accent)));
//        addSlide(AppIntroFragment.newInstance("test", "testDescription", R.drawable.large_movie_poster, ContextCompat.getColor(IntroActivity.this, R.color.accent)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(ContextCompat.getColor(IntroActivity.this, R.color.primary_light));
        setSeparatorColor(ContextCompat.getColor(IntroActivity.this, R.color.white));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        setSkipText("Finish");
        setDoneText("Create list");

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("showTutorial", false);
        editor.apply();

        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        IntroActivity.this.startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("showTutorial", false);
        editor.apply();

        // Do something when users tap on Done button.
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.putExtra("user", user);
        IntroActivity.this.startActivity(intent);

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        if (oldFragment instanceof IntroPicture && newFragment instanceof IntroInfo) {
            user.setProfilePictureUri(fragmentPicture.uri);
        } else if (oldFragment instanceof IntroInfo && newFragment instanceof IntroInterests) {
            user.setName(fragmentInfo.mNameTextInput.getEditText().getText().toString());
            user.setBio(fragmentInfo.mDescriptionTextInput.getEditText().getText().toString());

            try {
                user.setAge(Integer.parseInt(fragmentInfo.mAgeTextInput.getEditText().getText().toString()));
            } catch (NumberFormatException e) {
                user.setAge(0);
            }
        } else if (oldFragment instanceof IntroInterests && newFragment instanceof IntroProfile) {
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


                            DialogFactory.finalizeDialogOnClick(mDialog, true, "Profile information saved successfully", () -> {
//                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//                            intent.putExtra("user", userWithId);
//                            IntroActivity.this.startActivity(intent);
                                user = userWithId;
                                showSkipButton(true);
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


                    DialogFactory.finalizeDialogOnClick(mDialog, true, "Profile information saved successfully", () -> {
//                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//                            intent.putExtra("user", userWithId);
//                            IntroActivity.this.startActivity(intent);
                        user = userWithId;
                        showSkipButton(true);
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
    public void onBackPressed() {}
}