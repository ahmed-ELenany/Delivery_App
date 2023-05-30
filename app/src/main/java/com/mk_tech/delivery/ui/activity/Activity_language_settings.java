package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.SplashScreenActivity;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.ui.fragment.bottomMenu.HomeFragment;


public class Activity_language_settings extends AppCompatActivity {
    TextView tvEnglish, tvArabic;
    ImageView ivBack;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvArabic = findViewById(R.id.tvArabic);
        ivBack = findViewById(R.id.ivBack);
        HomeFragment.homeModel = null;

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tvArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.Save_lan(getApplication(), "ar");
                setLanguage(1);
            }
        });

        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.Save_lan(getApplication(), "en");
                setLanguage(1);

            }
        });
        setLanguage(0);
    }

    public void setLanguage(int reload) {
        if (SharedPref.Get_lan(getApplicationContext()).equals("ar")) {
            tvArabic.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
            tvEnglish.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            SplashScreenActivity.setLanLocal(getApplication(), "ar");

        } else {
            tvEnglish.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
            tvArabic.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            SplashScreenActivity.setLanLocal(getApplication(), "en");
        }
        SplashScreenActivity.language = SharedPref.Get_lan(getApplication());
        MainActivity.context.recreate();
        if (reload == 1) {
            startActivity(new Intent(this, Activity_language_settings.class));
            finish();
        }
    }

}
