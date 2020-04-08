package com.ivan.thesisproject.retrofit;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;
import com.ivan.thesisproject.models.Order;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.models.User;
import com.ivan.thesisproject.models.UserAuth;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;

    public MyRetrofit(SharedPreferences sharedPreferences) {
        String BASE_URL = "https://thesis-ivan.herokuapp.com/";
        retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        this.sharedPreferences = sharedPreferences;
    }

    public void getAllProduct(final CallApi<List<Product>> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> getProductsCall = apiService.getAllProduct();
        getProductsCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()){
                    Gson gson = new Gson();
                    LinkedHashTreeMap map = response.body();
                    assert map != null;
                    List objectList = (List) map.get("products");
                    List<Product> productList = gson.fromJson(gson.toJson(objectList), new TypeToken<List<Product>>()
                    {}.getType());
                    callApi.onSuccess(productList);
                }
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {
                System.out.println("Error occurred during getting the product , please check your internet connection");
            }
        });
    }

    public void sendOrder(Order order , final CallApi<String> callApi){
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> getProductsCall = apiService.sendOrder(order);
        getProductsCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()){
                    callApi.onSuccess("Order placed successfully");
                } else {
                    callApi.onFailed("Something wrong happened when we try to put order, " +
                            "please try again later");
                }
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {
                    callApi.onFailed("Something wrong happened when we try to put order, " +
                            "please try again later");
                System.out.println(t.getMessage());
            }
        });
    }

    public void userLogin(UserAuth userAuth , final CallApi<Boolean> callApi){
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> userLoginCall = apiService.userLogin(userAuth);
        userLoginCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    LinkedHashTreeMap result = response.body();
                    assert result != null;
                    User user = gson.fromJson(gson.toJson(result.get("user")),User.class);
                    assert user != null;
                    if (user.getUsername() != null && !user.getUsername().equals("")){
                        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(sharedPreferences);
                        sharedPrefUtils.save(SharedPrefKey.USER_KEY,new Gson().toJson(user));
                        callApi.onSuccess(Boolean.TRUE);
                    } else {
                        callApi.onFailed("Wrong username or password");
                    }
                }
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {
                        callApi.onFailed("Something went wrong , please check your internet connection");
            }
        });
    }
}
