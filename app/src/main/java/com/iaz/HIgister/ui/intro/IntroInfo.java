package com.iaz.HIgister.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 28/05/2018.
 */

public class IntroInfo extends Fragment {

    @BindView(R.id.text_input_user_name)
    TextInputLayout mNameTextInput;

    @BindView(R.id.text_input_user_description)
    TextInputLayout mDescriptionTextInput;

    @BindView(R.id.text_input_user_age)
    TextInputLayout mAgeTextInput;

    IntroActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (IntroActivity) getActivity();
//        activity.fragmentInfo = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (activity.user.getName() != null && !activity.user.getName().isEmpty())
            mNameTextInput.getEditText().setText(activity.user.getName());

        if (activity.user.getBio() != null && !activity.user.getBio().isEmpty())
            mDescriptionTextInput.getEditText().setText(activity.user.getBio());

        if (activity.user.getAge() > 0)
            mAgeTextInput.getEditText().setText(Integer.toString(activity.user.getAge()));

        mNameTextInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    mNameTextInput.setError("This field is required");
                }
                else {
                    mNameTextInput.setError(null);
                }
            }
        });
    }
}
