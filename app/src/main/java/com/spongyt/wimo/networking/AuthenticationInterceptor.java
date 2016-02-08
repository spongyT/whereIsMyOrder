package com.spongyt.wimo.networking;

import android.content.SharedPreferences;
import android.util.Base64;

import com.spongyt.wimo.SharedPreferencesKeys;
import com.spongyt.wimo.config.ApplicationConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class AuthenticationInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        SharedPreferences sharedPreferences = ApplicationConfig.sharedPreferences();
        String username = sharedPreferences.getString(SharedPreferencesKeys.ID, null);
        String password = sharedPreferences.getString(SharedPreferencesKeys.PASSWORD, null);
        String header = username+":"+password;
        String authToken = Base64.encodeToString(header.getBytes("UTF-8"), Base64.NO_WRAP);
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Basic "+authToken)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
