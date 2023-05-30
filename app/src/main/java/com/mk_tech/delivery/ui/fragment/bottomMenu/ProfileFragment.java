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
import com.mk_tech.delivery.databinding.FragmentProfileBinding;
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

public class ProfileFragment extends Fragment {

    TextView tvUserName, tvMyAddress,tvTerms, tvFAQ, tvArea, tvEmail, tvLanguage, tvSignIn, tvSignUp, tvAccountDetails, tvOrders,
            tvPoints,tvLogout;
    LinearLayout llLoginAndSignup, llContainer, llLanguage, llLoyaltyPoints, llSelectArea;
    ImageView  ivProfile;
    private FragmentProfileBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setLayoutStyle(true, true, false, false, false, true, R.string.profile);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvUserName = binding.tvUserName;
        tvLanguage = binding.tvLanguage;
        llLoginAndSignup = binding.llLoginAndSignup;
        llContainer = binding.llContainer;
        llLanguage = binding.llLanguage;
        tvSignIn = binding.tvSignIn;
        tvSignUp = binding.tvSignUp;
        tvAccountDetails = binding.tvAccountDetails;
        llLoyaltyPoints = binding.llLoyaltyPoints;
        tvEmail = binding.tvEmail;
        tvMyAddress=binding.tvMyAddress;
        tvOrders = binding.tvOrders;
        tvPoints = binding.tvPoints;
        tvLogout = binding.tvLogout;
        llSelectArea = binding.llSelectArea;
        tvArea = binding.tvArea;
        tvTerms = binding.tvTerms;
        tvFAQ = binding.tvFAQ;
        ivProfile = binding.ivProfile;

        if (SharedPref.Get_IsGuest(getContext())) {
            llLoginAndSignup.setVisibility(View.VISIBLE);
            llContainer.setVisibility(View.GONE);
            tvLogout.setVisibility(View.GONE);
        } else {
            llLoginAndSignup.setVisibility(View.GONE);
            llContainer.setVisibility(View.VISIBLE);
            tvUserName.setText(SharedPref.Get_user_data(getContext(), SharedPref.USER_NAME));
            tvEmail.setText(SharedPref.Get_user_data(getContext(), SharedPref.USER_EMAIL));

        }

        tvLanguage.setText(SharedPref.Get_lan(getContext()).equals("ar") ? "عربى" : "English");
        llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_language_settings.class));
            }
        });
        tvMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_address.class));
            }
        });
        tvFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityTearmsAndFaq.class).putExtra("type", "faq").putExtra("title", getString(R.string.faq)));

            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityTearmsAndFaq.class).putExtra("type", "terms").putExtra("title", getString(R.string.terms_amp_conditions)));

            }
        });


        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        tvAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityProfileUpdate.class));
            }
        });

        tvOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_orders.class));
            }
        });



        llSelectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_select_area.class));
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

                                    tvPoints.setText(value.getData().getLoyality_points() + " " + getString(R.string.Points));
                                    Glide.with(getActivity()).applyDefaultRequestOptions(
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

    @Override
    public void onResume() {
        super.onResume();
        tvArea.setText(SharedPref.Get_user_data(getContext(), SharedPref.AREA));
        getProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}