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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

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
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    LinkedHashTreeMap map = response.body();
                    assert map != null;
                    List objectList = (List) map.get("products");
                    List<Product> productList = gson.fromJson(gson.toJson(objectList), new TypeToken<List<Product>>() {
                    }.getType());
                    callApi.onSuccess(productList);
                }
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {
                System.out.println("Error occurred during retrieving the product , please check your internet connection");
            }
        });
    }

    public void getAllCategory(final CallApi<List<String>> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> getCategoryCall = apiService.getAllCategory();
        getCategoryCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    LinkedHashTreeMap map = response.body();
                    assert map != null;
                    List objectList = (List) map.get("categories");
                    List<String> categoryList = gson.fromJson(gson.toJson(objectList), new TypeToken<List<String>>() {
                    }.getType());
                    callApi.onSuccess(categoryList);
                }
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {
                System.out.println("Error occurred during retrieving the categories , please check your internet connection");
            }
        });
    }

    public void sendOrder(Order order, final CallApi<String> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> getProductsCall = apiService.sendOrder(order);
        getProductsCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()) {
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

    public void userLogin(UserAuth userAuth, final CallApi<Boolean> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        System.out.println(new Gson().toJson(userAuth));
        Call<LinkedHashTreeMap> userLoginCall = apiService.userLogin(userAuth);
        userLoginCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    LinkedHashTreeMap result = response.body();
                    assert result != null;
                    User user = gson.fromJson(gson.toJson(result.get("user")), User.class);
                    assert user != null;
                    if (user.getUsername() != null && !user.getUsername().equals("")) {
                        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(sharedPreferences);
                        sharedPrefUtils.save(SharedPrefKey.USER_KEY, new Gson().toJson(user));
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

    public void getOrderHistory(int id, final CallApi<List<Order>> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> call = apiService.getOrderHistory(id);
        call.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                LinkedHashTreeMap map = response.body();
                String ordersJson = new Gson().toJson(map.get("orders"));
                List<Order> orderList = new Gson().fromJson(ordersJson, new TypeToken<List<Order>>() {
                }.getType());
                callApi.onSuccess(orderList);
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {

            }
        });
    }

    public void getAllProductByFilter(String filter, final CallApi<List<Product>> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> getProductsCall = apiService.getAllProductByFilter(filter);
        getProductsCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    LinkedHashTreeMap map = response.body();
                    assert map != null;
                    List objectList = (List) map.get("products");
                    List<Product> productList = gson.fromJson(gson.toJson(objectList), new TypeToken<List<Product>>() {
                    }.getType());
                    callApi.onSuccess(productList);
                }
            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {
                System.out.println("Error occurred during retrieving the product , please check your internet connection");
            }
        });
    }

    public void uploadPaymentEvidence(MultipartBody.Part part, String id, final CallApi<String> callApi) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<LinkedHashTreeMap> uploadEvidenceCall = apiService.uploadPaymentEvidence(part, id);
        uploadEvidenceCall.enqueue(new Callback<LinkedHashTreeMap>() {
            @Override
            public void onResponse(Call<LinkedHashTreeMap> call, Response<LinkedHashTreeMap> response) {

            }

            @Override
            public void onFailure(Call<LinkedHashTreeMap> call, Throwable t) {

            }
        });
    }
}
