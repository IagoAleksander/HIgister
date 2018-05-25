package com.iaz.higister.ui.viewItem;

import com.iaz.higister.ui.base.MvpView;

public interface ViewItemMvpView extends MvpView {

    void showSnackBar(String msg);

    ViewItemActivity getActivityFromView();
}
