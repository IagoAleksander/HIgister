package com.iaz.higister.ui.main;

import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.base.MvpView;

import java.util.ArrayList;

public interface MyListsMvpView extends MvpView {

    MyListsFragment getFragment();

    void updateDataLists(ArrayList<UserList> lists);
    void updateDataPeople(ArrayList<User> peopleList);

}
