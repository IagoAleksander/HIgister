package com.iaz.HIgister.ui.listsTutorial;

import android.net.Uri;

import com.iaz.HIgister.ui.base.MvpView;

public interface ListsTutorialMvpView extends MvpView {

    void showSnackBar(String msg);
    void callGlide(Uri uri);
    void dismissDialog();

}
