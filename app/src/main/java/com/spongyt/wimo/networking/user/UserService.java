package com.spongyt.wimo.networking.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by tmichelc on 05.02.2016.
 */
public interface UserService {

    @POST("users")
    public Call<UserRequestResponse> createUser(@Body UserRequestResponse user);

    @PUT("users/{userId}")
    public Call<Object> sendToken(@Path("userId") String userId, @Body UserRequestResponse token);
}
