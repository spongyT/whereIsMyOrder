package com.spongyt.wimo.runnable;

import android.content.SharedPreferences;

import com.spongyt.wimo.SharedPreferencesKeys;
import com.spongyt.wimo.background.RetryRunnable;
import com.spongyt.wimo.config.ApplicationConfig;
import com.spongyt.wimo.networking.user.UserRequestResponse;
import com.spongyt.wimo.networking.user.UserService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class SendRegistrationTokenToServerRunnable extends RetryRunnable{

    private final static int NUMBER_OF_RETRIES = 3;
    private final static int DELAY_BETWEEN_RETRIES = 2000;
    private String token;

    public SendRegistrationTokenToServerRunnable(String token) {
        super(NUMBER_OF_RETRIES, DELAY_BETWEEN_RETRIES);
        this.token = token;
    }

    @Override
    public void tryOnce() throws Exception {
        UserService userService = ApplicationConfig.retrofit().create(UserService.class);
        SharedPreferences sharedPreferences = ApplicationConfig.sharedPreferences();
        String userId = sharedPreferences.getString(SharedPreferencesKeys.ID, null);
        UserRequestResponse request = new UserRequestResponse();
        request.setGcmToken(token);
        Call<Object> sendTokenCall = userService.sendToken(userId, request);
            Response response = sendTokenCall.execute();
            if(response.isSuccess()){
                sharedPreferences.edit().putBoolean(SharedPreferencesKeys.SENT_REGISTRATION_TOKEN_TO_SERVER, true).apply();
            }else{
                throw new Exception("Response from server was " + response.code()+ ":"+ response.message());
            }
    }
}
