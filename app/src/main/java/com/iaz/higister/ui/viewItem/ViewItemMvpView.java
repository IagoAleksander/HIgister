package com.iaz.higister.ui.viewItem;

import android.net.Uri;

import com.iaz.higister.ui.base.MvpView;

public interface ViewItemMvpView extends MvpView {

    void callGlide(Uri uri);
    void dismissDialog();
    void showSnackBar(String msg);

    ViewItemActivity getActivityFromView();
}