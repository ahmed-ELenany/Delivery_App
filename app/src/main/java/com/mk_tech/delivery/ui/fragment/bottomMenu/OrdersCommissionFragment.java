package com.mk_tech.delivery.ui.fragment.bottomMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.adapter.OrderAdapter;
import com.mk_tech.delivery.databinding.FragmentOrdersHistoryBinding;
import com.mk_tech.delivery.model.OrdersModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersCommissionFragment extends Fragment {


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