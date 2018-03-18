package com.iaz.higister.ui.profile;

import com.iaz.higister.data.model.User;
import com.iaz.higister.ui.base.MvpView;
import com.iaz.higister.ui.login.AuthActivity;

public interface ProfileMvpView extends MvpView {

    ProfileFragment getFragment();

    void updateData(User user);

}
