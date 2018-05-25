package com.iaz.higister2.ui.viewUser;

import com.iaz.higister2.data.model.UserList;
import com.iaz.higister2.ui.base.MvpView;

import java.util.ArrayList;

public interface UserListsMvpView extends MvpView {

    UserListsFragment getFragment();

    void updateDataLists(ArrayList<UserList> lists);
}
