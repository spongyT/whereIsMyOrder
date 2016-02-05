package com.spongyt.wimo;

import android.app.Application;
import android.content.Intent;

import com.spongyt.wimo.service.OrderSyncService;

import timber.log.Timber;

/**
 * Created by tmichelc on 03.02.2016.
 */
public class WimoApplication extends Application {

    private static WimoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Init timber logger
        Timber.plant(new Timber.DebugTree());

        // start NetworkService
        startService(new Intent(this, OrderSyncService.class));
    }

    public static WimoApplication getInstance(){
        return instance;
    }
}
