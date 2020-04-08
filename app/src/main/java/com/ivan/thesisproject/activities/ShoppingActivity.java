package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.ivan.thesisproject.R;
import com.ivan.thesisproject.adapters.ProductGridAdapter;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.retrofit.CallApi;
import com.ivan.thesisproject.retrofit.MyRetrofit;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        final GridView gridView = findViewById(R.id.shopping_grid_view);
        MyRetrofit myRetrofit = new MyRetrofit(
                getSharedPreferences(SharedPrefKey.PREFERENCE_KEY, MODE_PRIVATE));
        final Context context = this;
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Getting list of product , please wait ...", true);
        myRetrofit.getAllProduct(new CallApi<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                dialog.dismiss();
                ProductGridAdapter productGridAdapter = new ProductGridAdapter(context,
                        R.layout.product_grid_item, result);
                gridView.setAdapter(productGridAdapter);
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
}
