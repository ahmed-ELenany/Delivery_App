package com.mk_tech.delivery.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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

        if(dataList.get(position).getStatus().equals("Pending")){
            holder.tvDeliveryStatus.setTextColor(Color.parseColor("#FECD1B"));
            holder.tvDeliveryStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pending_sm,0,0,0);
            holder.ivOrderStatus.setImageResource(R.drawable.ic_pending_lg);
        }else  if(dataList.get(position).getStatus().equals("Completed")){
            holder.tvDeliveryStatus.setTextColor(Color.parseColor("#00A083"));
            holder.tvDeliveryStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_complet_sm,0,0,0);
            holder.ivOrderStatus.setImageResource(R.drawable.ic_complet_lg);

        }else  if(dataList.get(position).getStatus().equals("Cancelled")){
            holder.tvDeliveryStatus.setTextColor(Color.parseColor("#C41414"));
            holder.tvDeliveryStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_sm,0,0,0);
            holder.ivOrderStatus.setImageResource(R.drawable.ic_cancel_lg);
        }

        //holder.tvPaymentMethod.setText(dataList.get(position).getPayment_method());
       // holder.tvOrderId.setText("#" + dataList.get(position).getId());
        //holder.tvTotal.setText(dataList.get(position).getTotal() + " " + dataList.get(position).getCurrency());
        holder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // context.startActivity(new Intent(context, Activity_orders_details.class).putExtra("id", dataList.get(holder.getAdapterPosition()).getId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        CardView cardContainer;
        TextView tvDeliveryStatus, tvPaymentMethod, tvOrderId, tvTotal;
ImageView ivOrderStatus;
        public viewHolder(View itemView) {
            super(itemView);
            tvDeliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            ivOrderStatus = itemView.findViewById(R.id.ivOrderStatus);
            cardContainer = itemView.findViewById(R.id.cardContainer);

        }
    }

}
