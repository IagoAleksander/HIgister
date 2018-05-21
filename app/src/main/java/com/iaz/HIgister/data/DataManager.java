package com.iaz.HIgister.data;

import com.iaz.HIgister.data.local.DatabaseHelper;
import com.iaz.HIgister.data.local.PreferencesHelper;
import com.iaz.HIgister.data.remote.RibotsService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }


}
