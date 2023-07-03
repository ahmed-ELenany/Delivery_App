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
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.FileUtils;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.NothingSelectedSpinnerAdapter;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.OrderAdapter;
import com.mk_tech.delivery.databinding.FragmentOrdersHistoryBinding;
import com.mk_tech.delivery.databinding.FragmentProfileDetailsBinding;
import com.mk_tech.delivery.model.OrdersModel;
import com.mk_tech.delivery.model.ProfileUpdateModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrdersHistoryFragment extends Fragment {


     private FragmentOrdersHistoryBinding binding;
    TextView tvToday,tvYesterday,tvThisMonth;
    RecyclerView RvOrders;
    ScrollView containerNoData;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setLayoutStyle(true, true, false, false, false, true, R.string.profile);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrdersHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvYesterday=binding.tvYesterday;
        tvThisMonth=binding.tvThisMonth;
        tvToday=binding.tvToday;
        RvOrders = binding.RvOrders;
        containerNoData=binding.containerNoData;

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(tvToday);

            }
        });

        tvYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(tvYesterday);
            }
        });

        tvThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(tvThisMonth);
            }
        });

        containerNoData.setVisibility(View.GONE);
        RvOrders.setVisibility(View.VISIBLE);

        List<OrdersModel.DataModel> dataList =new ArrayList<>();

        OrdersModel.DataModel dataModel=new OrdersModel.DataModel();
        dataModel.setStatus("Pending");
        dataList.add(dataModel);

        OrdersModel.DataModel dataModel2=new OrdersModel.DataModel();
        dataModel2.setStatus("Completed");
        dataList.add(dataModel2);

        OrdersModel.DataModel dataModel3=new OrdersModel.DataModel();
        dataModel3.setStatus("Cancelled");
        dataList.add(dataModel3);


        dataList.add(dataModel);
        dataList.add(dataModel3);
        dataList.add(dataModel2);




        OrderAdapter orderAdapter = new OrderAdapter(getActivity(),  dataList);
        RecyclerView.LayoutManager mLayoutHomeManager3 = new LinearLayoutManager(getActivity());
        RvOrders.setLayoutManager(mLayoutHomeManager3);
        ((LinearLayoutManager) mLayoutHomeManager3).setOrientation(RecyclerView.VERTICAL);
        RvOrders.setAdapter(orderAdapter);

        return root;
    }

void setView(TextView view){
    tvYesterday.setTextColor(getResources().getColor(R.color.transparent_green));
    tvThisMonth.setTextColor(getResources().getColor(R.color.transparent_green));
    tvToday.setTextColor(getResources().getColor(R.color.transparent_green));

    tvYesterday.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    tvThisMonth.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    tvToday.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    view.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_bottom);



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