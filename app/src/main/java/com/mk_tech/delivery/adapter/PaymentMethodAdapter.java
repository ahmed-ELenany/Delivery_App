package com.mk_tech.delivery.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.PaymentMethodModel;
import com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_payment;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.viewHolder> {

    private final Context context;
    List<PaymentMethodModel.DataModel> dataList;

    public PaymentMethodAdapter(Context c, List<PaymentMethodModel.DataModel> dataList) {
        this.context = c;
        this.dataList = dataList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_method_row, parent, false);

        return new viewHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        holder.radioBtn.setChecked(!Activity_checkout_payment.payment_method_slug.isEmpty() && Activity_checkout_payment.payment_method_slug == dataList.get(position).getSlug());
        holder.tvName.setText(dataList.get(position).getName());

        Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .placeholder(R.drawable.picture)
                        .error(R.drawable.picture)
        ).load(dataList.get(position).getImage().getUrl()).into(holder.ivLogo);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_checkout_payment.payment_method_slug = dataList.get(holder.getAbsoluteAdapterPosition()).getSlug();
                notifyDataSetChanged();


            }
        });


    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivLogo;
        RadioButton radioBtn;
        LinearLayout llContainer;

        public viewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            radioBtn = itemView.findViewById(R.id.radioBtn);
            llContainer = itemView.findViewById(R.id.llContainer);

        }
    }

}
