package com.iaz.HIgister.injection.component;

import android.app.Application;
import android.content.Context;

import com.iaz.HIgister.data.DataManager;
import com.iaz.HIgister.data.local.DatabaseHelper;
import com.iaz.HIgister.data.local.PreferencesHelper;
import com.iaz.HIgister.data.remote.RibotsService;
import com.iaz.HIgister.injection.ApplicationContext;
import com.iaz.HIgister.injection.module.ApplicationModule;
import com.iaz.HIgister.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext Context context();
    Application application();
    RibotsService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
