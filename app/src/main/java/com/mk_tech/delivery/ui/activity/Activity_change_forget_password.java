package com.mk_tech.delivery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.RegisterModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Activity_change_forget_password extends AppCompatActivity {

    Button btn_update, btnSend;
    EditText etCode, etNewPass, etConfirmPass;
    LinearLayout llChangePass, llSMSCode;
    String email, token;
    String code;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_forget_password);
        btn_update = findViewById(R.id.btn_update);
        btnSend = findViewById(R.id.btnSend);
        etCode = findViewById(R.id.etCode);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        llChangePass = findViewById(R.id.llChangePass);
        llSMSCode = findViewById(R.id.llSMSCode);
        ImageView ivBack = findViewById(R.id.ivBack);

        Intent i = getIntent();
        code = i.getIntExtra("code", 0) + "";
        email = i.getStringExtra("email");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCode.getText().toString().equals(code)) {
                    llChangePass.setVisibility(View.VISIBLE);
                    llSMSCode.setVisibility(View.GONE);
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

    }

    void updatePassword() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.init_AlertDialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, "", SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.changeForgetPassword(etCode.getText().toString(), email, etNewPass.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<RegisterModel>() {
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
                            public void onNext(RegisterModel value) {
                                if (value.getSuccess()) {

                                    FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.w("getInstanceId failed", "getInstanceId failed", task.getException());
                                                        token = "1234";
                                                    } else {
                                                        // Get new Instance ID token
                                                        token = task.getResult().getToken();
                                                    }

                                                    login();
                                                    Log.d("token:", token);
                                                }
                                            });
                                } else {
                                    UtilsClass.Show_AlertDialog(getString(R.string.error), value.getMessage());
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

    void login() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.init_AlertDialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.Login(email, etNewPass.getText().toString(), "android", token)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<RegisterModel>() {
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
                            public void onNext(RegisterModel value) {
                                if (value.getSuccess()) {
                                    SharedPref.Save_token(getApplicationContext(), value.getData().getToken());
                                    SharedPref.Save_IsGuest(getApplicationContext(), false);
                                    SharedPref.Save_user_data(getApplicationContext(), value.getData().getUserModel().getId() + ""
                                            , value.getData().getUserModel().getName()
                                            , value.getData().getUserModel().getEmail()
                                            , value.getData().getUserModel().getMobile());
                                    Intent i = new Intent(Activity_change_forget_password.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Log.d("--", "data suc");

                                } else {
                                    UtilsClass.Show_AlertDialog(getString(R.string.error), value.getMessage());
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