package com.iaz.higister2.ui.viewItem;

import com.iaz.higister2.data.DataManager;
import com.iaz.higister2.injection.ConfigPersistent;
import com.iaz.higister2.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class ViewItemPresenter extends BasePresenter<ViewItemMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    private ViewItemActivity activity;

    @Inject
    public ViewItemPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ViewItemMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setActivity(ViewItemActivity activity) {
        this.activity = activity;
    }

}
