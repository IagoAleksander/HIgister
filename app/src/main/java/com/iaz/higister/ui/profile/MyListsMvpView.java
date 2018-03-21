package com.iaz.higister.ui.profile;

import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.base.MvpView;

import java.util.ArrayList;

public interface MyListsMvpView extends MvpView {

    MyListsFragment getFragment();

    void updateData(ArrayList<UserList> lists);

}
