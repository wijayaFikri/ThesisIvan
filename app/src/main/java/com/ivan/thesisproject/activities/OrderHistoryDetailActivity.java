package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.models.Order;
import com.ivan.thesisproject.retrofit.CallApi;
import com.ivan.thesisproject.retrofit.MyRetrofit;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OrderHistoryDetailActivity extends AppCompatActivity {
    String filePath;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);
        verifyStoragePermissions(this);

        final Order order = new Gson().fromJson(getIntent().getStringExtra("order"), Order.class);

        TextView orderIdTv = findViewById(R.id.historyOrderDetail_order_id);
        TextView totalPriceTv = findViewById(R.id.historyOrderDetail_total_price);
        TextView totalItemTv = findViewById(R.id.historyOrderDetail_total_item);
        TextView dateTv = findViewById(R.id.historyOrderDetail_order_date);
        TextView statusTv = findViewById(R.id.historyOrderDetail_status);

        assert order != null;
        String totalItemLabel = String.valueOf(order.getProductList().size()) + " items";
        String totalPriceLabel = "Rp " + String.valueOf(order.getTotalPrice());
        String orderDateLabel = order.getOrderDate();

        orderIdTv.setText(order.getId());
        totalPriceTv.setText(totalPriceLabel);
        totalItemTv.setText(totalItemLabel);
        dateTv.setText(orderDateLabel);
        statusTv.setText(order.getStatus());

        final ImageView imageView = findViewById(R.id.historyOrderDetail_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

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
                        .load(order.getImageUrl())
                        .into(target);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                ImageView imageView = findViewById(R.id.historyOrderDetail_image);
                imageView.setImageURI(selectedImage);

                filePath = getPath(selectedImage);
                System.out.println(filePath);
            }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void submitInvoice(View view) {
        final Context context = this;
        if (filePath != null) {
            final ProgressDialog dialog = ProgressDialog.show(this, "",
                    "Sending evidence , please wait ...", true);
            File file = new File(filePath);
            MultipartBody.Part filePart = MultipartBody.
                    Part.createFormData("file", file.getName(), RequestBody
                    .create(MediaType.parse("image/*"), file));
            SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefKey.PREFERENCE_KEY, MODE_PRIVATE);
            MyRetrofit myRetrofit = new MyRetrofit(sharedPreferences);
            Order order = new Gson().fromJson(getIntent().getStringExtra("order"), Order.class);
            myRetrofit.uploadPaymentEvidence(filePart, order.getId(), new CallApi<String>() {
                @Override
                public void onSuccess(String result) {
                    dialog.dismiss();
                    Toast.makeText(context, "Evidence of payment sent successfully",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailed(String message) {
                    dialog.dismiss();
                    Toast.makeText(context, message,
                            Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
