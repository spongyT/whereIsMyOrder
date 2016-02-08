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
public class CreateUserRunnable extends RetryRunnable {

    private final static int NUMBER_OF_RETRIES = 3;
    private final static int DELAY_BETWEEN_RETRIES = 2000;
    public static Boolean executed = false;

    public CreateUserRunnable() {
        super(NUMBER_OF_RETRIES, DELAY_BETWEEN_RETRIES);
    }

    @Override
    public void tryOnce() throws Exception{
        synchronized (executed){
            if(executed){
                return;
            }

            UserService userService = ApplicationConfig.unsecuredRetrofit().create(UserService.class);
            Call createUserCall = userService.createUser(new UserRequestResponse());
            Response<UserRequestResponse> response = createUserCall.execute();

            if(response.isSuccess()){
                UserRequestResponse responseBody = response.body();
                SharedPreferences.Editor sharedPreferencesEditor = ApplicationConfig.sharedPreferences().edit();
                sharedPreferencesEditor.putString(SharedPreferencesKeys.ID, responseBody.getId());
                sharedPreferencesEditor.putString(SharedPreferencesKeys.PASSWORD, responseBody.getPassword());
                sharedPreferencesEditor.commit(); // needs to be done synchronously
                executed = true;
            }else{
                throw new Exception("Response from server was " + response.code()+ ":"+ response.message());
            }

        }
    }

}
