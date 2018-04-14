package com.iaz.higister.ui.profile;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.data.model.User;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.text_input_user_name)
    TextInputLayout mNameTextInput;

    @BindView(R.id.text_input_user_description)
    TextInputLayout mDescriptionTextInput;

    @BindView(R.id.text_input_user_age)
    TextInputLayout mAgeTextInput;

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

    User user;
    ProfileActivity activity;

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

        activity = (ProfileActivity) getActivity();
        activity.activityComponent().inject(this);

        mProfilePresenter.attachView(this);

        mProfilePresenter.receiveProfileInfo();
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint())
        {
            return;
        }
    }

    @Override
    public ProfileFragment getFragment() {
        return this;
    }

    @Override
    public void updateData(User user) {
        this.user = user;

        activity.updateUserInfo(user);

        activity.getSupportActionBar().setTitle(user.name);
        mDescriptionTextView.setText(user.description);
        mAgeTextView.setText(Integer.toString(user.age));

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

        if (!user.interests.isEmpty()) {
            interestsLayout1.setVisibility(View.VISIBLE);

            interests1.setVisibility(View.VISIBLE);
            interests1.setText(user.interests.get(0));

            if (user.interests.size() > 1) {
                interests2.setVisibility(View.VISIBLE);
                interests2.setText(user.interests.get(1));

                if (user.interests.size() > 2) {
                    interests3.setVisibility(View.VISIBLE);
                    interests3.setText(user.interests.get(2));

                    if (user.interests.size() > 3) {
                        interestsLayout2.setVisibility(View.VISIBLE);

                        interests4.setVisibility(View.VISIBLE);
                        interests4.setText(user.interests.get(3));

                        if (user.interests.size() > 4) {
                            interests5.setVisibility(View.VISIBLE);
                            interests5.setText(user.interests.get(4));

                            if (user.interests.size() > 5) {
                                interests6.setVisibility(View.VISIBLE);
                                interests6.setText(user.interests.get(5));

                                if (user.interests.size() > 6) {
                                    interestsLayout3.setVisibility(View.VISIBLE);

                                    interests7.setVisibility(View.VISIBLE);
                                    interests7.setText(user.interests.get(6));

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void swapBetweenDisplayAndEditProfileInfos() {
        if (mViewProfileLayout.getVisibility() == View.VISIBLE) {
            mNameTextInput.getEditText().setText(activity.getSupportActionBar().getTitle());
            mDescriptionTextInput.getEditText().setText(user.description);
            mAgeTextInput.getEditText().setText(Integer.toString(user.age));


            mCheckBoxMovies.setChecked(false);
            mCheckBoxSeries.setChecked(false);
            mCheckBoxBooks.setChecked(false);
            mCheckBoxMusic.setChecked(false);
            mCheckBoxAnimes.setChecked(false);
            mCheckBoxMangas.setChecked(false);
            mCheckBoxComics.setChecked(false);

            for (String interest : user.interests) {
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
            mViewProfileLayout.setVisibility(View.GONE);
            mEditProfileLayout.setVisibility(View.VISIBLE);
            activity.fab.setImageResource(R.drawable.ic_save);
        } else {

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

            mProfilePresenter.saveProfileInfo(
                    mNameTextInput.getEditText().getText().toString(),
                    mDescriptionTextInput.getEditText().getText().toString(),
                    Integer.parseInt(mAgeTextInput.getEditText().getText().toString()),
                    interests
            );


            activity.fab.setImageResource(R.drawable.ic_edit);

        }
    }
}