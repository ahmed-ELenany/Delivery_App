package com.mk_tech.delivery.adapter;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.CartModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;
import com.mk_tech.delivery.ui.activity.ActivityCart;
import com.mk_tech.delivery.ui.activity.Activity_product_details;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.viewHolder> {

    private final Context context;
    List<CartModel.CartProduct> dataList;

    public CartProductAdapter(Context c, List<CartModel.CartProduct> dataList) {
        this.context = c;
        this.dataList = dataList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_product_row, parent, false);


        return new viewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .placeholder(R.drawable.picture)
                        .error(R.drawable.picture)
        ).load(dataList.get(position).getImage()).into(holder.ivProduct);


        holder.tvProductName.setText(dataList.get(position).getName());
        holder.tvProductDescription.setText(dataList.get(position).getDescription());
        holder.tvProductPrice.setText(dataList.get(position).getPrice() + " " + dataList.get(position).getCurrency());
        holder.tvProductTotalPrice.setText(dataList.get(position).getTotal() + " " + dataList.get(position).getCurrency());
        holder.tvQuantity.setText(dataList.get(position).getQuantity() + "");


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(dataList.get(holder.getAdapterPosition()).getId());

            }
        });
        holder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_product_details.class)
                        .putExtra("id", dataList.get(holder.getAdapterPosition()).getProduct_id()).addFlags(FLAG_ACTIVITY_NEW_TASK));

            }
        });

        holder.tvQuantityIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(holder.getAdapterPosition()).getStock() > dataList.get(holder.getAdapterPosition()).getQuantity()) {
                    cartsUpdate(dataList.get(holder.getAdapterPosition()).getId(), dataList.get(holder.getAdapterPosition()).getQuantity() + 1);

                }
            }
        });

        holder.tvQuantityDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(holder.getAdapterPosition()).getQuantity() > 1) {
                    cartsUpdate(dataList.get(holder.getAdapterPosition()).getId(), dataList.get(holder.getAdapterPosition()).getQuantity() - 1);

                }
            }
        });


    }

    public void delete(int id) {
        UtilsClass.init_Progress_Dialog(context);
        UtilsClass.Show_Progress_Dialog();
        UtilsClass.init_AlertDialog(context);

        try {
            if (NetworkUtil.isNetworkAvaliable(context)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(context), SharedPref.Get_lan(context));
                Subscription mSubscription = apiService.CartProductDelete(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CartModel>() {
                            @Override
                            public void onCompleted() {


                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onNext(CartModel value) {
                                if (value.getSuccess()) {
                                    UtilsClass.Dismiss_Progress_Dialog();
                                    ActivityCart.context.getCart();
                                } else {
                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());

                                }

                            }
                        });


            } else {
                UtilsClass.Dismiss_Progress_Dialog();
                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UtilsClass.Dismiss_Progress_Dialog();
            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");


        }

    }

    public void cartsUpdate(int id, int quantity) {
        UtilsClass.init_Progress_Dialog(context);
        UtilsClass.init_AlertDialog(context);
        UtilsClass.Show_Progress_Dialog();
        try {
            if (NetworkUtil.isNetworkAvaliable(context)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(context), SharedPref.Get_lan(context));
                Subscription mSubscription = apiService.cartsUpdate(id, quantity)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CartModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onNext(CartModel value) {

                                UtilsClass.Dismiss_Progress_Dialog();
                                if (value.getSuccess()) {
                                    ActivityCart.context.getCart();
                                } else {

                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());

                                }

                            }
                        });


            } else {
                UtilsClass.Dismiss_Progress_Dialog();
                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UtilsClass.Dismiss_Progress_Dialog();
            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");


        }

    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductTotalPrice, tvProductDescription, tvQuantity, tvQuantityIncrement, tvQuantityDecrement;
        ImageView ivProduct, ivDelete;
        LinearLayout llContainer;

        public viewHolder(View itemView) {
            super(itemView);
            tvQuantityIncrement = itemView.findViewById(R.id.tvQuantityIncrement);
            tvQuantityDecrement = itemView.findViewById(R.id.tvQuantityDecrement);
            llContainer = itemView.findViewById(R.id.llContainer);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductTotalPrice = itemView.findViewById(R.id.tvProductTotalPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

}
