package com.ivan.thesisproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.activities.ProductDetailActivity;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class ProductGridAdapter extends ArrayAdapter<Product> {
    private int resourceLayout;
    private Context mContext;

    public ProductGridAdapter(@NonNull Context context, int resource, List<Product> listItem) {
        super(context, resource, listItem);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }
        final SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(
                mContext.getSharedPreferences(
                        SharedPrefKey.PREFERENCE_KEY, Context.MODE_PRIVATE));
        String cartString = sharedPrefUtils.get(SharedPrefKey.CART_KEY);
        final Product product = getItem(position);
        List<Product> cart;
        TextView productNameTv = v.findViewById(R.id.gridItem_shopping_productName);
        TextView productPriceTv = v.findViewById(R.id.gridItem_shopping_productPrice);
        TextView productVendorTv = v.findViewById(R.id.gridItem_shopping_productVendor);
        CardView cv = v.findViewById(R.id.gridItem_shopping_cardView);
        final ImageView productImageView = v.findViewById(R.id.gridItem_shopping_image);
        final Button addProductButton = v.findViewById(R.id.gridItem_shopping_addButton);

        assert product != null;
        String priceLabel = "Rp " + String.valueOf(product.getPrice());
        productNameTv.setText(product.getProductName());
        productVendorTv.setText(product.getVendorName());
        productPriceTv.setText(priceLabel);

        if (cartString != null) {
            cart = new Gson().fromJson(cartString, new TypeToken<List<Product>>() {
            }.getType());

            if (cart.contains(product)){
                addProductButton.setText("Added to cart");
                addProductButton.setClickable(Boolean.FALSE);
                addProductButton.setBackground(mContext.getResources().getDrawable(R.drawable.form_background));
                addProductButton.setTextColor(Color.GRAY);
            } else {
                addProductButton.setText("Add to cart");
                addProductButton.setClickable(Boolean.TRUE);
                addProductButton.setBackground(mContext.getResources().getDrawable(R.drawable.login_button_background));
                addProductButton.setTextColor(Color.WHITE);
            }
        }

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                addProductButton.setText("Added to cart");
                addProductButton.setClickable(Boolean.FALSE);
                addProductButton.setBackground(mContext.getResources().getDrawable(R.drawable.form_background));
                addProductButton.setTextColor(Color.GRAY);
            }
        });

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("product", new Gson().toJson(product));
                mContext.startActivity(intent);
            }
        });

        Picasso.get().load(product.getImageUrl()).into(productImageView);

        /*final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                productImageView.setImageBitmap(bitmap);
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
        });*/
        return v;
    }
}
