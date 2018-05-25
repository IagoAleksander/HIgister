package com.iaz.higister2.ui.viewItem;

import com.iaz.higister2.ui.base.MvpView;

public interface ViewItemMvpView extends MvpView {

    void showSnackBar(String msg);

    ViewItemActivity getActivityFromView();
}
