package com.iaz.higister.ui.main;

import android.net.Uri;

import com.iaz.higister.data.model.User;
import com.iaz.higister.ui.base.MvpView;

public interface ProfileMvpView extends MvpView {

    ProfileActivity getActivity();

    void updateData(User user);
    void showSnackBar(String msg);
    void callGlide(Uri uri);

}
