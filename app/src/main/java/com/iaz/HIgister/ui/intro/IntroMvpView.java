package com.iaz.HIgister.ui.intro;

import android.net.Uri;

import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.ui.base.MvpView;
import com.iaz.HIgister.ui.main.ProfileActivity;

public interface IntroMvpView extends MvpView {

    void showSnackBar(String msg);
    void callGlide(Uri uri);
    void dismissDialog();

}
