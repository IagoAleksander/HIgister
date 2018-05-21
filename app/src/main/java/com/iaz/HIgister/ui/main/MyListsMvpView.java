package com.iaz.HIgister.ui.main;

import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.ui.base.MvpView;

import java.util.ArrayList;

public interface MyListsMvpView extends MvpView {

    MyListsFragment getFragment();

    void updateDataLists(ArrayList<UserList> lists);
    void updateDataPeople(ArrayList<User> peopleList);

}
