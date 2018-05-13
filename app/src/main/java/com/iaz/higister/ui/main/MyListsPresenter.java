package com.iaz.higister.ui.main;

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
public class MyListsPresenter extends BasePresenter<MyListsMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;

    @Inject
    MyListsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MyListsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void search(String filter, int type) {
        if (type == 8)  {
            getMvpView().getFragment().userRepository.filterResult(filter, new UserRepository.OnGetUsers() {
                @Override
                public void onSuccess(ArrayList<User> peopleList) {
                    getMvpView().getFragment().updateDataPeople(peopleList);
                }

                @Override
                public void onFailure(String exception) {

                }
            });
        }
        else {
            getMvpView().getFragment().listRepository.filterResult(filter, new ListRepository.OnUpdateLists() {
                @Override
                public void onSuccess(ArrayList<UserList> userLists) {
                    getMvpView().getFragment().updateDataLists(userLists);
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        }
    }
}
