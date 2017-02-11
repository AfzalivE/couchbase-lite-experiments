package com.afzaln.cblexperiments;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by afzal on 2017-02-11.
 */

public class CblApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
