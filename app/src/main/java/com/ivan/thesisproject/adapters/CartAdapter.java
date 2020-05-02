package com.ivan.thesisproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.activities.CartActivity;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class CartAdapter extends ArrayAdapter<Product> {

    private int resourceLayout;
    private Context mContext;
    private List<Product> cartList;

    public CartAdapter(@NonNull Context context, int resource, List<Product> listItem) {
        super(context, resource, listItem);
        this.resourceLayout = resource;
        this.mContext = context;
        cartList = listItem;
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

        final Product product = getItem(position);

        final SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(
                mContext.getSharedPreferences(
                        SharedPrefKey.PREFERENCE_KEY, Context.MODE_PRIVATE));

        TextView productNameTv = v.findViewById(R.id.cart_product_name);
        TextView productPriceTv = v.findViewById(R.id.cart_product_price);
        TextView productVendorTv = v.findViewById(R.id.cart_vendor_name);
        final EditText productQuantityEd = v.findViewById(R.id.cart_product_quantity);
        ImageView addButton = v.findViewById(R.id.cart_add_product_button);
        ImageView subtractButton = v.findViewById(R.id.cart_subtract_product_button);
        ImageView removeButton = v.findViewById(R.id.cart_remove_product_button);
        final ImageView productImageView = v.findViewById(R.id.cart_product_image);
        assert product != null;
        String priceLabel = "Rp " + String.valueOf(product.getPrice());
        productNameTv.setText(product.getProductName());
        productPriceTv.setText(priceLabel);
        productVendorTv.setText(product.getVendorName());
        productQuantityEd.setText(product.getOrderQuantity());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = productQuantityEd.getText().toString();
                int quantityInteger = Integer.parseInt(quantity);
                quantityInteger += 1;
                product.setOrderQuantity(String.valueOf(quantityInteger));
                cartList.set(cartList.indexOf(product), product);
                sharedPrefUtils.save(SharedPrefKey.CART_KEY, new Gson().toJson(cartList));
                productQuantityEd.setText(String.valueOf(quantityInteger));
                ((CartActivity) mContext).setTotalPrice();
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = productQuantityEd.getText().toString();
                int quantityInteger = Integer.parseInt(quantity);
                if (quantityInteger > 1) {
                    quantityInteger -= 1;
                }
                product.setOrderQuantity(String.valueOf(quantityInteger));
                cartList.set(cartList.indexOf(product), product);
                sharedPrefUtils.save(SharedPrefKey.CART_KEY, new Gson().toJson(cartList));
                productQuantityEd.setText(String.valueOf(quantityInteger));
                ((CartActivity) mContext).setTotalPrice();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(product);
                notifyDataSetChanged();
                cartList.remove(product);
                sharedPrefUtils.save(SharedPrefKey.CART_KEY, new Gson().toJson(cartList));
                ((CartActivity) mContext).setTotalPrice();
            }
        });

        productQuantityEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantity = productQuantityEd.getText().toString();
                if (quantity.equals("0") || quantity.equals("")){
                    quantity = "1";
                    productQuantityEd.setText("1");
                }
                product.setOrderQuantity(quantity);
                cartList.set(cartList.indexOf(product), product);
                sharedPrefUtils.save(SharedPrefKey.CART_KEY, new Gson().toJson(cartList));
                ((CartActivity) mContext).setTotalPrice();
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
