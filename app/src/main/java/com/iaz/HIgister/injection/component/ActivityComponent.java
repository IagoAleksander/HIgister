package com.iaz.HIgister.injection.component;

import com.iaz.HIgister.injection.PerActivity;
import com.iaz.HIgister.injection.module.ActivityModule;
import com.iaz.HIgister.ui.createItem.CreateItemActivity;
import com.iaz.HIgister.ui.createList.CreateListActivity;
import com.iaz.HIgister.ui.login.AuthActivity;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.main.MyListsFragment;
import com.iaz.HIgister.ui.main.ProfileActivity;
import com.iaz.HIgister.ui.splash.SplashActivity;
import com.iaz.HIgister.ui.viewItem.ViewItemActivity;
import com.iaz.HIgister.ui.viewList.ViewListActivity;
import com.iaz.HIgister.ui.viewUser.UserListsFragment;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

//    void inject(TempTempMainActivity tempMainActivity);
//
    void inject(AuthActivity authActivity);
//
    void inject(ProfileActivity profileActivity);
//
    void inject(MyListsFragment listsFragment);
//
    void inject(CreateListActivity createListActivity);

    void inject(ViewListActivity viewListActivity);

    void inject(CreateItemActivity createItemActivity);

    void inject(ViewItemActivity viewItemActivity);
//
    void inject(UserListsFragment listsFragment);

}
