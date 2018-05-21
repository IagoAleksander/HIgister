package com.iaz.HIgister.ui.viewItem;

import com.iaz.HIgister.ui.base.MvpView;

public interface ViewItemMvpView extends MvpView {

    void showSnackBar(String msg);

    ViewItemActivity getActivityFromView();
}
