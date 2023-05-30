package com.mk_tech.delivery.ui.activity.checkout;

import static com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_address.addressModel;
import static com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_address.deliveryPrice;

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
import com.mk_tech.delivery.adapter.ProductSummaryAdapter;
import com.mk_tech.delivery.model.CartModel;
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

public class Activity_checkout_order_summary extends AppCompatActivity {

    ImageView ivBack;
    TextView tvSubmit, tvSubTotal, tvDeliveryCharges, tvDiscount, tvTotal, tvAddressInfo, tvAddressDetails;
    RecyclerView RV;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_checkout_summery);
        UtilsClass.init_AlertDialog(this);
        ivBack = findViewById(R.id.ivBack);
        RV = findViewById(R.id.RV);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvDeliveryCharges = findViewById(R.id.tvDeliveryCharges);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        tvAddressInfo = findViewById(R.id.tvAddressInfo);
        tvAddressDetails = findViewById(R.id.tvAddressDetails);
        tvSubmit = findViewById(R.id.tvSubmit);

        tvAddressInfo.setText(addressModel.getCity() + " " + addressModel.getArea());
        tvAddressDetails.setText(getString(R.string.street) + " " + addressModel.getStreet() + " " + getString(R.string.block) + " " + addressModel.getBlock() + "\n" + addressModel.getPhone());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Activity_checkout_order_summary.this, Activity_checkout_address.class));

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Activity_checkout_order_summary.this, Activity_checkout_payment.class));
            }
        });

        getCart();
    }

    public void getCart() {
        UtilsClass.init_Progress_Dialog(Activity_checkout_order_summary.this);
        UtilsClass.init_AlertDialog(Activity_checkout_order_summary.this);
        UtilsClass.Show_Progress_Dialog();
        try {
            if (NetworkUtil.isNetworkAvaliable(getApplicationContext())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.getCarts()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CartModel>() {
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
                            public void onNext(CartModel value) {
                                if (value.getSuccess()) {

                                    double discount = 0.0;
                                    double subTotal = 0.0;
                                    double total = 0.0;
                                    List<CartModel.CartProduct> productList = new ArrayList<>();
                                    for (int i = 0; i < value.getData().size(); i++) {
                                        double totla= Double.parseDouble(value.getData().get(i).getTotal().replace(",",""));
                                        subTotal = subTotal +totla ;
                                        for (int x = 0; x < value.getData().get(i).getProducts_count(); x++) {
                                            productList.add(value.getData().get(i).getProducts().get(x));
                                        }
                                    }
                                    total = subTotal - discount + deliveryPrice;
                                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                                    DecimalFormat formatter = new DecimalFormat("#,###.000",symbols );

                                    tvSubTotal.setText(formatter.format(subTotal)+ " " + getString(R.string.currency));
                                    tvDiscount.setText(formatter.format(discount )+ " " + getString(R.string.currency));
                                    tvTotal.setText(formatter.format(total) + " " + getString(R.string.currency));
                                    tvDeliveryCharges.setText(formatter.format(deliveryPrice) + " " + getString(R.string.currency));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_checkout_order_summary.this, Activity_checkout_address.class));

    }
}