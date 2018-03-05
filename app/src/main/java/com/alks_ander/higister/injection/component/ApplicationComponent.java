package com.alks_ander.higister.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.alks_ander.higister.data.DataManager;
import com.alks_ander.higister.data.SyncService;
import com.alks_ander.higister.data.local.DatabaseHelper;
import com.alks_ander.higister.data.local.PreferencesHelper;
import com.alks_ander.higister.data.remote.RibotsService;
import com.alks_ander.higister.injection.ApplicationContext;
import com.alks_ander.higister.injection.module.ApplicationModule;
import com.alks_ander.higister.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    RibotsService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
