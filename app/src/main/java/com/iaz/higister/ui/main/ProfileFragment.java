package com.iaz.higister.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by alksander on 05/03/2018.
 */

public class ProfileFragment extends Fragment implements ProfileMvpView {

    @Inject
    ProfilePresenter mProfilePresenter;

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

    MainActivity activity;

    private CustomPhotoPickerDialog photoDialog;

    UserRepository userRepository = new UserRepository();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.profile_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        activity = (MainActivity) getActivity();
        activity.activityComponent().inject(this);

        mProfilePresenter.attachView(this);

        userRepository.receiveProfileInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserRepository.OnUpdateProfile() {
            @Override
            public void onSuccess(User user) {
                updateData(user);
                userRepository.addListener(activity, new UserRepository.OnUpdateProfile() {
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
    }

    @Override
    public ProfileFragment getFragment() {
        return this;
    }

    @Override
    public void updateData(User user) {
        activity.user = user;
        activity.updateUserInfo();

        if (user.getDescription() == null || user.getDescription().isEmpty())
            userDescriptionLayout.setVisibility(View.GONE);
        else {
            mDescriptionTextView.setText(user.getDescription());
            userDescriptionLayout.setVisibility(View.VISIBLE);
        }

        if (user.getAge() == 0 || user.getDescription().isEmpty())
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
    }

    public void swapBetweenDisplayAndEditProfileInfos() {
        if (mViewProfileLayout.getVisibility() == View.VISIBLE) {

            userRepository.removeListener();

            changePictureButton.setOnClickListener(v -> {
                photoDialog = new CustomPhotoPickerDialog(activity, new CustomPhotoPickerDialog
                        .OnOptionPhotoSelected() {
                    @Override
                    public void onGallery() {
                        mProfilePresenter.openDialogWindow();
                        photoDialog.dismiss();
                    }

                    @Override
                    public void onCamera() {
                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            photoDialog.dismiss();
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                        } else {
                            mProfilePresenter.getPhoto();
                            photoDialog.dismiss();
                        }
                    }
                });
                photoDialog.show();


            });


            mNameTextInput.getEditText().setText(activity.getSupportActionBar().getTitle());

            if (activity.user != null) {
                mDescriptionTextInput.getEditText().setText(activity.user.getDescription());
                mAgeTextInput.getEditText().setText(Integer.toString(activity.user.getAge()));


                mCheckBoxMovies.setChecked(false);
                mCheckBoxSeries.setChecked(false);
                mCheckBoxBooks.setChecked(false);
                mCheckBoxMusic.setChecked(false);
                mCheckBoxAnimes.setChecked(false);
                mCheckBoxMangas.setChecked(false);
                mCheckBoxComics.setChecked(false);

                for (String interest : activity.user.getInterests()) {
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

//        }
//        else {
//
            mViewProfileLayout.setVisibility(View.GONE);
            mEditProfileLayout.setVisibility(View.VISIBLE);
            activity.fab.setImageResource(R.drawable.ic_save);
            activity.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                    if (activity.uri != null && !activity.uri.toString().contains("http")) {
                        userRepository.saveProfileImageOnStorage(activity.uri.toString(), new UserRepository.OnImageUpload() {
                            @Override
                            public void onSuccess(Uri uri) {

                                activity.user = new User();
                                activity.user.setName(mNameTextInput.getEditText().getText().toString());
                                activity.user.setDescription(mDescriptionTextInput.getEditText().getText().toString());
                                activity.user.setAge(Integer.parseInt(mAgeTextInput.getEditText().getText().toString()));
                                activity.user.setInterests(interests);
                                activity.user.setProfilePictureUri(uri.toString());

                                userRepository.saveProfileInfo(activity, activity.user, new UserRepository.OnUpdateProfile() {
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

                        activity.user = new User();
                        activity.user.setName(mNameTextInput.getEditText().getText().toString());
                        activity.user.setDescription(mDescriptionTextInput.getEditText().getText().toString());
                        activity.user.setAge(Integer.parseInt(mAgeTextInput.getEditText().getText().toString()));
                        activity.user.setInterests(interests);

                        if (activity.uri != null)
                            activity.user.setProfilePictureUri(activity.uri.toString());

                        userRepository.saveProfileInfo(activity, activity.user, new UserRepository.OnUpdateProfile() {
                            @Override
                            public void onSuccess(User user) {
                                updateData(user);
                            }

                            @Override
                            public void onFailure(String exception) {

                            }
                        });
                    }

                    activity.fab.setImageResource(R.drawable.ic_edit);
                    activity.fab.setOnClickListener(view ->

                    {
                        swapBetweenDisplayAndEditProfileInfos();
                    });
                }
            });
        }
//            mProfilePresenter.saveProfileInfo(
//                    mNameTextInput.getEditText().getText().toString(),
//                    mDescriptionTextInput.getEditText().getText().toString(),
//                    Integer.parseInt(mAgeTextInput.getEditText().getText().toString()),
//                    interests
//            );


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
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void callGlide(Uri uri) {
        activity.callGlide(uri);
    }
}