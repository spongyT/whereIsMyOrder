package com.spongyt.wimo.gcm;

import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.spongyt.wimo.SharedPreferencesKeys;
import com.spongyt.wimo.config.ApplicationConfig;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        SharedPreferences sharedPreferences = ApplicationConfig.sharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SharedPreferencesKeys.REGISTRATION_TOKEN);
        editor.remove(SharedPreferencesKeys.SENT_REGISTRATION_TOKEN_TO_SERVER);
        editor.apply();

        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        startService(intent);
    }
}
