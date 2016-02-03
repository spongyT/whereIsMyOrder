package com.spongyt.wimo;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by tmichelc on 03.02.2016.
 */
public class WimoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Init timber logger
        Timber.plant(new Timber.DebugTree());
    }
}
