package com.iaz.HIgister.ui.intro;

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

public class IntroProfile extends Fragment {

    @BindView(R.id.info_text)
    TextView infoText;

    @BindView(R.id.button_see_profile)
    Button seeProfileButton;

    IntroActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (IntroActivity) getActivity();
//        activity.fragmentProfile = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (activity.user.getName() != null)
            infoText.setText("Congratulations "+activity.user.getName() +"\n\nyou completed the registration process and now are able to really enter in the world of HIgister");

        seeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("isFromIntro", true);
                activity.startActivity(intent);
            }
        });
    }
}
