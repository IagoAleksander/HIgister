package com.alks_ander.higister.ui.createList;

import android.net.Uri;

import com.alks_ander.higister.data.model.Ribot;
import com.alks_ander.higister.ui.base.MvpView;

import java.util.List;

public interface CreateListMvpView extends MvpView {

    void callGlide(Uri uri);
    void callGlideBanner(Uri uri);
    void dismissDialog();
    void showSnackBar(String msg);
}
