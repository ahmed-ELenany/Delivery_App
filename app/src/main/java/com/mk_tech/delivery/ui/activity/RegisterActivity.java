package com.mk_tech.delivery.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.FileUtils;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.NothingSelectedSpinnerAdapter;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.RegisterModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    RadioButton rbAcceptTerms;
    EditText etUserName , etEmail, etMobile, etPassword, etConfirmPassword,etDriverLicenseNumber,etCivilIdNumber,etPassportNumber;
    String token;
    TextView tvLogin;
    private final int PICK_IMAGE_REQUEST = 1;
    TextView tvDriverLicenseAttachment,tvCivilId,tvPassport;
    String attachmentType,getAttachmentName;
    Spinner spinnerNationality;
    Uri  uriDriverLicenseAttachment,uriCivilId,uriPassport;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UtilsClass.init_AlertDialog(this);
         tvDriverLicenseAttachment = findViewById(R.id.tvDriverLicenseAttachment);
        tvCivilId = findViewById(R.id.tvCivilId);
        spinnerNationality = findViewById(R.id.spinnerNationality);

        tvPassport = findViewById(R.id.tvPassport);
        tvLogin = findViewById(R.id.tvLogin);
        btnRegister = findViewById(R.id.btnRegister);
        rbAcceptTerms = findViewById(R.id.rbAcceptTerms);
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etDriverLicenseNumber= findViewById(R.id.etDriverLicenseNumber);
        etCivilIdNumber= findViewById(R.id.etCivilIdNumber);
        etPassportNumber= findViewById(R.id.etPassportNumber);
         ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<String> listData = new ArrayList<>();
        listData.add("kuwait");
        listData.add("Egypt");
        listData.add("Saudi Arabia");
         //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, listData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNationality.setPrompt(getString(R.string.nationality));
        NothingSelectedSpinnerAdapter sizeSpinnerAdapter= new NothingSelectedSpinnerAdapter(
                aa,
                R.layout.government_spinner_row_nothing_selected,
                getApplicationContext());
        spinnerNationality.setAdapter(sizeSpinnerAdapter);

        spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView spinner_item_text = (TextView) selectedItemView;
                 //spinner_item_text.setPadding(UtilsClass.dip2px(getApplicationContext(),30),0,UtilsClass.dip2px(getApplicationContext(),30),0);
                 spinner_item_text.setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        tvDriverLicenseAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentType="DriverLicenseAttachment";
                getAttachmentName=getString(R.string.driver_license);
                storagePermission();

            }
        });

        tvCivilId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentType="CivilId";
                getAttachmentName=getString(R.string.civil_id);
                storagePermission();

            }
        });
        tvPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentType="Passport";
                getAttachmentName=getString(R.string.passport);
                storagePermission();

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

    void storagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        showAttachmentChooser();

                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }



        }
    }
    private void showAttachmentChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(intent, getAttachmentName), PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null  ) {
            Uri filePath =data.getData();
            if(filePath!=null) {
                File file = FileUtils.getFile(this, filePath);

                Log.d("file base:", filePath + "");
                Log.d("fileName:", file.getName() + "");
                if(attachmentType.equals("DriverLicenseAttachment")){
                    tvDriverLicenseAttachment.setText(file.getName());
                    uriDriverLicenseAttachment=filePath;
                }else if(attachmentType.equals("CivilId")){
                    tvCivilId.setText(file.getName());
                    uriCivilId=filePath;

                }else if(attachmentType.equals("Passport")){
                    tvPassport.setText(file.getName());
                    uriPassport=filePath;

                }
             }

        }
    }

    void register() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.Register(  etUserName.getText().toString(), etEmail.getText().toString(),
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