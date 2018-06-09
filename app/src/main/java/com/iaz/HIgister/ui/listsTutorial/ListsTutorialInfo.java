package com.iaz.HIgister.ui.listsTutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 28/05/2018.
 */

public class ListsTutorialInfo extends Fragment {

    @BindView(R.id.text_input_list_description)
    TextInputLayout mDescriptionTextInput;
    @BindView(R.id.checkbox_list_isVisible)
    CheckBox isListVisible;
    @BindView(R.id.checkbox_list_commentsEnabled)
    CheckBox areCommentsEnabled;

    ListsTutorialActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ListsTutorialActivity) getActivity();
//        activity.fragmentInfo = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lists_tutorial_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
