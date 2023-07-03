package com.mk_tech.delivery.ui.fragment.bottomMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.SplashScreenActivity;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
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

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileSettingsFragment extends Fragment {

    TextView tvLanguage,tvContactUs, tvAboutUS, tvLogout,tvAccountMoney,tvProfileDetails;
    LinearLayout llAccountant;
     private FragmentProfileSettingsBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setLayoutStyle(true, true, false, false, false, true, R.string.profile);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvLanguage = binding.tvLanguage;
        llAccountant = binding.llAccountant;
        tvAboutUS = binding.tvAboutUS;
        tvContactUs = binding.tvContactUs;
        tvLogout = binding.tvLogout;
        tvLogout = binding.tvLogout;
        tvAccountMoney=binding.tvAccountMoney;
        tvProfileDetails=binding.tvProfileDetails;
        tvProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.context.navController.navigate(R.id.navigation_profileDetails);

            }
        });

        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_language_settings.class));
            }
        });

        tvAboutUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityTearmsAndFaq.class).putExtra("type", "aboutUs").putExtra("title", getString(R.string.about_us)));

            }
        });

        tvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityTearmsAndFaq.class).putExtra("type", "contactUs").putExtra("title", getString(R.string.contact_with_us)));

            }
        });



        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.Save_token(getActivity(), "");
                startActivity(new Intent(getActivity(), SplashScreenActivity.class));
                getActivity().finish();
            }
        });
        return root;
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

                                    tvAccountMoney.setText(value.getData().getLoyality_points() + " " + getString(R.string.Points));

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
         getProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}