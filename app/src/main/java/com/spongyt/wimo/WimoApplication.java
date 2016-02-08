package com.spongyt.wimo;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.spongyt.wimo.config.ApplicationConfig;
import com.spongyt.wimo.gcm.GcmRegistrationIntentService;
import com.spongyt.wimo.runnable.CreateUserRunnable;
import com.spongyt.wimo.runnable.SendRegistrationTokenToServerRunnable;
import com.spongyt.wimo.service.OrderSyncService;

import java.util.concurrent.ScheduledExecutorService;

import timber.log.Timber;

/**
 * Created by tmichelc on 03.02.2016.
 */
public class WimoApplication extends Application {

    private static WimoApplication instance;
    private SharedPreferences sharedPreferences;
    private ScheduledExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        sharedPreferences = ApplicationConfig.sharedPreferences();
        executorService = ApplicationConfig.executorService();

        if(!sharedPreferences.contains(SharedPreferencesKeys.REGISTRATION_TOKEN)){
            registerForGcmInBackground();
        }else if(!sharedPreferences.getBoolean(SharedPreferencesKeys.SENT_REGISTRATION_TOKEN_TO_SERVER, false)){
            String token = sharedPreferences.getString(SharedPreferencesKeys.REGISTRATION_TOKEN, null);
            sendRegistrationTokenToServer(token);
        }

        if(!sharedPreferences.contains(SharedPreferencesKeys.ID)) {
            createUserInBackgound();
        }

        // Init timber logger
        Timber.plant(new Timber.DebugTree());

        // start NetworkService
        startService(new Intent(this, OrderSyncService.class));
    }

    private void sendRegistrationTokenToServer(String token) {
        executorService.execute(new SendRegistrationTokenToServerRunnable(token));
    }

    private void registerForGcmInBackground() {
        startService(new Intent(this, GcmRegistrationIntentService.class));
    }

    private void createUserInBackgound() {
        executorService.execute(new CreateUserRunnable());
    }

    public static WimoApplication getInstance(){
        return instance;
    }
}
