package com.iaz.higister2.injection.component;

import android.app.Application;
import android.content.Context;

import com.iaz.higister2.data.DataManager;
import com.iaz.higister2.data.SyncService;
import com.iaz.higister2.data.local.DatabaseHelper;
import com.iaz.higister2.data.local.PreferencesHelper;
import com.iaz.higister2.data.remote.RibotsService;
import com.iaz.higister2.injection.ApplicationContext;
import com.iaz.higister2.injection.module.ApplicationModule;
import com.iaz.higister2.util.RxEventBus;

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
