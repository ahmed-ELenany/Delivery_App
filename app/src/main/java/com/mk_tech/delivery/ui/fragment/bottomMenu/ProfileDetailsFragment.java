package com.mk_tech.delivery.ui.fragment.bottomMenu;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.SplashScreenActivity;
import com.mk_tech.delivery.Utilities.FileUtils;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.NothingSelectedSpinnerAdapter;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.databinding.FragmentProfileDetailsBinding;
import com.mk_tech.delivery.databinding.FragmentProfileSettingsBinding;
import com.mk_tech.delivery.databinding.FragmentProfileSettingsBinding;
import com.mk_tech.delivery.model.ProfileUpdateModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;
import com.mk_tech.delivery.ui.activity.ActivityProfileUpdate;
import com.mk_tech.delivery.ui.activity.ActivityTearmsAndFaq;
import com.mk_tech.delivery.ui.activity.Activity_address;
import com.mk_tech.delivery.ui.activity.Activity_language_settings;
import com.mk_tech.delivery.ui.activity.Activity_orders;
import com.mk_tech.delivery.ui.activity.Activity_select_area;
import com.mk_tech.delivery.ui.activity.LoginActivity;
import com.mk_tech.delivery.ui.activity.RegisterActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileDetailsFragment extends Fragment {


     private FragmentProfileDetailsBinding binding;
TextView tvSettings;
    Button btnSave;
    RadioButton rbAcceptTerms;
    EditText etUserName , etEmail, etMobile, etPassword, etConfirmPassword,etDriverLicenseNumber,etCivilIdNumber,etPassportNumber;
    String token;
    TextView tvLogin;
    private final int PICK_IMAGE_REQUEST = 1;
    TextView tvDriverLicenseAttachment,tvCivilId,tvPassport;
    String attachmentType,getAttachmentName;
    Spinner spinnerNationality;
    Uri uriDriverLicenseAttachment,uriCivilId,uriPassport;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setLayoutStyle(true, true, false, false, false, true, R.string.profile);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvSettings=binding.tvSettings;
        tvDriverLicenseAttachment = binding.tvDriverLicenseAttachment;
        tvCivilId = binding.tvCivilId;
        spinnerNationality = binding.spinnerNationality;

        tvPassport = binding.tvPassport;
        tvLogin = binding.tvLogin;
        btnSave = binding.btnSave;
        rbAcceptTerms = binding.rbAcceptTerms;
        etUserName = binding.etUserName;
        etEmail = binding.etEmail;
        etMobile = binding.etMobile;
        etPassword = binding.etPassword;
        etConfirmPassword = binding.etConfirmPassword;
        etDriverLicenseNumber=binding.etDriverLicenseNumber;
        etCivilIdNumber= binding.etCivilIdNumber;
        etPassportNumber= binding.etPassportNumber;


        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.context.navController.navigate(R.id.navigation_profile);

            }
        });
        List<String> listData = new ArrayList<>();
        listData.add("kuwait");
        listData.add("Egypt");
        listData.add("Saudi Arabia");
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNationality.setPrompt(getString(R.string.nationality));
        NothingSelectedSpinnerAdapter sizeSpinnerAdapter= new NothingSelectedSpinnerAdapter(
                aa,
                R.layout.government_spinner_row_nothing_selected,
                getActivity());
        spinnerNationality.setAdapter(sizeSpinnerAdapter);

        spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView spinner_item_text = (TextView) selectedItemView;
                 spinner_item_text.setTextColor(Color.parseColor("#998895B7"));
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

        return root;
    }
    void storagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
                    if (ContextCompat.checkSelfPermission(getActivity(),
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
        getActivity().startActivityForResult(Intent.createChooser(intent, getAttachmentName), PICK_IMAGE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null  ) {
            Uri filePath =data.getData();
            if(filePath!=null) {
                File file = FileUtils.getFile(getActivity(), filePath);

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
    public void getProfile() {
        UtilsClass.init_Progress_Dialog(getActivity());
        UtilsClass.Show_Progress_Dialog();
        try {
            if (NetworkUtil.isNetworkAvaliable(getActivity())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getActivity()), SharedPref.Get_lan(getActivity()));
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

    @Override
    public void onResume() {
        super.onResume();
       //  getProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}