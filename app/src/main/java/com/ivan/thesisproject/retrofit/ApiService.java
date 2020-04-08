package com.ivan.thesisproject.retrofit;

import com.google.gson.internal.LinkedHashTreeMap;
import com.ivan.thesisproject.models.Order;
import com.ivan.thesisproject.models.User;
import com.ivan.thesisproject.models.UserAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("/mobile/products")
    Call<LinkedHashTreeMap> getAllProduct();

    @POST("/mobile/create/order")
    Call<LinkedHashTreeMap> sendOrder(@Body Order order);

    @POST("mobile/user/login")
    Call<LinkedHashTreeMap> userLogin(@ Body UserAuth userAuth);
}
