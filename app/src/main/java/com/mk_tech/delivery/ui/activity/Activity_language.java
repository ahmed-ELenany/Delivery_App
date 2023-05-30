package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.SplashScreenActivity;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.ui.fragment.bottomMenu.HomeFragment;


public class Activity_language extends AppCompatActivity {
    Button btn_en;
    TextView tvAr;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        tvAr = findViewById(R.id.tvAr);
        btn_en = findViewById(R.id.btn_en);

        HomeFragment.homeModel = null;
        tvAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.Save_lan(getApplication(), "ar");
                SplashScreenActivity.setLanLocal(getApplication(), "ar");
                takeAction();
            }
        });

        btn_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.Save_lan(getApplication(), "en");
                SplashScreenActivity.setLanLocal(getApplication(), "en");
                takeAction();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout llContainer = findViewById(R.id.llContainer);
        ImageView ivLogo = findViewById(R.id.ivLogo);

        llContainer.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_from_bottom_to_top));
        ivLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_from_left_to_right));

    }

    @Override
    public void onBackPressed() {

        //takeAction();
    }

    public void takeAction() {
        SplashScreenActivity.language = SharedPref.Get_lan(getApplication());
        Intent i = new Intent(Activity_language.this, LoginActivity.class);
        startActivity(i);

    }
}
