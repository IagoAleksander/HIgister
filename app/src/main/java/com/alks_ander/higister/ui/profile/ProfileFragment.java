package com.alks_ander.higister.ui.profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alks_ander.higister.R;
import com.alks_ander.higister.ui.login.AuthFragment;
import com.alks_ander.higister.util.Rotate;
import com.alks_ander.higister.util.TextSizeTransition;
import com.alks_ander.higister.util.TextWatcherAdapter;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by alksander on 05/03/2018.
 */

public class ProfileFragment extends Fragment{

    @BindView(R.id.user_view_profile_layout)
    LinearLayout mViewProfileLayout;

    @BindView(R.id.user_description)
    TextView mDescriptionTextView;

    @BindView(R.id.user_age)
    TextView mAgeTextView;

    @BindView(R.id.user_interests)
    TextView mInterestsTextView;

    @BindView(R.id.user_edit_profile_layout)
    LinearLayout mEditProfileLayout;

    @BindView(R.id.text_input_user_name)
    TextInputLayout mNameTextInput;

    @BindView(R.id.text_input_user_description)
    TextInputLayout mDescriptionTextInput;

    @BindView(R.id.text_input_user_age)
    TextInputLayout mAgeTextInput;

    @BindView(R.id.text_input_user_interests)
    TextInputLayout mInterestsTextInput;

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

        ProfileActivity activity = (ProfileActivity) getActivity();
        activity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewProfileLayout.getVisibility() == View.VISIBLE) {
                    mDescriptionTextInput.getEditText().setText(mDescriptionTextView.getText());
                    mAgeTextInput.getEditText().setText(mAgeTextView.getText());
                    mInterestsTextInput.getEditText().setText(mInterestsTextView.getText());
                    mViewProfileLayout.setVisibility(View.GONE);
                    mEditProfileLayout.setVisibility(View.VISIBLE);
                    activity.fab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_save, null));
                }
                else {
                    mDescriptionTextView.setText(mDescriptionTextInput.getEditText().getText());
                    mAgeTextView.setText(mAgeTextInput.getEditText().getText());
                    mInterestsTextView.setText(mInterestsTextInput.getEditText().getText());
                    mEditProfileLayout.setVisibility(View.GONE);
                    mViewProfileLayout.setVisibility(View.VISIBLE);
                    activity.fab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_edit, null));

                }
            }
        });
    }
}