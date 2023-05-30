package com.mk_tech.delivery.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.CartModel;

import java.util.List;

public class ProductSummaryAdapter extends RecyclerView.Adapter<ProductSummaryAdapter.viewHolder> {

    private final Context context;
    List<CartModel.CartProduct> dataList;

    public ProductSummaryAdapter(Context c, List<CartModel.CartProduct> dataList) {
        this.context = c;
        this.dataList = dataList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summary_product_row, parent, false);


        return new viewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        holder.tvProductName.setText(dataList.get(position).getName());
        holder.tvProductDescription.setText(dataList.get(position).getDescription());
        holder.tvProductPrice.setText(dataList.get(position).getPrice() + " " + context.getString(R.string.currency));
        holder.tvProductTotalPrice.setText(dataList.get(position).getTotal() + " " + context.getString(R.string.currency));
        holder.tvQuantity.setText(dataList.get(position).getQuantity() + "X");


    }


    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductTotalPrice, tvProductDescription, tvQuantity;
        LinearLayout llContainer;

        public viewHolder(View itemView) {
            super(itemView);
            llContainer = itemView.findViewById(R.id.llContainer);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductTotalPrice = itemView.findViewById(R.id.tvProductTotalPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
        }
    }

}
