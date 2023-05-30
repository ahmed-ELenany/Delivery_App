package com.mk_tech.delivery.adapter;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.Company;
import com.mk_tech.delivery.ui.activity.Activity_company_details;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.viewHolder> {

    private final Context context;
    public String layoutType;
    List<Company> dataList;
    String companyType;
    public CompanyAdapter(Context c, List<Company> dataList, String layoutType,String companyType) {
        this.context = c;
        this.dataList = dataList;
        this.layoutType = layoutType;
        this.companyType = companyType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutType.equals("large") ? R.layout.company_large_row : R.layout.company_row, parent, false);

        if (layoutType.equals("small_horizontal")) {
            itemView.getLayoutParams().width = (parent.getMeasuredWidth() / 3) - 5;
        }

        return new viewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .placeholder(R.drawable.picture)
                        .error(R.drawable.picture)

        ).load(dataList.get(position).getProfile()).into(holder.ivProfile);

        if (layoutType.equals("large")) {
            holder.tvName.setText(dataList.get(position).getName());
            // holder.tv_Description.setText(dataList.get(position).getDescription());

        }


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   context.startActivity(new Intent(context, Activity_company_details.class).putExtra("id", dataList.get(holder.getAdapterPosition()).getId()).addFlags(FLAG_ACTIVITY_NEW_TASK));



            }
        });

    }

    //**********************************************************************************************************
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tv_Description;
        ImageView ivProfile;
        LinearLayout container;

        public viewHolder(View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tv_Description = itemView.findViewById(R.id.tv_Description);
            container = itemView.findViewById(R.id.container);
        }
    }

}
