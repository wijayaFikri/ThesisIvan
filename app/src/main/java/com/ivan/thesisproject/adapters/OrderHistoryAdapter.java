package com.ivan.thesisproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.ivan.thesisproject.R;
import com.ivan.thesisproject.activities.OrderHistoryDetailActivity;
import com.ivan.thesisproject.models.Order;

import java.util.List;

public class OrderHistoryAdapter extends ArrayAdapter<Order> {
    private int resourceLayout;
    private Context mContext;

    public OrderHistoryAdapter(@NonNull Context context, int resource, List<Order> listItem) {
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

        final Order order = getItem(position);

        TextView orderIdTv = v.findViewById(R.id.historyOrder_order_id);
        TextView totalPriceTv = v.findViewById(R.id.historyOrder_total_price);
        TextView totalItemTv = v.findViewById(R.id.historyOrder_total_item);
        TextView dateTv = v.findViewById(R.id.historyOrder_order_date);
        TextView statusTv = v.findViewById(R.id.historyOrder_status);

        assert  order != null;
        String totalItemLabel = String.valueOf(order.getProductList().size()) + " items";
        String totalPriceLabel = "Rp " + String.valueOf(order.getTotalPrice());
        String orderDateLabel = order.getOrderDate();

        CardView cv = v.findViewById(R.id.historyOrder_order_card);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderHistoryDetailActivity.class);
                intent.putExtra("order",new Gson().toJson(order));
                mContext.startActivity(intent);
            }
        });

        orderIdTv.setText(order.getId());
        totalPriceTv.setText(totalPriceLabel);
        totalItemTv.setText(totalItemLabel);
        dateTv.setText(orderDateLabel);
        statusTv.setText(order.getStatus());

        return v;
    }
}
