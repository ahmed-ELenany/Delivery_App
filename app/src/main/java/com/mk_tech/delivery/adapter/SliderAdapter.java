package com.mk_tech.delivery.adapter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.SliderModel;
import com.mk_tech.delivery.ui.activity.Activity_companies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

     static MediaPlayer mediaPlayer;
    private final Context context;
    int type = 1;
    SliderView sliderView;
      boolean muted=true;
     private List<SliderModel> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context, SliderView sliderView, List<SliderModel> sliderItems, int type) {
        this.context = context;
        this.mSliderItems = sliderItems;
        this.sliderView = sliderView;
        this.type = type;
        mediaPlayer=null;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }



    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate((type == 1 ? R.layout.slider_row : R.layout.slider_i_row), null);

        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
         SliderModel sliderItem = mSliderItems.get(position);

            if (sliderItem.getType() != null && sliderItem.getType().equals("video")) {

                viewHolder.ivPlay.setVisibility(View.VISIBLE);
                viewHolder.ivPlay.setBackgroundResource(R.drawable.picture);
                viewHolder.videoView.setVisibility(View.VISIBLE);
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.videoView.setVideoPath(sliderItem.getUrl());
                viewHolder.videoView.getLayoutParams().height= UtilsClass.dip2px(context,300);
                viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        try{
                            if (mediaPlayer==null || !mediaPlayer.isPlaying()) {
                                mp.setLooping(true);
                                mp.start();
                                mediaPlayer=mp;
                            }
                            viewHolder.ivVolume.setVisibility(View.VISIBLE);
                            setVolumeControl(mp,viewHolder.ivVolume);
                            viewHolder.ivPlay.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                            mp.setLooping(true);
                            mp.start();
                            mediaPlayer=mp;
                        viewHolder.ivVolume.setVisibility(View.VISIBLE);
                        setVolumeControl(mp,viewHolder.ivVolume);
                        viewHolder.ivPlay.setVisibility(View.GONE);

                    }

                    }
                });

             } else {
                try{
                    if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
                        muted=true;
                        mediaPlayer.setVolume(0F, 0F);
                     }
                }catch (Exception ex){

                }
                viewHolder.ivPlay.setVisibility(View.GONE);
                viewHolder.videoView.setVisibility(View.GONE);
                viewHolder.ivVolume.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.VISIBLE);
                Glide.with(viewHolder.itemView)
                        .load(sliderItem.getUrl())
                        .into(viewHolder.imageView);

            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sliderItem.getLink() != null && !sliderItem.getLink().isEmpty()) {
                        context.startActivity(new Intent(context, Activity_companies.class).putExtra("type", "wire_store").addFlags(FLAG_ACTIVITY_NEW_TASK));
                  } else {
//                        Dialog dialog = new Dialog(context);
//                        dialog.setContentView(R.layout.image_zoom_dialog);
//                        dialog.setCancelable(true);
//                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                        ImageView imageClose = dialog.findViewById(R.id.imageClose);
//                        ZoomageView myZoomView = dialog.findViewById(R.id.myZoomView);
//                        VideoView videoView = dialog.findViewById(R.id.videoView);
//
//
//                        imageClose.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        if (sliderItem.getType() != null && sliderItem.getType().equals("video")) {
//                            myZoomView.setVisibility(View.GONE);
//                            videoView.setVideoPath(sliderItem.getUrl());
//                            videoView.start();
//                        } else {
//                            videoView.setVisibility(View.GONE);
//                            Glide.with(viewHolder.itemView)
//                                    .load(sliderItem.getUrl())
//                                    .into(myZoomView);
//                        }
//
//                        dialog.show();
                    }

                    // Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
                }
            });



    }
    private void setVolumeControl(MediaPlayer mp,ImageView ivVolume) {
        ivVolume.setOnClickListener(v -> {
             muted = !muted;
            if(muted) {
                ivVolume.setImageResource(R.drawable.mute);
                mp.setVolume(0F, 0F);
            } else {
                 ivVolume.setImageResource(R.drawable.volume);
                mp.setVolume(1F, 1F);
            }

            mp.setLooping(true);
            mp.start();
        });

        if(muted) {
            ivVolume.setImageResource(R.drawable.mute);
            mp.setVolume(0F, 0F);
        } else {
            ivVolume.setImageResource(R.drawable.volume);
            mp.setVolume(1F, 1F);
        }
        mp.setLooping(true);
        mp.start();
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageView, ivPlay,ivVolume;
        VideoView videoView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            videoView = itemView.findViewById(R.id.videoView);
            ivVolume = itemView.findViewById(R.id.ivVolume);
            this.itemView = itemView;
        }
    }
}
