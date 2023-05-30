package com.mk_tech.delivery.adapter;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.Product;
import com.mk_tech.delivery.ui.activity.Activity_product_details;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewHolder> {

    private final Context context;
    List<Product> dataList;
    int numOfGrid;
    String layoutType;
    boolean isOffer;
    public ProductAdapter(Context c, List<Product> dataList, String layoutType,boolean isOffer) {
        this.context = c;
        this.dataList = dataList;
        this.layoutType = layoutType;
        this.isOffer = isOffer;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutType.equals("large") ? R.layout.product_large_row : R.layout.product_row, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .placeholder(R.drawable.picture)
                        .error(R.drawable.picture)
        ).load(dataList.get(position).getImage()).into(holder.iv_banner);

        holder.tv_title.setText(dataList.get(position).getName());
        holder.tv_Description.setText(dataList.get(position).getDescription());
        holder.tv_price.setText(dataList.get(position).getNew_price() + " " + context.getString(R.string.currency));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_product_details.class).putExtra("id", dataList.get(holder.getAdapterPosition()).getId()).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

        if (dataList.get(position).getDiscount_percentage() <= 0) {
            holder.tvDiscount.setVisibility(View.INVISIBLE);
            holder.ivOffer.setVisibility(View.INVISIBLE);
        } else {
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(dataList.get(position).getDiscount_percentage() + "%");
        }
        if (dataList.get(position).getEndAt() != null && !dataList.get(position).getEndAt().isEmpty() && isOffer) {

            Date dateEndFormat = null;
            try {
                dateEndFormat = new SimpleDateFormat("yyyy-MM-dd").parse(dataList.get(position).getEndAt());

            Long timeEnd = dateEndFormat.getTime();
            Long TimeNow = new Date().getTime();
            long diff = timeEnd - TimeNow;
            long diffSec = (diff / 1000)%60;
            long dMins = (diff / (60 * 1000))%60;
            long dHour = (diff / (60 * 60 * 1000))%24;
            long dDay = diff / (24 * 60 * 60 * 1000);
            holder.tvDay.setText(dDay +"");
            holder.tvHour.setText(dHour +"");
            holder.tvMinutes.setText(dMins +"");
            holder.tvSecond.setText(diffSec +"");
            } catch (ParseException e) {
                e.printStackTrace();
            }
//              try {
//                Date dateEndFormat = new SimpleDateFormat("yyyy-MM-dd").parse(dataList.get(position).getEndAt());
//                Long timeEnd = dateEndFormat.getTime();
//                Long TimeNow = new Date().getTime();
//
//                long diff = timeEnd - TimeNow;
//                //equations
//                new CountDownTimer(diff, 60000) {
//                    public void onTick(long millisUntilFinished) {
//                        long diffSec = (diff / 1000)%60;
//                        long dMins = (diff / (60 * 1000))%60;
//                        long dHour = (diff / (60 * 60 * 1000))%24;
//                        long dDay = diff / (24 * 60 * 60 * 1000);
//                        holder.tvDay.setText(dDay +"");
//                        holder.tvHour.setText(dHour +"");
//                        holder.tvMinutes.setText(dMins +"");
//                        holder.tvSecond.setText(diffSec +"");
//                        notifyDataSetChanged();
//
//                      }
//
//                    public void onFinish() {
//                     }
//                }.start();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

        } else {
            holder.llTimeCounter.setVisibility(View.GONE);
        }



    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tvDiscount, tv_Description, tv_price, tvMinutes,tvDay,tvHour,tvSecond;
        ImageView iv_banner, ivOffer;
        RelativeLayout relativeContainer;
        LinearLayout llTimeCounter;
        View itemView;
        public viewHolder(View itemView) {
            super(itemView);
            llTimeCounter = itemView.findViewById(R.id.llTimeCounter);
            tv_title = itemView.findViewById(R.id.tv_title);
            tvSecond = itemView.findViewById(R.id.tvSecond);
            tvMinutes = itemView.findViewById(R.id.tvMinutes);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvHour = itemView.findViewById(R.id.tvHour);
            iv_banner = itemView.findViewById(R.id.iv_banner);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tv_Description = itemView.findViewById(R.id.tv_Description);
            tv_price = itemView.findViewById(R.id.tv_price);
            relativeContainer = itemView.findViewById(R.id.relativeContainer);
            ivOffer = itemView.findViewById(R.id.ivOffer);
            this.itemView = itemView;

        }
    }

}
