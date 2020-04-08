package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.adapters.OrderHistoryAdapter;
import com.ivan.thesisproject.models.Order;
import com.ivan.thesisproject.models.User;
import com.ivan.thesisproject.retrofit.CallApi;
import com.ivan.thesisproject.retrofit.MyRetrofit;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        SharedPreferences sharedPreferences = getSharedPreferences(
                SharedPrefKey.PREFERENCE_KEY, MODE_PRIVATE);
        MyRetrofit myRetrofit = new MyRetrofit(sharedPreferences);
        final Context context = this;
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(sharedPreferences);
        User user = new Gson().fromJson(sharedPrefUtils.get(SharedPrefKey.USER_KEY), User.class);
        final ProgressDialog dialog = ProgressDialog.show(context, "",
                "Getting List of Order , please wait ...", true);
        myRetrofit.getOrderHistory(user.getId(), new CallApi<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                dialog.dismiss();
                ListView lv = findViewById(R.id.orderHistory_listView);
                OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(context, R.layout.order_status_item, result);
                lv.setDivider(null);
                lv.setAdapter(orderHistoryAdapter);
            }

            @Override
            public void onFailed(String message) {
                dialog.dismiss();
            }
        });
    }
}
