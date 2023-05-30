package com.mk_tech.delivery.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.Product;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    private final Context context;
    List<Product> dataList;


    public NotificationAdapter(Context c, List<Product> dataList) {
        this.context = c;
        this.dataList = dataList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        //holder.tvTitle.setText(dataList.get(position).getDelivery_status());
        // holder.tvDate.setText(dataList.get(position).getCreated_at_ago());
        //holder.tvTitle.setText("#" + dataList.get(position).getId());


    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvBody, tvTitle;

        public viewHolder(View itemView) {
            super(itemView);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvOrderId);

        }
    }

}
