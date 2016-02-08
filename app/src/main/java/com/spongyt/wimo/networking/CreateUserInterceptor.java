package com.spongyt.wimo.networking;

import android.content.SharedPreferences;

import com.spongyt.wimo.SharedPreferencesKeys;
import com.spongyt.wimo.config.ApplicationConfig;
import com.spongyt.wimo.runnable.CreateUserRunnable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class CreateUserInterceptor implements Interceptor {

    SharedPreferences sharedPreferences;

    public CreateUserInterceptor() {
        sharedPreferences = ApplicationConfig.sharedPreferences();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if(!sharedPreferences.contains(SharedPreferencesKeys.ID) || !sharedPreferences.contains(SharedPreferencesKeys.PASSWORD)){
            try {
                new CreateUserRunnable().tryOnce();
            } catch (Exception e) {
                Timber.e(e, "Error creating user");
            }
        }

        return chain.proceed(request);
    }
}
