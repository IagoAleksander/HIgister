package com.iaz.higister.ui.viewUser;

import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;

import java.util.ArrayList;

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