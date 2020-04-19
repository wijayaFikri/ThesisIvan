package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ivan.thesisproject.R;

public class SortActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
    }

    public void goShopSortByLatestProduct(View view) {
        goShop("latest");
    }

    public void goShopSortByLowestPrice(View view) {
        goShop("lowest_price");
    }

    public void goShopSortByHighestPrice(View view) {
        goShop("highest_price");
    }

    public void goShop(String filter){
        Intent intent = new Intent(this,ShoppingActivity.class);
        intent.putExtra("filter",filter);
        startActivity(intent);
        finish();
    }
}
