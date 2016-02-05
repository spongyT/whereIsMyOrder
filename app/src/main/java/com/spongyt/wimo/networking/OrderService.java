package com.spongyt.wimo.networking;

import com.spongyt.wimo.networking.model.OrderRequestResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tmichelc on 04.02.2016.
 */
public interface OrderService {

    @POST("orders")
    public Call<OrderRequestResponse> createOrder(@Body OrderRequestResponse order);
}
