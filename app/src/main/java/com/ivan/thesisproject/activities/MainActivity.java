package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ivan.thesisproject.R;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goShopping(View view) {
        Intent intent = new Intent(this,ShoppingActivity.class);
        startActivity(intent);
    }

    public void goCart(View view) {
        Intent intent = new Intent(this,CartActivity.class);
        startActivity(intent);
    }

    public void goLogout(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(getSharedPreferences(
                SharedPrefKey.PREFERENCE_KEY,MODE_PRIVATE));
        sharedPrefUtils.remove(SharedPrefKey.USER_KEY);
        startActivity(intent);
        finish();
    }

    public void goOrderHistory(View view) {
        Intent intent = new Intent(this,OrderHistoryActivity.class);
        startActivity(intent);
    }
}
