package com.iaz.higister.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.util.CustomPhotoPickerDialog;
import com.iaz.higister.util.DialogFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by alksander on 05/03/2018.
 */

public class ProfileActivity extends BaseActivity implements ProfileMvpView {

    @Inject
    ProfilePresenter mProfilePresenter;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.profile_name)
    TextView profileName;

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

    @BindView(R.id.user_view_profile_layout)
    LinearLayout mViewProfileLayout;

    @BindView(R.id.interestsPrimary)
    LinearLayout interestsLayout1;

    @BindView(R.id.interestsSecondary)
    LinearLayout interestsLayout2;

    @BindView(R.id.interestsTerciary)
    LinearLayout interestsLayout3;

    @BindView(R.id.interest_1)
    TextView interests1;

    @BindView(R.id.interest_2)
    TextView interests2;

    @BindView(R.id.interest_3)
    TextView interests3;

    @BindView(R.id.interest_4)
    TextView interests4;

    @BindView(R.id.interest_5)
    TextView interests5;

    @BindView(R.id.interest_6)
    TextView interests6;

    @BindView(R.id.interest_7)
    TextView interests7;

    @BindView(R.id.user_description)
    TextView mDescriptionTextView;

    @BindView(R.id.user_age)
    TextView mAgeTextView;

    @BindView(R.id.user_edit_profile_layout)
    LinearLayout mEditProfileLayout;

    @BindView(R.id.button_change_picture)
    Button changePictureButton;

    @BindView(R.id.text_input_user_name)
    TextInputLayout mNameTextInput;

    @BindView(R.id.user_description_layout)
    LinearLayout userDescriptionLayout;

    @BindView(R.id.text_input_user_description)
    TextInputLayout mDescriptionTextInput;

    @BindView(R.id.user_age_layout)
    LinearLayout userAgeLayout;

    @BindView(R.id.text_input_user_age)
    TextInputLayout mAgeTextInput;

    @BindView(R.id.user_interests_layout)
    LinearLayout userInterestsLayout;

    @BindView(R.id.checkbox_movies)
    CheckBox mCheckBoxMovies;

    @BindView(R.id.checkbox_series)
    CheckBox mCheckBoxSeries;

    @BindView(R.id.checkbox_books)
    CheckBox mCheckBoxBooks;

    @BindView(R.id.checkbox_music)
    CheckBox mCheckBoxMusic;

    @BindView(R.id.checkbox_animes)
    CheckBox mCheckBoxAnimes;

    @BindView(R.id.checkbox_mangas)
    CheckBox mCheckBoxMangas;

    @BindView(R.id.checkbox_comics)
    CheckBox mCheckBoxComics;

    @BindView(R.id.user_social_layout)
    LinearLayout userSocialLayout;

    private CustomPhotoPickerDialog photoDialog;

    UserRepository userRepository = new UserRepository();
    User user;
    Uri uri;
    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        activityComponent().inject(this);

        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mProfilePresenter.attachView(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("user");

            if (user != null) {
                updateData(user);
            } else {
                recoverProfileInfo();
            }
        }
    }

    @Override
    public ProfileActivity getActivity() {
        return this;
    }

    @Override
    public void updateData(User user) {
        this.user = user;
        updateProfileInfo();

        if (user.getBio() == null || user.getBio().isEmpty())
            userDescriptionLayout.setVisibility(View.GONE);
        else {
            mDescriptionTextView.setText(user.getBio());
            userDescriptionLayout.setVisibility(View.VISIBLE);
        }

        if (user.getAge() == 0 || user.getBio().isEmpty())
            userAgeLayout.setVisibility(View.GONE);
        else {
            mAgeTextView.setText(String.format("%d", user.getAge()));
            userAgeLayout.setVisibility(View.VISIBLE);
        }

//        if (user.socialMedia == null || user.socialMedia.isEmpty())
        userSocialLayout.setVisibility(View.GONE);
//        else
//            mSocialTextView.setText(user.socialMedia);


        mEditProfileLayout.setVisibility(View.GONE);
        mViewProfileLayout.setVisibility(View.VISIBLE);

        interestsLayout1.setVisibility(View.GONE);
        interestsLayout2.setVisibility(View.GONE);
        interestsLayout3.setVisibility(View.GONE);
        interests1.setVisibility(View.GONE);
        interests2.setVisibility(View.GONE);
        interests3.setVisibility(View.GONE);
        interests4.setVisibility(View.GONE);
        interests5.setVisibility(View.GONE);
        interests6.setVisibility(View.GONE);
        interests7.setVisibility(View.GONE);

        if (user.getInterests() != null && !user.getInterests().isEmpty()) {
            userInterestsLayout.setVisibility(View.VISIBLE);
            interestsLayout1.setVisibility(View.VISIBLE);

            interests1.setVisibility(View.VISIBLE);
            interests1.setText(user.getInterests().get(0));

            if (user.getInterests().size() > 1) {
                interests2.setVisibility(View.VISIBLE);
                interests2.setText(user.getInterests().get(1));

                if (user.getInterests().size() > 2) {
                    interests3.setVisibility(View.VISIBLE);
                    interests3.setText(user.getInterests().get(2));

                    if (user.getInterests().size() > 3) {
                        interestsLayout2.setVisibility(View.VISIBLE);

                        interests4.setVisibility(View.VISIBLE);
                        interests4.setText(user.getInterests().get(3));

                        if (user.getInterests().size() > 4) {
                            interests5.setVisibility(View.VISIBLE);
                            interests5.setText(user.getInterests().get(4));

                            if (user.getInterests().size() > 5) {
                                interests6.setVisibility(View.VISIBLE);
                                interests6.setText(user.getInterests().get(5));

                                if (user.getInterests().size() > 6) {
                                    interestsLayout3.setVisibility(View.VISIBLE);

                                    interests7.setVisibility(View.VISIBLE);
                                    interests7.setText(user.getInterests().get(6));

                                }
                            }
                        }
                    }
                }
            }
        } else {
            userInterestsLayout.setVisibility(View.GONE);
        }
        invalidateOptionsMenu();
    }

    public void updateProfileInfo() {
        followersCounter.setText(Integer.toString(user.getFollowersNumber()));
        createdListsCounter.setText(Integer.toString(user.getListsCreatedNumber()));
        favoritedListsCounter.setText(Integer.toString(user.getListsFavouritedNumber()));

        if (user.getProfilePictureUri() != null)
            callGlide(Uri.parse(user.getProfilePictureUri()));

        if (user.getName() != null) {
            profileName.setText(user.getName());
            profileName.setVisibility(View.VISIBLE);
        } else
            profileName.setVisibility(View.GONE);
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
                        updateProfileInfo();
                    }
                });

            }

            @Override
            public void onFailure(String exception) {
            }
        });
    }

    public void swapBetweenDisplayAndEditProfileInfos() {
        if (mViewProfileLayout.getVisibility() == View.VISIBLE) {

            userRepository.removeListener();

            changePictureButton.setOnClickListener(v -> {
                photoDialog = new CustomPhotoPickerDialog(ProfileActivity.this, new CustomPhotoPickerDialog
                        .OnOptionPhotoSelected() {
                    @Override
                    public void onGallery() {
                        mProfilePresenter.openDialogWindow();
                        photoDialog.dismiss();
                    }

                    @Override
                    public void onCamera() {
                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            photoDialog.dismiss();
                            ActivityCompat.requestPermissions(ProfileActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                        } else {
                            mProfilePresenter.getPhoto();
                            photoDialog.dismiss();
                        }
                    }
                });
                photoDialog.show();


            });


            mNameTextInput.getEditText().setText(profileName.getText());

            if (user != null) {
                mDescriptionTextInput.getEditText().setText(user.getBio());
                mAgeTextInput.getEditText().setText(Integer.toString(user.getAge()));

                mCheckBoxMovies.setChecked(false);
                mCheckBoxSeries.setChecked(false);
                mCheckBoxBooks.setChecked(false);
                mCheckBoxMusic.setChecked(false);
                mCheckBoxAnimes.setChecked(false);
                mCheckBoxMangas.setChecked(false);
                mCheckBoxComics.setChecked(false);

                for (String interest : user.getInterests()) {
                    switch (interest) {
                        case "Movies":
                            mCheckBoxMovies.setChecked(true);
                            break;
                        case "TV Series":
                            mCheckBoxSeries.setChecked(true);
                            break;
                        case "Books":
                            mCheckBoxBooks.setChecked(true);
                            break;
                        case "Music":
                            mCheckBoxMusic.setChecked(true);
                            break;
                        case "Animes":
                            mCheckBoxAnimes.setChecked(true);
                            break;
                        case "Mangas":
                            mCheckBoxMangas.setChecked(true);
                            break;
                        case "Comics":
                            mCheckBoxComics.setChecked(true);
                            break;
                    }
                }
            }
//
            mViewProfileLayout.setVisibility(View.GONE);
            mEditProfileLayout.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mProfilePresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProfilePresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
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
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        if (user != null && user.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            if (mEditProfileLayout.getVisibility() == View.VISIBLE) {
                menu.findItem(R.id.action_edit).setVisible(false);
                menu.findItem(R.id.action_save).setVisible(true);
            } else if (mViewProfileLayout.getVisibility() == View.VISIBLE) {
                menu.findItem(R.id.action_edit).setVisible(true);
                menu.findItem(R.id.action_save).setVisible(false);
            }
        }
        else {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (user != null)
                    if (user.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        getApplicationContext().startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                    }

                finish();
                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
            case R.id.action_save:
                ArrayList<String> interests = new ArrayList<>();
                if (mCheckBoxMovies.isChecked())
                    interests.add("Movies");
                if (mCheckBoxSeries.isChecked())
                    interests.add("TV Series");
                if (mCheckBoxBooks.isChecked())
                    interests.add("Books");
                if (mCheckBoxMusic.isChecked())
                    interests.add("Music");
                if (mCheckBoxAnimes.isChecked())
                    interests.add("Animes");
                if (mCheckBoxMangas.isChecked())
                    interests.add("Mangas");
                if (mCheckBoxComics.isChecked())
                    interests.add("Comics");

                if (uri != null && !uri.toString().contains("http")) {
                    userRepository.saveProfileImageOnStorage(uri.toString(), new UserRepository.OnImageUpload() {
                        @Override
                        public void onSuccess(Uri uri) {

                            if (user == null)
                                user = new User();

                            user.setName(mNameTextInput.getEditText().getText().toString());
                            user.setBio(mDescriptionTextInput.getEditText().getText().toString());
                            user.setAge(Integer.parseInt(mAgeTextInput.getEditText().getText().toString()));
                            user.setInterests(interests);
                            user.setProfilePictureUri(uri.toString());

                            userRepository.saveProfileInfo(ProfileActivity.this, user, new UserRepository.OnUpdateProfile() {
                                @Override
                                public void onSuccess(User user) {
                                    updateData(user);
                                }

                                @Override
                                public void onFailure(String exception) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(String exception) {
                        }
                    });

                } else {

                    if (user == null)
                        user = new User();

                    user.setName(mNameTextInput.getEditText().getText().toString());
                    user.setBio(mDescriptionTextInput.getEditText().getText().toString());

                    try {
                        user.setAge(Integer.parseInt(mAgeTextInput.getEditText().getText().toString()));
                    } catch (NumberFormatException e) {
                        user.setAge(0);
                    }

                    user.setInterests(interests);

                    if (uri != null)
                        user.setProfilePictureUri(uri.toString());

                    userRepository.saveProfileInfo(ProfileActivity.this, user, new UserRepository.OnUpdateProfile() {
                        @Override
                        public void onSuccess(User user) {
                            updateData(user);
                        }

                        @Override
                        public void onFailure(String exception) {

                        }
                    });
                }

                break;
            case R.id.action_edit:
                swapBetweenDisplayAndEditProfileInfos();

                break;

            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (user != null)
            if (user.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                getApplicationContext().startActivity(intent);
                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
            }

        finish();
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }
}