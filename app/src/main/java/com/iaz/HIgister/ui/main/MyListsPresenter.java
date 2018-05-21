package com.iaz.HIgister.ui.main;

import com.iaz.HIgister.data.DataManager;
import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.data.repository.ListRepository;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.injection.ConfigPersistent;
import com.iaz.HIgister.ui.base.BasePresenter;

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

    public void search(String filter, ArrayList<Integer> listTypes) {
        if (listTypes == null)  {
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
            getMvpView().getFragment().listRepository.filterResult(filter, listTypes, new ListRepository.OnUpdateLists() {
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
