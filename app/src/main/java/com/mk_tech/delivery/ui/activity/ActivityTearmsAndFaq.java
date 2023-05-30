package com.mk_tech.delivery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.TearmsModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityTearmsAndFaq extends AppCompatActivity {

    TextView tvText;
    TextView tvTitle;
    ImageView ivBack;
    String type = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tearms_faq);
        tvText = findViewById(R.id.tvText);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i = getIntent();
        String type = i.getStringExtra("type");
        String title = i.getStringExtra("title");
        tvTitle.setText(title);
        getData(type);

    }

    public void getData(String type) {
        try {
            UtilsClass.init_AlertDialog(ActivityTearmsAndFaq.this);
            UtilsClass.init_Progress_Dialog(ActivityTearmsAndFaq.this);
            UtilsClass.Show_Progress_Dialog();
            if (NetworkUtil.isNetworkAvaliable(getApplicationContext())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, "", SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.getData(type)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<TearmsModel>() {
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
                            public void onNext(TearmsModel value) {
                                if (value.getSuccess()) {
                                    tvText.setText(value.getData());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        tvText.setText(Html.fromHtml(value.getData(), Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        tvText.setText(Html.fromHtml(value.getData()));

                                    }

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


}