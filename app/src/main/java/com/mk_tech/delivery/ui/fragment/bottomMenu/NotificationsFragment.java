package com.mk_tech.delivery.ui.fragment.bottomMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.databinding.FragmentNotificationBinding;

public class NotificationsFragment extends Fragment {

    ScrollView scrollViewNoData;
    RecyclerView RV;
    private FragmentNotificationBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setLayoutStyle(true, true, false, false, false, true, R.string.notifications);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        scrollViewNoData = binding.scrollViewNoData;
        RV = binding.RV;
       /* List<Product> productList = new ArrayList<>();
        Product product = new Product();
        productList.add(product);
        productList.add(product);
        productList.add(product);
        productList.add(product);
        productList.add(product);

        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), productList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        RV.setLayoutManager(gridLayoutManager);
        RV.setAdapter(notificationAdapter);

        scrollViewNoData.setVisibility(View.GONE);
        RV.setVisibility(View.VISIBLE);*/

        // getWishlists();

        return root;
    }

   /* public void getWishlists() {

        try {
            if (NetworkUtil.isNetworkAvaliable(getActivity())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getActivity()), SharedPref.Get_lan(getActivity()));
                Subscription mSubscription = apiService.getWishlistsProduct(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CartModel>() {
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
                            public void onNext(CartModel value) {
                                if (value.getSuccess()) {
                                    if (value.getData().size() > 0) {
                                        scrollViewNoData.setVisibility(View.VISIBLE);
                                        RV.setVisibility(View.GONE);

                                    } else {
                                        scrollViewNoData.setVisibility(View.GONE);
                                        RV.setVisibility(View.VISIBLE);
                                    }
                                     ProductAdapter productAdapter = new ProductAdapter(getContext(), value.getData());
                                    RecyclerView.LayoutManager mLayoutHomeManager = new LinearLayoutManager(getContext());
                                    RV.setLayoutManager(mLayoutHomeManager);
                                    ((LinearLayoutManager) mLayoutHomeManager).setOrientation(RecyclerView.VERTICAL);
                                    RV.setAdapter(productAdapter);

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

    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}