package com.mk_tech.delivery.ui.fragment.bottomMenu;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.faltenreich.skeletonlayout.Skeleton;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.databinding.FragmentHomeBinding;
import com.mk_tech.delivery.model.HomeModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    public static HomeModel homeModel;

    Skeleton skeleton;

    NestedScrollView scrollView;
    private FragmentHomeBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setLayoutStyle(true, false, false, false, false, false, R.string.title_home);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        UtilsClass.init_AlertDialog(getActivity());

        scrollView = binding.scrollView;

        skeleton = binding.skeletonLayout;



        if (homeModel == null) {
            getHome();
        } else {
            setViewData();
        }


        return root;
    }


    public void getHome() {
        UtilsClass.init_Progress_Dialog(getActivity());
        //UtilsClass.Show_Progress_Dialog();
        skeleton.showSkeleton();

        try {
            if (NetworkUtil.isNetworkAvaliable(getActivity())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getActivity()), SharedPref.Get_lan(getActivity()));
                Subscription mSubscription = apiService.getHome()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<HomeModel>() {
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
                            public void onNext(HomeModel value) {
                                if (value.getSuccess()) {
                                    homeModel = value;
                                    setViewData();
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

    void setViewData() {


        //sliderTop.startAutoCycle();


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
     }
}