package com.mk_tech.delivery.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {


    public static final String ACCESS_TOKEN = "access_token";
    public static final String SELECTED_LANGUAGE = "selected_lang";
    public static final String SELECTED_CURRENCY = "selected_currency";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PHONE = "phone";
    public static final String ISAGUEST = "guest";
    public static final String CITY = "city";
    public static final String AREA = "area";
    public static final String CITY_ID = "area_id";
    public static final String AREA_ID = "city_id";


    public static void Save_lan(Context context, String lan) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(SELECTED_LANGUAGE, lan);
        editor.apply();
    }

    public static String Get_lan(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(SELECTED_LANGUAGE)) {
            return sharedPreferences.getString(SELECTED_LANGUAGE, "en");
        }
        return "";
    }

    public static void Save_IsGuest(Context context, boolean isGuest) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(ISAGUEST, isGuest);
        editor.apply();
    }

    public static boolean Get_IsGuest(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(ISAGUEST)) {
            return sharedPreferences.getBoolean(ISAGUEST, false);
        }
        return false;
    }

    public static void Save_currency(Context context, String currency) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(SELECTED_CURRENCY, currency);
        editor.apply();
    }

    public static String Get_currency(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(SELECTED_CURRENCY)) {
            return sharedPreferences.getString(SELECTED_CURRENCY, "kwd");
        }
        return "kwd";
    }


    public static String Get_token(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(ACCESS_TOKEN)) {
            return sharedPreferences.getString(ACCESS_TOKEN, "");
        }
        return "";
    }


    public static void Save_area_city(Context context, String key, String value) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void Save_token(Context context, String token) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN, token);
        editor.apply();
    }

    public static String Get_user_data(Context context, String dataName) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(dataName)) {
            return sharedPreferences.getString(dataName, "");
        }
        return "";
    }

    public static void Save_user_data(Context context, String id, String name, String email, String mobile) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(USER_ID, id);
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_PHONE, mobile);
        editor.apply();
    }

}