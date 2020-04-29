package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.models.Order;
import com.ivan.thesisproject.models.Product;
import com.ivan.thesisproject.models.User;
import com.ivan.thesisproject.retrofit.CallApi;
import com.ivan.thesisproject.retrofit.MyRetrofit;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity {
    List<Product> productList;
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        final Context context = this;

        sharedPrefUtils = new SharedPrefUtils(
                getSharedPreferences(
                        SharedPrefKey.PREFERENCE_KEY, Context.MODE_PRIVATE));

        final SharedPreferences sharedPreferences = getSharedPreferences(
                SharedPrefKey.PREFERENCE_KEY, MODE_PRIVATE);

        if (getIntent().getStringExtra("product") != null) {
            final Product product = new Gson().fromJson(getIntent()
                    .getStringExtra("product"), Product.class);
            TextView totalPriceTv = findViewById(R.id.orderDetail_price);
            TextView totalItemTv = findViewById(R.id.orderDetail_total_item);
            final EditText addressEd = findViewById(R.id.orderDetail_address);
            User user = new Gson().fromJson(sharedPrefUtils.get(SharedPrefKey.USER_KEY), User.class);
            addressEd.setText(user.getAddress());
            int totalPrice = product.getPrice() * Integer.parseInt(product.getOrderQuantity());
            totalPriceTv.setText(String.valueOf(totalPrice));
            totalItemTv.setText("1");

            Button button = findViewById(R.id.orderDetail_confirm_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog dialog = ProgressDialog.show(context, "",
                            "Sending order , please wait ...", true);
                    String address = addressEd.getText().toString();
                    if (address.equals("")) {
                        addressEd.setError("Please fill the address");
                        return;
                    }
                    User user = new Gson().fromJson(sharedPrefUtils.get(SharedPrefKey.USER_KEY), User.class);
                    List<Product> singleProductList = new ArrayList<>();
                    singleProductList.add(product);
                    Order order = new Order(singleProductList, address, user.getId());
                    MyRetrofit myRetrofit = new MyRetrofit(sharedPreferences);
                    myRetrofit.sendOrder(order, new CallApi<String>() {
                        @Override
                        public void onSuccess(String result) {
                            dialog.dismiss();
                            sharedPrefUtils.remove(SharedPrefKey.CART_KEY);
                            Toast.makeText(context, result,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailed(String message) {
                            dialog.dismiss();
                            Toast.makeText(context, message,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        } else {


            String cartString = sharedPrefUtils.get(SharedPrefKey.CART_KEY);
            if (cartString != null) {
                productList = new Gson().fromJson(cartString, new TypeToken<List<Product>>() {
                }.getType());
            }

            int totalPrice = 0;
            int totalItem = productList.size();
            for (Product p : productList) {
                int price = p.getPrice();
                try {
                    int orderQuantity = Integer.parseInt(p.getOrderQuantity());
                    totalPrice = totalPrice + (price * orderQuantity);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

            TextView totalPriceTv = findViewById(R.id.orderDetail_price);
            TextView totalItemTv = findViewById(R.id.orderDetail_total_item);
            final EditText addressEd = findViewById(R.id.orderDetail_address);
            User user = new Gson().fromJson(sharedPrefUtils.get(SharedPrefKey.USER_KEY), User.class);
            addressEd.setText(user.getAddress());
            totalPriceTv.setText(String.valueOf(totalPrice));
            totalItemTv.setText(String.valueOf(totalItem));

            Button button = findViewById(R.id.orderDetail_confirm_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog dialog = ProgressDialog.show(context, "",
                            "Sending order , please wait ...", true);
                    String address = addressEd.getText().toString();
                    if (address.equals("")) {
                        addressEd.setError("Please fill the address");
                        return;
                    }
                    User user = new Gson().fromJson(sharedPrefUtils.get(SharedPrefKey.USER_KEY), User.class);
                    Order order = new Order(productList, address, user.getId());
                    MyRetrofit myRetrofit = new MyRetrofit(sharedPreferences);
                    myRetrofit.sendOrder(order, new CallApi<String>() {
                        @Override
                        public void onSuccess(String result) {
                            dialog.dismiss();
                            sharedPrefUtils.remove(SharedPrefKey.CART_KEY);
                            Toast.makeText(context, result,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailed(String message) {
                            dialog.dismiss();
                            Toast.makeText(context, message,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
