package com.spongyt.wimo.gcm;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.spongyt.wimo.R;
import com.spongyt.wimo.config.ApplicationConfig;
import com.spongyt.wimo.runnable.SendRegistrationTokenToServerRunnable;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class GcmRegistrationIntentService extends IntentService {

    private final static String EXTRA_REGISTRATION_TOKEN = "EXTRA_REGISTRATION_TOKEN";

    public GcmRegistrationIntentService() {
        super(GcmRegistrationIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.hasExtra(EXTRA_REGISTRATION_TOKEN)){
            sendRegistrationTokenToServer(intent.getStringExtra(EXTRA_REGISTRATION_TOKEN));
            return;
        }

        InstanceID instanceID = InstanceID.getInstance(this);
        String token;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Timber.d("Retrieved registration token: %s", token);
        } catch (IOException e) {
            Timber.e(e, "Error while taking registration token");
            return;
        }

        sendRegistrationTokenToServer(token);
    }

    private void sendRegistrationTokenToServer(String token) {
        ApplicationConfig.executorService().execute(new SendRegistrationTokenToServerRunnable(token));
    }
}
