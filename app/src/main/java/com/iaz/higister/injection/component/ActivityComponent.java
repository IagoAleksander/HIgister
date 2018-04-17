package com.iaz.higister.injection.component;

import dagger.Subcomponent;
import com.iaz.higister.injection.PerActivity;
import com.iaz.higister.injection.module.ActivityModule;
import com.iaz.higister.ui.createItem.CreateItemActivity;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.login.AuthActivity;
import com.iaz.higister.ui.temp.TempTempMainActivity;
import com.iaz.higister.ui.main.MyListsFragment;
import com.iaz.higister.ui.main.ProfileFragment;
import com.iaz.higister.ui.splash.SplashActivity;
import com.iaz.higister.ui.viewItem.ViewItemActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(TempTempMainActivity tempMainActivity);

    void inject(AuthActivity authActivity);

    void inject(ProfileFragment profileFragment);

    void inject(MyListsFragment listsFragment);

    void inject(CreateListActivity createListActivity);

    void inject(ViewListActivity viewListActivity);

    void inject(CreateItemActivity createItemActivity);

    void inject(ViewItemActivity viewItemActivity);

}
