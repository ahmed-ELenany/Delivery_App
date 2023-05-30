package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.ProductSummaryAdapter;
import com.mk_tech.delivery.model.CartModel;
import com.mk_tech.delivery.model.OrderDetailsModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_orders_details extends AppCompatActivity {
    ImageView ivBack;
    TextView tvDeliveryStatus, tvSubTotal, tvDeliveryCharges, tvDiscount, tvTotal, tvAddressInfo, tvAddressDetails;
    RecyclerView RV;
    int orderId;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        UtilsClass.init_AlertDialog(this);
        Intent i = getIntent();
        orderId = i.getIntExtra("id", 0);
        ivBack = findViewById(R.id.ivBack);
        tvDeliveryStatus = findViewById(R.id.tvDeliveryStatus);
        RV = findViewById(R.id.RV);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvDeliveryCharges = findViewById(R.id.tvDeliveryCharges);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        tvAddressInfo = findViewById(R.id.tvAddressInfo);
        tvAddressDetails = findViewById(R.id.tvAddressDetails);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getMyOrders();

    }

    public void getMyOrders() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.getOrderDetails(orderId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<OrderDetailsModel>() {
                            @Override
                            public void onCompleted() {
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onNext(OrderDetailsModel value) {
                                if (value.getSuccess()) {
                                    tvDeliveryStatus.setText(value.getData().getStatus());
                                    List<CartModel.CartProduct> productList = new ArrayList<>();
                                    for (int i = 0; i < value.getData().getCompanies().size(); i++) {
                                        for (int x = 0; x < value.getData().getCompanies().get(i).getProducts().size(); x++) {
                                            CartModel.CartProduct cartProduct = new CartModel.CartProduct();
                                            OrderDetailsModel.OrderSummaryProduct orderSummaryProduct = value.getData().getCompanies().get(i).getProducts().get(x);
                                            cartProduct.setName(orderSummaryProduct.getName());
                                            cartProduct.setPrice(orderSummaryProduct.getPrice());
                                            cartProduct.setQuantity(orderSummaryProduct.getQuantity());
                                            cartProduct.setTotal(orderSummaryProduct.getTotal());
                                            productList.add(cartProduct);
                                        }
                                    }
                                    double subTotal = value.getData().getTotal() - value.getData().getDiscount() - value.getData().getDelivery();
                                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                                    DecimalFormat formatter = new DecimalFormat("#,###.000",symbols );

                                    tvSubTotal.setText(formatter.format(subTotal)+ " " + getString(R.string.currency));

                                    tvDiscount.setText(value.getData().getDiscount() + " " + getString(R.string.currency));
                                    tvTotal.setText(value.getData().getTotal() + " " + getString(R.string.currency));
                                    tvDeliveryCharges.setText(value.getData().getDelivery() + " " + getString(R.string.currency));
                                    ProductSummaryAdapter productSummaryAdapter = new ProductSummaryAdapter(getApplicationContext(), productList);
                                    RecyclerView.LayoutManager mLayoutHomeManager = new LinearLayoutManager(getApplicationContext());
                                    RV.setLayoutManager(mLayoutHomeManager);
                                    ((LinearLayoutManager) mLayoutHomeManager).setOrientation(RecyclerView.VERTICAL);
                                    RV.setAdapter(productSummaryAdapter);

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


    @Override
    protected void onResume() {
        super.onResume();

    }
}
