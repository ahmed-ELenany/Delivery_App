package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.MyAddressAdapter;
import com.mk_tech.delivery.model.AddressListModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;
import com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_address;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_address extends AppCompatActivity {
    public static Activity_address context;
    ImageView ivBack;
    Button btnAddNewAddress, btnConfirmSelectedAddress;
    ScrollView containerNoData;
    RecyclerView Rv, RvAddressGlobal;
    String requestType;
    TextView tvMyAddress;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_address);
        context = this;
        UtilsClass.init_AlertDialog(this);
        Intent i = getIntent();
        requestType = i.getStringExtra("requestType");
        ivBack = findViewById(R.id.ivBack);
        containerNoData = findViewById(R.id.containerNoData);
        Rv = findViewById(R.id.Rv);
        btnConfirmSelectedAddress = findViewById(R.id.btnConfirmSelectedAddress);
        btnAddNewAddress = findViewById(R.id.btnAddNewAddress);
        RvAddressGlobal = findViewById(R.id.RvAddressGlobal);
        tvMyAddress = findViewById(R.id.tvMyAddress);
        if (requestType != null && requestType.equals("selectAddress")) {
            btnConfirmSelectedAddress.setVisibility(View.VISIBLE);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Rv.getVisibility() == View.VISIBLE) {
                    Rv.setVisibility(View.GONE);
                } else {
                    Rv.setVisibility(View.VISIBLE);
                }
            }
        });
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Activity_address.this, Activity_address_add.class));

            }
        });

        btnConfirmSelectedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Activity_checkout_address.addressModel != null) {
                    finish();
                } else {
                    UtilsClass.Show_AlertDialog(getString(R.string.delivery_address), getString(R.string.Please_select_delivery_address));

                }
            }
        });


    }

    public void getMyAddress() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.getAddresses()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddressListModel>() {
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
                            public void onNext(AddressListModel value) {
                                if (value.getSuccess()) {
                                    if (value.getData().size() > 0) {
                                        containerNoData.setVisibility(View.GONE);
                                        MyAddressAdapter myAddressAdapter = new MyAddressAdapter(getApplicationContext(), value.getData(), requestType);
                                        RecyclerView.LayoutManager mLayoutHomeManager3 = new LinearLayoutManager(getApplicationContext());
                                        Rv.setLayoutManager(mLayoutHomeManager3);
                                        ((LinearLayoutManager) mLayoutHomeManager3).setOrientation(RecyclerView.VERTICAL);
                                        Rv.setAdapter(myAddressAdapter);
                                    } else {
                                        containerNoData.setVisibility(View.VISIBLE);
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
        getMyAddress();
    }
}
