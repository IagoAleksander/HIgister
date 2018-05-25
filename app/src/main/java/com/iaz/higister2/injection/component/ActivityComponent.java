package com.iaz.higister.injection.component;

import com.iaz.higister.injection.PerActivity;
import com.iaz.higister.injection.module.ActivityModule;
import com.iaz.higister.ui.createItem.CreateItemActivity;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.login.AuthActivity;
import com.iaz.higister.ui.main.MyListsFragment;
import com.iaz.higister.ui.main.ProfileActivity;
import com.iaz.higister.ui.splash.SplashActivity;
import com.iaz.higister.ui.temp.TempTempMainActivity;
import com.iaz.higister.ui.viewItem.ViewItemActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;
import com.iaz.higister.ui.viewUser.UserListsFragment;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(TempTempMainActivity tempMainActivity);

    void inject(AuthActivity authActivity);

    void inject(ProfileActivity profileActivity);

    void inject(MyListsFragment listsFragment);

    void inject(CreateListActivity createListActivity);

    void inject(ViewListActivity viewListActivity);

    void inject(CreateItemActivity createItemActivity);

    void inject(ViewItemActivity viewItemActivity);

    void inject(UserListsFragment listsFragment);

}
