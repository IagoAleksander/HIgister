package com.iaz.higister2.ui.viewUser;

import com.iaz.higister2.data.DataManager;
import com.iaz.higister2.injection.ConfigPersistent;
import com.iaz.higister2.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class UserListsPresenter extends BasePresenter<UserListsMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;

    @Inject
    UserListsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(UserListsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }
}
