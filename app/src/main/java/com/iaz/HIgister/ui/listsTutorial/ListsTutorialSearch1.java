package com.iaz.HIgister.ui.listsTutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iaz.HIgister.ui.main.ProfileActivity;
import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 28/05/2018.
 */

public class ListsTutorialSearch1 extends Fragment {



    ListsTutorialActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ListsTutorialActivity) getActivity();
//        activity.fragmentProfile = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lists_tutorial_search1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
