package com.mk_tech.delivery.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.OrdersModel;
import com.mk_tech.delivery.ui.activity.Activity_orders_details;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder> {

    private final Context context;
    List<OrdersModel.DataModel> dataList;


    public OrderAdapter(Context c, List<OrdersModel.DataModel> dataList) {
        this.context = c;
        this.dataList = dataList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        holder.tvDeliveryStatus.setText(dataList.get(position).getStatus());
        holder.tvPaymentMethod.setText(dataList.get(position).getPayment_method());
        holder.tvOrderId.setText("#" + dataList.get(position).getId());
        holder.tvTotal.setText(dataList.get(position).getTotal() + " " + dataList.get(position).getCurrency());
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_orders_details.class).putExtra("id", dataList.get(holder.getAdapterPosition()).getId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        LinearLayout llContainer;
        TextView tvDeliveryStatus, tvPaymentMethod, tvOrderId, tvTotal;

        public viewHolder(View itemView) {
            super(itemView);
            tvDeliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            llContainer = itemView.findViewById(R.id.llContainer);

        }
    }

}
