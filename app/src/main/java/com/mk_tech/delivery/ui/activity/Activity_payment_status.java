package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.ui.activity.checkout.Activity_checkout_payment;


public class Activity_payment_status extends AppCompatActivity {

    TextView tvTryAgain, tvMyOrders;
    ScrollView scrollFail, scrollSuc;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        tvTryAgain = findViewById(R.id.tvTryAgain);
        tvMyOrders = findViewById(R.id.tvMyOrders);
        scrollFail = findViewById(R.id.scrollFail);
        scrollSuc = findViewById(R.id.scrollSuc);


        Intent i = getIntent();
        String status = i.getStringExtra("status");

        if (status.equals("suc")) {
            scrollFail.setVisibility(View.GONE);
            scrollSuc.setVisibility(View.VISIBLE);
        } else {
            scrollSuc.setVisibility(View.GONE);
            scrollFail.setVisibility(View.VISIBLE);
        }

        tvMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_payment_status.this, Activity_orders.class));
                finish();
            }
        });
        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_payment_status.this, Activity_checkout_payment.class));
                finish();
            }
        });

    }


}
