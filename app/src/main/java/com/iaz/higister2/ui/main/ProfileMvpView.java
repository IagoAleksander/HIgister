package com.iaz.higister2.ui.main;

import android.net.Uri;

import com.iaz.higister2.data.model.User;
import com.iaz.higister2.ui.base.MvpView;

public interface ProfileMvpView extends MvpView {

    ProfileActivity getActivity();

    void updateData(User user);
    void showSnackBar(String msg);
    void callGlide(Uri uri);

}
