package com.mk_tech.delivery.adapter;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.Address;
import com.mk_tech.delivery.ui.activity.Activity_address;
import com.mk_tech.delivery.ui.activity.Activity_address_add;
import com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_address;

import java.util.List;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.viewHolder> {

    private final Context context;
    List<Address> dataList;
    String requestType;

    public MyAddressAdapter(Context c, List<Address> dataList, String requestType) {
        this.context = c;
        this.dataList = dataList;
        this.requestType = requestType;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_address_row, parent, false);

        return new viewHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        if (requestType != null && requestType.equals("selectAddress")) {
            holder.radioBtn.setVisibility(View.VISIBLE);
            holder.radioBtn.setChecked(Activity_checkout_address.addressModel != null && Activity_checkout_address.addressModel.getId() == dataList.get(position).getId());
        }

        holder.tvName.setText(dataList.get(position).getName());
        holder.tvAddressDetails.setText(dataList.get(position).getCity() + " " + dataList.get(position).getArea());
        holder.tvStreet.setText(context.getString(R.string.street) + " " + dataList.get(position).getStreet() + " " + context.getString(R.string.block) + " " + dataList.get(position).getStreet());
        holder.tvMobile.setText(dataList.get(position).getPhone());


        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestType != null && requestType.equals("selectAddress")) {
                    Activity_checkout_address.addressModel = dataList.get(holder.getAbsoluteAdapterPosition());
                    Activity_checkout_address.addressModel.setIsGlobal(0);
                    notifyDataSetChanged();

                } else {
                    Activity_address_add.addressModel = dataList.get(holder.getAbsoluteAdapterPosition());
                    context.startActivity(new Intent(context, Activity_address_add.class)
                            .putExtra("requestType", "update").addFlags(FLAG_ACTIVITY_NEW_TASK));
                    Activity_address.context.finish();
                }

            }
        });


    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddressDetails, tvStreet, tvMobile;
        RadioButton radioBtn;
        LinearLayout llContainer;

        public viewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddressDetails = itemView.findViewById(R.id.tvAddressDetails);
            tvStreet = itemView.findViewById(R.id.tvStreet);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            radioBtn = itemView.findViewById(R.id.radioBtn);
            llContainer = itemView.findViewById(R.id.llContainer);

        }
    }

}
