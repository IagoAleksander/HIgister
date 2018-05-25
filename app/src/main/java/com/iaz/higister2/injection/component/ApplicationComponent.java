package com.iaz.higister.injection.component;

import android.app.Application;
import android.content.Context;

import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.SyncService;
import com.iaz.higister.data.local.DatabaseHelper;
import com.iaz.higister.data.local.PreferencesHelper;
import com.iaz.higister.data.remote.RibotsService;
import com.iaz.higister.injection.ApplicationContext;
import com.iaz.higister.injection.module.ApplicationModule;
import com.iaz.higister.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;

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
