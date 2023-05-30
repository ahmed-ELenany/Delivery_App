package com.mk_tech.delivery.ui.activity;

import static com.mk_tech.delivery.Utilities.SharedPref.USER_EMAIL;
import static com.mk_tech.delivery.Utilities.SharedPref.USER_NAME;
import static com.mk_tech.delivery.Utilities.SharedPref.USER_PHONE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.FileUtils;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.ProfileUpdateModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityProfileUpdate extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 1;
    Button btnUpdate;
    EditText etFirstName, tvMobile;
    TextView etEmail, tvForgetPassword;
    String token;
    TextView tvEditImage;
    Uri fileImageUri;
    ImageView ivProfile;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        UtilsClass.init_AlertDialog(this);

        btnUpdate = findViewById(R.id.btnUpdate);
        etFirstName = findViewById(R.id.etFirstName);
        etEmail = findViewById(R.id.etEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvEditImage = findViewById(R.id.tvEditImage);
        ivProfile = findViewById(R.id.ivProfile);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storagePermission();
                showMultiFileChooser("image");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileUpdate();


            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityProfileUpdate.this, Activity_change_password.class));
            }
        });

        etFirstName.setText(SharedPref.Get_user_data(getApplicationContext(), USER_NAME));
        etEmail.setText(SharedPref.Get_user_data(getApplicationContext(), USER_EMAIL));
        tvMobile.setText(SharedPref.Get_user_data(getApplicationContext(), USER_PHONE));
        getProfile();
    }

    private void showMultiFileChooser(String type) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"image/*", "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Log.d("file base:", data.getData() + "");
            fileImageUri = data.getData();

            Glide.with(ActivityProfileUpdate.this).applyDefaultRequestOptions(
                    new RequestOptions()
                            .placeholder(R.drawable.ic_user_profile)
                            .error(R.drawable.ic_user_profile)
            ).load(data.getData()).circleCrop().into(ivProfile);
        }
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
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    void profileUpdate() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.init_AlertDialog(this);
        UtilsClass.Show_Progress_Dialog();
        MultipartBody.Part bodyImage = null;
        if (fileImageUri != null) {
            File file = FileUtils.getFile(this, fileImageUri);
            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileImageUri)), file);
            // MultipartBody.Part is used to send also the actual file name
            bodyImage = MultipartBody.Part.createFormData("profile", file.getName(), requestFile);
        }
        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.profileUpdate(
                                RequestBody.create(MediaType.parse("text/plain"), etFirstName.getText().toString()),
                                RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString()),
                                RequestBody.create(MediaType.parse("text/plain"), tvMobile.getText().toString()),
                                bodyImage)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ProfileUpdateModel>() {
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
                            public void onNext(ProfileUpdateModel value) {
                                if (value.getSuccess()) {
                                    SharedPref.Save_user_data(getApplicationContext(), value.getData().getId() + ""
                                            , value.getData().getName()
                                            , value.getData().getEmail()
                                            , value.getData().getMobile());
                                    Intent i = new Intent(ActivityProfileUpdate.this, MainActivity.class);
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

    public void getProfile() {
        UtilsClass.init_Progress_Dialog(ActivityProfileUpdate.this);
        UtilsClass.Show_Progress_Dialog();
        try {
            if (NetworkUtil.isNetworkAvaliable(ActivityProfileUpdate.this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(ActivityProfileUpdate.this), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.getProfile()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ProfileUpdateModel>() {
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
                            public void onNext(ProfileUpdateModel value) {
                                if (value.getSuccess()) {


                                    Glide.with(ActivityProfileUpdate.this).applyDefaultRequestOptions(
                                            new RequestOptions()
                                                    .placeholder(R.drawable.ic_user_profile)
                                                    .error(R.drawable.ic_user_profile)
                                    ).load(value.getData().getProfile()).circleCrop().into(ivProfile);
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