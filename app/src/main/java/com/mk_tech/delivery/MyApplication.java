package com.mk_tech.delivery;

import android.app.Application;
import android.content.res.Configuration;

import com.mk_tech.delivery.Utilities.SharedPref;

import java.util.Locale;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String lang = SharedPref.Get_lan(getApplicationContext());
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}