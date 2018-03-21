package com.iaz.higister.ui.viewList;

import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.base.MvpView;

public interface ViewListMvpView extends MvpView {

    void updateData(UserList user);
}
