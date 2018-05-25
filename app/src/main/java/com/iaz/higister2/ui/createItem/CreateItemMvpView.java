package com.iaz.higister2.ui.createItem;

import android.net.Uri;

import com.iaz.higister2.ui.base.MvpView;

public interface CreateItemMvpView extends MvpView {

    void callGlide(Uri uri);
    void dismissDialog();
    void showSnackBar(String msg);

    CreateItemActivity getActivityFromView();
}
