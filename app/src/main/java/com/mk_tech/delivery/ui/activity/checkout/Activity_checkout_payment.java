package com.mk_tech.delivery.ui.activity.checkout;

import static com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_address.addressModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.mk_tech.delivery.adapter.PaymentMethodAdapter;
import com.mk_tech.delivery.model.PaymentMethodModel;
import com.mk_tech.delivery.model.PaymentModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;
import com.mk_tech.delivery.ui.activity.Activity_address;
import com.mk_tech.delivery.ui.activity.Activity_payment_status;
import com.mk_tech.delivery.ui.activity.Activity_webView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Activity_checkout_payment extends AppCompatActivity {

    public static String payment_method_slug = "";
    ImageView ivBack;
    TextView tvSubmit;
    RecyclerView Rv;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_checkout_payment);
        UtilsClass.init_AlertDialog(this);
        ivBack = findViewById(R.id.ivBack);
        tvSubmit = findViewById(R.id.tvSubmit);
        Rv = findViewById(R.id.Rv);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Activity_checkout_payment.this, Activity_checkout_order_summary.class));

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!payment_method_slug.isEmpty()) {

                    payment();


                } else {
                    UtilsClass.Show_AlertDialog(getString(R.string.payment_method), getString(R.string.please_select_payment_method));
                }
            }
        });


    }

    public void getPaymentMethods() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.getPaymentMethods()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<PaymentMethodModel>() {
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
                            public void onNext(PaymentMethodModel value) {
                                if (value.getSuccess()) {
                                    PaymentMethodAdapter paymentMethodAdapter = new PaymentMethodAdapter(getApplicationContext(), value.getData());
                                    RecyclerView.LayoutManager mLayoutHomeManager3 = new LinearLayoutManager(getApplicationContext());
                                    Rv.setLayoutManager(mLayoutHomeManager3);
                                    ((LinearLayoutManager) mLayoutHomeManager3).setOrientation(RecyclerView.VERTICAL);
                                    Rv.setAdapter(paymentMethodAdapter);


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

    void payment() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.getPayment(addressModel.getId(), payment_method_slug)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<PaymentModel>() {
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
                            public void onNext(PaymentModel value) {
                                if (value.getSuccess()) {
                                    if (value.getData() == null) {
                                        startActivity(new Intent(Activity_checkout_payment.this, Activity_payment_status.class).putExtra("status", "suc"));
                                        finish();
                                    } else {
                                        startActivity(new Intent(Activity_checkout_payment.this, Activity_webView.class).putExtra("url", value.getData()));
                                        finish();
                                        Activity_address.context.finish();
                                    }

                                } else {
                                    UtilsClass.Show_AlertDialog(getString(R.string.error), getString(R.string.ayou_must_determine_your_location_on_the_map_according_to_the_area_that_you_have_chosen_in_the_address));
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
        getPaymentMethods();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_checkout_payment.this, Activity_checkout_order_summary.class));

    }
}