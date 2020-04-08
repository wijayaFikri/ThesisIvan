package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.adapters.CartAdapter;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

import java.util.List;

public class CartActivity extends BaseActivity {
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ListView lv = findViewById(R.id.cart_listView);
        sharedPrefUtils = new SharedPrefUtils(
                getSharedPreferences(
                        SharedPrefKey.PREFERENCE_KEY, Context.MODE_PRIVATE));
        setTotalPrice();
        String cartString = sharedPrefUtils.get(SharedPrefKey.CART_KEY);
        if (cartString != null) {
            List<Product> cart = new Gson().fromJson(cartString, new TypeToken<List<Product>>() {
            }.getType());
            lv.setDivider(null);
            CartAdapter cartAdapter = new CartAdapter(this, R.layout.cart_item_detail, cart);
            lv.setAdapter(cartAdapter);
        }
    }

    public void setTotalPrice() {
        String cartString = sharedPrefUtils.get(SharedPrefKey.CART_KEY);
        if (cartString != null) {
            List<Product> cart = new Gson().fromJson(cartString, new TypeToken<List<Product>>() {
            }.getType());
            int totalPrice = 0;
            for (Product p : cart) {
                int price = p.getPrice();
                try {
                    int orderQuantity = Integer.parseInt(p.getOrderQuantity());
                    totalPrice = totalPrice + (price * orderQuantity);
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }

            }
            TextView totalPriceTv = findViewById(R.id.cart_total_price);
            String totalPriceLabel = "Rp " + String.valueOf(totalPrice);
            totalPriceTv.setText(totalPriceLabel);
        }
    }

    public void goOrderDetail(View view) {
        Intent intent = new Intent(this,OrderDetailActivity.class);
        startActivity(intent);
    }
}
