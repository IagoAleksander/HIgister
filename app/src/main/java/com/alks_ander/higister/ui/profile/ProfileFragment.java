package com.alks_ander.higister.ui.profile;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.text.Editable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by alksander on 05/03/2018.
 */

public class ProfileFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.profile_fragment, container, false);

        return mView;
    }
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
}