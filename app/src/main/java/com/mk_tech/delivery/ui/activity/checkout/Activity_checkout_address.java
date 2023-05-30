package com.mk_tech.delivery.ui.activity.checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.Address;
import com.mk_tech.delivery.model.DeliveryPriceModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;
import com.mk_tech.delivery.ui.activity.Activity_address;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Activity_checkout_address extends AppCompatActivity {

    public static Address addressModel;
    public static double deliveryPrice = 0.0;
    ImageView ivBack;
    TextView tvSubmit, tvAddressInfo, tvAddressDetails, tvAddAddress, tvDeliveryCharge;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_checkout_address);
        UtilsClass.init_AlertDialog(this);
        ivBack = findViewById(R.id.ivBack);
        tvAddAddress = findViewById(R.id.tvAddAddress);

        tvAddressInfo = findViewById(R.id.tvAddressInfo);
        tvAddressDetails = findViewById(R.id.tvAddressDetails);
        tvSubmit = findViewById(R.id.tvSubmit);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressModel == null) {
                    UtilsClass.init_AlertDialog(Activity_checkout_address.this);
                    UtilsClass.Show_AlertDialog(getString(R.string.delivery_address), getString(R.string.Please_add_delivery_ddress));
                } else {
                    finish();
                    startActivity(new Intent(Activity_checkout_address.this, Activity_checkout_order_summary.class));

                }
            }
        });

        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_checkout_address.this, Activity_address.class).putExtra("requestType", "selectAddress"));
            }
        });


    }


    public void getDeliveryPrice(int addressId) {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.getDeliveryPrice(addressId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<DeliveryPriceModel>() {
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
                            public void onNext(DeliveryPriceModel value) {
                                if (value.getSuccess()) {
                                    deliveryPrice = Double.parseDouble(value.getData().getTotalShipping());
                                    tvDeliveryCharge.setText(deliveryPrice + " " + getString(R.string.currency));

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
        if (addressModel != null) {
            tvAddressInfo.setText(addressModel.getCity() + " " + addressModel.getArea());
            tvAddressDetails.setText(getString(R.string.street) + " " + addressModel.getStreet() + " " + getString(R.string.block) + " " + addressModel.getBlock() + "\n" + addressModel.getPhone());
            getDeliveryPrice(addressModel.getId());
        }
    }
}