package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final Product product = new Gson().fromJson(getIntent().getStringExtra("product"), Product.class);
        initButton(product);

        final ImageView imageView = findViewById(R.id.product_detail_imageView);
        TextView nameTv = findViewById(R.id.product_detail_name_textView);
        TextView priceTv = findViewById(R.id.product_detail_price_textView);
        TextView vendorTv = findViewById(R.id.product_detail_vendor_textView);

        String priceLabel = "Rp " + String.valueOf(product.getPrice());
        nameTv.setText(product.getProductName());
        priceTv.setText(priceLabel);
        vendorTv.setText(product.getVendorName());

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                // Get image from data Notification
                Picasso.get()
                        .load(product.getImageUrl())
                        .into(target);
            }
        });
    }

    public void initButton(Product product) {
        final SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(
                getSharedPreferences(
                        SharedPrefKey.PREFERENCE_KEY, Context.MODE_PRIVATE));
        String cartString = sharedPrefUtils.get(SharedPrefKey.CART_KEY);
        Button button = findViewById(R.id.product_detail_addToCart);
        List<Product> cart;
        if (cartString != null) {
            cart = new Gson().fromJson(cartString, new TypeToken<List<Product>>() {
            }.getType());

            if (cart.contains(product)) {
                button.setText("Added to cart");
                button.setClickable(Boolean.FALSE);
                button.setBackground(getResources().getDrawable(R.drawable.form_background));
                button.setTextColor(Color.GRAY);
            } else {
                button.setText("Add to cart");
                button.setClickable(Boolean.TRUE);
                button.setTextColor(getResources().getColor(R.color.colorPrimary));
                button.setBackground(getResources().getDrawable(R.drawable.shopping_sort_button));
            }
        }
    }

    public void addProductToCart(View view) {
        final Product product = new Gson().fromJson(getIntent().getStringExtra("product"), Product.class);
        Button button = findViewById(R.id.product_detail_addToCart);
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(getSharedPreferences(
                SharedPrefKey.PREFERENCE_KEY, MODE_PRIVATE));
        String cartString = sharedPrefUtils.get(SharedPrefKey.CART_KEY);
        if (cartString != null) {
            List<Product> cart = new Gson().fromJson(cartString, new TypeToken<List<Product>>() {
            }.getType());
            cart.add(product);
            sharedPrefUtils.save(SharedPrefKey.CART_KEY, new Gson().toJson(cart));
        } else {
            List<Product> cart = new ArrayList<>();
            cart.add(product);
            sharedPrefUtils.save(SharedPrefKey.CART_KEY, new Gson().toJson(cart));
        }

        button.setText("Added to cart");
        button.setClickable(Boolean.FALSE);
        button.setBackground(getResources().getDrawable(R.drawable.form_background));
        button.setTextColor(Color.GRAY);
    }

    public void buyProduct(View view) {
        final Product product = new Gson().fromJson(getIntent().getStringExtra("product"), Product.class);
        Intent intent = new Intent(this,CartActivity.class);
        intent.putExtra("product",new Gson().toJson(product));
        startActivity(intent);
    }
}
