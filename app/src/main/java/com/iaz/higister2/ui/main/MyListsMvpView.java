package com.iaz.higister2.ui.main;

import com.iaz.higister2.data.model.User;
import com.iaz.higister2.data.model.UserList;
import com.iaz.higister2.ui.base.MvpView;

import java.util.ArrayList;

public interface MyListsMvpView extends MvpView {

    MyListsFragment getFragment();

    void updateDataLists(ArrayList<UserList> lists);
    void updateDataPeople(ArrayList<User> peopleList);

}
