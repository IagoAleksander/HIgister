package com.iaz.HIgister.ui.main;

import android.net.Uri;

import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.ui.base.MvpView;

public interface ProfileMvpView extends MvpView {

    ProfileActivity getActivity();

    void updateData(User user);
    void showSnackBar(String msg);
    void callGlide(Uri uri);

}
