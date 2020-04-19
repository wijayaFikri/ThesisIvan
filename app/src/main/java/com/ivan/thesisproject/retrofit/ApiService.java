package com.ivan.thesisproject.retrofit;

import com.google.gson.internal.LinkedHashTreeMap;
import com.ivan.thesisproject.models.Order;
import com.ivan.thesisproject.models.UserAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("/mobile/products")
    Call<LinkedHashTreeMap> getAllProduct();

    @GET("mobile/get/category")
    Call<LinkedHashTreeMap> getAllCategory();

    @POST("/mobile/create/order")
    Call<LinkedHashTreeMap> sendOrder(@Body Order order);

    @POST("mobile/user/login")
    Call<LinkedHashTreeMap> userLogin(@ Body UserAuth userAuth);

    @FormUrlEncoded
    @POST("mobile/order/history")
    Call<LinkedHashTreeMap> getOrderHistory(@Field("userId") int UserId);

    @FormUrlEncoded
    @POST("/mobile/products")
    Call<LinkedHashTreeMap> getAllProductByFilter(@Field("filter") String filter);
}
