package com.iaz.higister.ui.createItem;

import android.net.Uri;

import com.iaz.higister.ui.base.MvpView;

public interface CreateItemMvpView extends MvpView {

    void callGlide(Uri uri);
    void dismissDialog();
    void showSnackBar(String msg);

    CreateItemActivity getActivity();
}
