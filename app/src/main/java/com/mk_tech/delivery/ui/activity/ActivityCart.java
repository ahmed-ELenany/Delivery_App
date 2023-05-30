package com.mk_tech.delivery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mk_tech.delivery.adapter.CartProductAdapter;
import com.mk_tech.delivery.model.CartModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;
import com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_address;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityCart extends AppCompatActivity {

    public static ActivityCart context;
    LinearLayout llContainerNoData, llContainerDataFound;
    RecyclerView RV;
    TextView tvTotal, tvCheckout, tvDiscount, tvSubTotal;
    ImageView ivBack;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);
        context = this;
        llContainerNoData = findViewById(R.id.llContainerNoData);
        llContainerDataFound = findViewById(R.id.llContainerDataFound);
        RV = findViewById(R.id.RV);
        tvCheckout = findViewById(R.id.tvCheckout);
        tvTotal = findViewById(R.id.tvTotal);
        ivBack = findViewById(R.id.ivBack);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityCart.this, Activity_checkout_address.class));
            }
        });


    }

    public void getCart() {
        UtilsClass.init_Progress_Dialog(ActivityCart.this);
        UtilsClass.init_AlertDialog(ActivityCart.this);
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
                                llContainerDataFound.setVisibility(View.GONE);
                                llContainerNoData.setVisibility(View.VISIBLE);
                                //startActivity(new Intent(ActivityCart.this,LoginActivity.class));
                            }

                            @Override
                            public void onNext(CartModel value) {
                               if (value.getSuccess()) {
                                    if (value.getData().size() > 0) {
                                        llContainerDataFound.setVisibility(View.VISIBLE);
                                        llContainerNoData.setVisibility(View.GONE);

                                    } else {
                                        llContainerDataFound.setVisibility(View.GONE);
                                        llContainerNoData.setVisibility(View.VISIBLE);
                                    }



                                    List<CartModel.CartProduct> productList = new ArrayList<>();
                                    for (int i = 0; i < value.getData().size(); i++) {
                                          for (int x = 0; x < value.getData().get(i).getProducts_count(); x++) {
                                            productList.add(value.getData().get(i).getProducts().get(x));
                                        }
                                    }

                                  // tvSubTotal.setText(value.getData().get(0).getTotal()+ " " + getString(R.string.currency));
                                  // tvDiscount.setText(value.getData().get(0).getDiscount()+ " " + getString(R.string.currency));

                                   tvSubTotal.setVisibility(View.GONE);
                                   tvDiscount.setVisibility(View.GONE);
                                   tvTotal.setText(value.getMessage() + " " + getString(R.string.currency));

                                  CartProductAdapter cartAdapter = new CartProductAdapter(getApplicationContext(), productList);
                                    RecyclerView.LayoutManager mLayoutHomeManager = new LinearLayoutManager(getApplicationContext());
                                    RV.setLayoutManager(mLayoutHomeManager);
                                    ((LinearLayoutManager) mLayoutHomeManager).setOrientation(RecyclerView.VERTICAL);
                                    RV.setAdapter(cartAdapter);

                                    SharedPref.Save_area_city(getApplicationContext(), "CART_PRODUCTS_NUM", productList.size() + "");
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
        getCart();
    }
}