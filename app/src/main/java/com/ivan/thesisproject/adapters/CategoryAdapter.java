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

import com.ivan.thesisproject.R;
import com.ivan.thesisproject.activities.CategoryActivity;
import com.ivan.thesisproject.activities.ShoppingActivity;
import com.ivan.thesisproject.models.Order;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<String> {
    private int resourceLayout;
    private Context mContext;

    public CategoryAdapter(@NonNull Context context, int resource, List<String> listItem) {
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

        final String item = getItem(position);

        TextView categoryTv = v.findViewById(R.id.category_item_textView);
        categoryTv.setText(item);

        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CategoryActivity) mContext).goShopFromCategory(item);
            }
        });

        return v;
    }
}
