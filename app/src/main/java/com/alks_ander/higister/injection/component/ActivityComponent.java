package com.alks_ander.higister.injection.component;

import dagger.Subcomponent;
import com.alks_ander.higister.injection.PerActivity;
import com.alks_ander.higister.injection.module.ActivityModule;
import com.alks_ander.higister.ui.main.MainActivity;
import com.alks_ander.higister.ui.search.SearchActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SearchActivity searchActivity);

}
