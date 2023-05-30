package com.mk_tech.delivery.adapter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.model.Banner;
import com.mk_tech.delivery.ui.activity.Activity_company_details;
import com.mk_tech.delivery.ui.activity.Activity_product_details;

import java.util.ArrayList;
import java.util.List;


public class BanerAdapter extends
        SliderViewAdapter<BanerAdapter.SliderAdapterVH> {

    private final Context context;
    int type = 1;
     String model;
    int model_id;
    private List<Banner.Image> mSliderItems = new ArrayList<>();

    public BanerAdapter(Context context, List<Banner.Image> sliderItems, String model,int model_id, int type) {
        this.context = context;
        this.mSliderItems = sliderItems;
        this.type = type;
        this.model = model;
        this.model_id = model_id;
     }



    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate((type == 1 ? R.layout.slider_row : R.layout.slider_i_row), null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Banner.Image sliderItem = mSliderItems.get(position);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getUrl())
                .into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model_id>0) {
                    if(model.equals("companies")){
                        context.startActivity(new Intent(context, Activity_company_details.class).putExtra("id", model_id).addFlags(FLAG_ACTIVITY_NEW_TASK));
                    }else if(model.equals("products")){
                        context.startActivity(new Intent(context, Activity_product_details.class).putExtra("id", model_id).addFlags(FLAG_ACTIVITY_NEW_TASK));
                    }

                } else {
                  /*  Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.image_zoom_dialog);
                    dialog.setCancelable(true);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    ImageView imageClose = dialog.findViewById(R.id.imageClose);
                    ZoomageView myZoomView = dialog.findViewById(R.id.myZoomView);
                    VideoView videoView = dialog.findViewById(R.id.videoView);


                    imageClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                    videoView.setVisibility(View.GONE);
                    Glide.with(viewHolder.itemView)
                            .load(sliderItem.getUrl())
                            .into(myZoomView);

                    dialog.show();*/
                }


                // Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageView, ivPlay;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            this.itemView = itemView;
        }
    }
}
