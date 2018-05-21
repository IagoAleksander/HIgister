package com.iaz.HIgister.ui.viewUser;

import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.ui.base.MvpView;

import java.util.ArrayList;

public interface UserListsMvpView extends MvpView {

    UserListsFragment getFragment();

    void updateDataLists(ArrayList<UserList> lists);
}
