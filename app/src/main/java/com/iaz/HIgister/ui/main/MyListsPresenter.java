package com.iaz.HIgister.ui.main;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
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
            Answers.getInstance().logSearch(new SearchEvent()
                    .putQuery("People Search")
                    .putCustomAttribute("Keyword", filter));

            getMvpView().getFragment().userRepository.filterResult(filter, new UserRepository.OnGetUsers() {
                @Override
                public void onSuccess(ArrayList<User> peopleList) {
                    ListRepository listRepository = new ListRepository();

                    ArrayList<User> peopleListPopulated = new ArrayList<>();

                    for (User user : peopleList) {
                        listRepository.receiveListsOfUser(user.uid, new ListRepository.OnUpdateLists() {
                            @Override
                            public void onSuccess(ArrayList<UserList> userLists) {
                                user.setListsCreatedNumber(userLists.size());

                                listRepository.receiveFavoritesOfUser(user.uid, new ListRepository.OnUpdateLists() {
                                    @Override
                                    public void onSuccess(ArrayList<UserList> userLists2) {
                                        user.setListsFavouritedNumber(userLists2.size());
                                        peopleListPopulated.add(user);

                                        if (peopleListPopulated.size() == peopleList.size())
                                            getMvpView().getFragment().updateDataPeople(peopleList);
                                    }

                                    @Override
                                    public void onFailed(Exception e) {
                                        user.setListsFavouritedNumber(0);
                                        peopleListPopulated.add(user);

                                        if (peopleListPopulated.size() == peopleList.size())
                                            getMvpView().getFragment().updateDataPeople(peopleList);
                                    }
                                }, "favorited");
                            }

                            @Override
                            public void onFailed(Exception e) {
                                user.setListsCreatedNumber(0);
                                listRepository.receiveFavoritesOfUser(user.uid, new ListRepository.OnUpdateLists() {
                                    @Override
                                    public void onSuccess(ArrayList<UserList> userLists2) {
                                        user.setListsFavouritedNumber(userLists2.size());
                                        peopleListPopulated.add(user);

                                        if (peopleListPopulated.size() == peopleList.size())
                                            getMvpView().getFragment().updateDataPeople(peopleList);
                                    }

                                    @Override
                                    public void onFailed(Exception e) {
                                        user.setListsFavouritedNumber(0);
                                        peopleListPopulated.add(user);

                                        if (peopleListPopulated.size() == peopleList.size())
                                            getMvpView().getFragment().updateDataPeople(peopleList);
                                    }
                                }, "favorited");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String exception) {

                }
            });
        }
        else {
            Answers.getInstance().logSearch(new SearchEvent()
                    .putQuery("Lists Search")
                    .putCustomAttribute("Keyword", filter));
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
