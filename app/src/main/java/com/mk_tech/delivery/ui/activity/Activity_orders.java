package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.OrderAdapter;
import com.mk_tech.delivery.model.OrdersModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_orders extends AppCompatActivity {
    ImageView ivBack;
    ScrollView containerNoData;
    RecyclerView Rv;
    Button btnGoToHome;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        UtilsClass.init_AlertDialog(this);
        ivBack = findViewById(R.id.ivBack);
        containerNoData = findViewById(R.id.containerNoData);
        Rv = findViewById(R.id.Rv);
        btnGoToHome = findViewById(R.id.btnGoToHome);

        btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_orders.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
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
                Subscription mSubscription = apiService.getOrders()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<OrdersModel>() {
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
                            public void onNext(OrdersModel value) {
                                if (value.getSuccess()) {
                                    if (value.getData().size() > 0) {
                                        containerNoData.setVisibility(View.GONE);
                                        Rv.setVisibility(View.VISIBLE);
                                        OrderAdapter orderAdapter = new OrderAdapter(getApplicationContext(), value.getData());
                                        RecyclerView.LayoutManager mLayoutHomeManager3 = new LinearLayoutManager(getApplicationContext());
                                        Rv.setLayoutManager(mLayoutHomeManager3);
                                        ((LinearLayoutManager) mLayoutHomeManager3).setOrientation(RecyclerView.VERTICAL);
                                        Rv.setAdapter(orderAdapter);
                                    } else {
                                        containerNoData.setVisibility(View.VISIBLE);
                                        Rv.setVisibility(View.GONE);

                                    }


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
        // getMyOrders();
    }
}
