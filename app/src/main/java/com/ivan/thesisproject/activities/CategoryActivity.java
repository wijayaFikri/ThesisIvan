package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.ivan.thesisproject.R;
import com.ivan.thesisproject.adapters.CategoryAdapter;
import com.ivan.thesisproject.retrofit.CallApi;
import com.ivan.thesisproject.retrofit.MyRetrofit;
import com.ivan.thesisproject.utilities.SharedPrefKey;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final Context context = this;
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Getting list of Category , please wait ...", true);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefKey.PREFERENCE_KEY,
                MODE_PRIVATE);
        MyRetrofit myRetrofit = new MyRetrofit(sharedPreferences);
        myRetrofit.getAllCategory(new CallApi<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                dialog.dismiss();
                ListView listView = findViewById(R.id.category_listView);
                CategoryAdapter categoryAdapter = new CategoryAdapter(context,R.layout.category_item,result);
                listView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailed(String message) {
                dialog.dismiss();
                Toast.makeText(context, message,
                        Toast.LENGTH_LONG).show();
                System.out.println(message);
            }
        });
    }

    public void goShopFromCategory(String filter){
        Intent intent = new Intent(this,ShoppingActivity.class);
        intent.putExtra("filter",filter);
        startActivity(intent);
        finish();
    }
}
