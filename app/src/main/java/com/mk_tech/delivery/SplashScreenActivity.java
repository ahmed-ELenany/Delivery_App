package com.mk_tech.delivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.RootUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.TypefaceUtil;
import com.mk_tech.delivery.ui.activity.Activity_language;
import com.mk_tech.delivery.ui.activity.LoginActivity;


public class SplashScreenActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 20;
    private static final int SPLASH_TIME_OUT = 4000;
    public static String language;
    ImageView iv_splash, iv_splash_head, iv_logo;
boolean splashDone=false,versionCheckDone=false;
    public static void setLanLocal(Context context, String language) {
        if (language.isEmpty() || language.equals("ar")) {
            LocaleHelper.setLocale(context, "ar");
            TypefaceUtil.overrideFont(context, "SERIF", "font/f_regular.ttf");
        } else {
            LocaleHelper.setLocale(context, language);
            TypefaceUtil.overrideFont(context, "SERIF", "font/f_regular.ttf");
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        language = SharedPref.Get_lan(getApplication());
        setLanLocal(getApplication(), language);
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashDone=true;
                versionCheckDone=true;
                takeAction();
            }
        }, SPLASH_TIME_OUT);
      //  getStoreVersion();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (new RootUtil().isDeviceRooted()) {
            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
        }

    }


    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }
//    public void getStoreVersion( ) {
//        try {
//             UtilsClass.init_AlertDialog(SplashScreenActivity.this);
//             if (NetworkUtil.isNetworkAvaliable(getApplicationContext())) {
//                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, "", SharedPref.Get_lan(getApplicationContext()));
//                Subscription mSubscription = apiService.getStoreVersion()
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new Subscriber<StoreVersionModel>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                e.printStackTrace();
//                                UtilsClass.Show_AlertDialog(getString(R.string.error),   getString(R.string.internet_error));
//
//                            }
//
//                            @Override
//                            public void onNext(StoreVersionModel value) {
//                                if (value.getSuccess()) {
//                                    int storeVersion= Integer.parseInt(value.getData().getPlayStore());
//                                    int versionCode =BuildConfig.VERSION_CODE;
//
//                                    if(versionCode<storeVersion){
//
//                                        Dialog alertDialog = new Dialog(SplashScreenActivity.this);
//
//                                        alertDialog.setContentView(R.layout.custom_alert_dialog);
//                                        alertDialog.setCancelable(false);
//                                        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                                        TextView txtBody = alertDialog.findViewById(R.id.txtBody);
//                                        TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
//                                        TextView tvYes = alertDialog.findViewById(R.id.tvYes);
//                                        txtTitle.setText(R.string.Update_is_available);
//                                        txtBody.setText(R.string.update_version_now );
//                                        tvYes.setText(R.string.update);
//                                         tvYes.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                String url = "https://play.google.com/store/apps/details?id=com.store.Wiregcc";
//                                                Intent i = new Intent(Intent.ACTION_VIEW);
//                                                i.setData(Uri.parse(url));
//                                                startActivity(i);
//                                            }
//                                        });
//
//
//                                        alertDialog.show();
//
//                                    }else{
//                                        versionCheckDone=true;
//                                        takeAction();
//                                    }
//                                } else {
//                                    UtilsClass.Show_AlertDialog(getString(R.string.error), value.getMessage());
//
//                                }
//
//                            }
//                        });
//
//
//            } else {
//                 UtilsClass.Show_AlertDialog(getString(R.string.error),   getString(R.string.internet_error));
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            UtilsClass.Show_AlertDialog(getString(R.string.error),   getString(R.string.internet_error));
//
//
//        }
//
//    }

    public void takeAction() {

        if(versionCheckDone&&splashDone){

        //deep link
       /* Intent i;
        Uri url = getIntent().getData();
        getIntent().getExtras().getString("entityid");*/

        if (!SharedPref.Get_lan(getApplicationContext()).isEmpty()) {
            if (SharedPref.Get_token(getApplicationContext()).isEmpty()) {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        } else {
            Intent i = new Intent(SplashScreenActivity.this, Activity_language.class);
            startActivity(i);
            finish();
        }
        }
    }

}
