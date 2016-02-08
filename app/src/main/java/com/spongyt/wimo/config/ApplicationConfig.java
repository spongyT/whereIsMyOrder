package com.spongyt.wimo.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.spongyt.wimo.WimoApplication;
import com.spongyt.wimo.networking.AuthenticationInterceptor;
import com.spongyt.wimo.networking.CreateUserInterceptor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class ApplicationConfig {

    private static Retrofit retrofit;
    private static Retrofit unsecuredRetrofit;
    private static SharedPreferences sharedPreferences;
    private static ScheduledExecutorService executorService;

    public static Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = createRetrofit(true);
        }

        return retrofit;
    }

    public static Retrofit unsecuredRetrofit() {
        if (unsecuredRetrofit == null) {
            unsecuredRetrofit = createRetrofit(false);
        }

        return unsecuredRetrofit;
    }

    private static Retrofit createRetrofit(boolean secured) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (secured) {
            builder.addInterceptor(new CreateUserInterceptor());
            builder.addInterceptor(new AuthenticationInterceptor());
        }
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pjtxcpwkwzlwirwf.myfritz.net:8081")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }


    public static SharedPreferences sharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = WimoApplication.getInstance().getSharedPreferences("wimo", Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }


    public static ScheduledExecutorService executorService() {
        if (executorService == null) {
            int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
            executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        }

        return executorService;
    }

}
