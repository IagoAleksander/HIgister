package com.iaz.higister.ui.viewUser;

import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.base.MvpView;

import java.util.ArrayList;

public interface UserListsMvpView extends MvpView {

    UserListsFragment getFragment();

    void updateDataLists(ArrayList<UserList> lists);
}
