package com.mk_tech.delivery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    RadioButton rbAcceptTerms;
    EditText etFirstName, etLastName, etEmail, etMobile, etPassword, etConfirmPassword;
    String token;
    TextView tvLogin;
    TextView tvSkip;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UtilsClass.init_AlertDialog(this);

        tvLogin = findViewById(R.id.tvLogin);
        btnRegister = findViewById(R.id.btnRegister);
        rbAcceptTerms = findViewById(R.id.rbAcceptTerms);
        etFirstName = findViewById(R.id.etFirstName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvSkip = findViewById(R.id.tvSkip);
        etLastName = findViewById(R.id.etLastName);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.Save_token(getApplicationContext(), "");
                SharedPref.Save_IsGuest(getApplicationContext(), true);
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!rbAcceptTerms.isChecked()) {
                    UtilsClass.Show_AlertDialog(getString(R.string.error), getString(R.string.Please_read_and_accept_terms));

                } else if (etMobile.getText().toString().isEmpty()) {
                    UtilsClass.Show_AlertDialog(getString(R.string.error), getString(R.string.enter_mobile_number));

                } else if (etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
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
                                    register();
                                    Log.d("token:", token);
                                }
                            });
                } else {
                    UtilsClass.Show_AlertDialog(getString(R.string.error), getString(R.string.confirm_password_does_not_match));
                }

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void register() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.Register(etFirstName.getText().toString() + " " + etLastName.getText().toString(), etEmail.getText().toString(),
                                etMobile.getText().toString(), etPassword.getText().toString(), "male", "android", token)
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
                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
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