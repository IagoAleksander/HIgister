package com.iaz.higister.ui.createList;

import android.net.Uri;

import com.iaz.higister.ui.base.MvpView;

public interface CreateListMvpView extends MvpView {

    void callGlide(Uri uri);
    void callGlideBanner(Uri uri);
    void dismissDialog();
    void showSnackBar(String msg);

    CreateListActivity getActivity();
}
