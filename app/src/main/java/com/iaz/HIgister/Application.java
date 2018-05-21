package com.iaz.HIgister;

import android.content.Context;

import com.crashlytics.android.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.iaz.HIgister.injection.component.ApplicationComponent;
import com.iaz.HIgister.injection.component.DaggerApplicationComponent;
import com.iaz.HIgister.injection.module.ApplicationModule;
import com.iaz.HIgister.util.ApplicationUtil;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class Application extends android.app.Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationUtil.setContext(getApplicationContext());
        FirebaseApp.initializeApp(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Fabric.with(this, new Crashlytics());
        }
    }

    public static Application get(Context context) {
        return (Application) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
