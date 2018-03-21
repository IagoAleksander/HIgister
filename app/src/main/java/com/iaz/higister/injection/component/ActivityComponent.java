package com.iaz.higister.injection.component;

import dagger.Subcomponent;
import com.iaz.higister.injection.PerActivity;
import com.iaz.higister.injection.module.ActivityModule;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.login.AuthActivity;
import com.iaz.higister.ui.main.MainActivity;
import com.iaz.higister.ui.profile.MyListsFragment;
import com.iaz.higister.ui.profile.ProfileFragment;
import com.iaz.higister.ui.search.SearchActivity;
import com.iaz.higister.ui.splash.SplashActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(AuthActivity authActivity);

    void inject(ProfileFragment profileFragment);

    void inject(MyListsFragment listsFragment);

    void inject(CreateListActivity createListActivity);

    void inject(ViewListActivity viewListActivity);

}
