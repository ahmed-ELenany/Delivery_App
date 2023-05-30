package com.mk_tech.delivery.Utilities;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mk_tech.delivery.R;

import java.util.Objects;

public class UtilsClass {
    public static ProgressDialog progressDialog;
    public static Dialog alertDialog;

    public static void Show_Toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void Show_Progress_Dialog() {
        try {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                Log.d("dialogType", "show");
            }
        } catch (Exception e) {

        }

    }

    public static void Dismiss_Progress_Dialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            Log.d("dialogType", "dismiss");

        }
    }


    public static void init_Progress_Dialog(Context ctx) {
        progressDialog = new ProgressDialog(ctx, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(ctx.getString(R.string.please_wait));
    }

    public static void init_AlertDialog(Context ctx) {
        alertDialog = new Dialog(ctx);

    }

    public static void Show_AlertDialog(String title, String message) {
        alertDialog.setContentView(R.layout.custom_alert_dialog);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtBody = alertDialog.findViewById(R.id.txtBody);
        TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
        TextView tvYes = alertDialog.findViewById(R.id.tvYes);
        txtTitle.setText(title);
        txtBody.setText(message);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }


    /**
     * This method is used to hide keyboard
     *
     * @param activity
     */
    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        }
    }


    public static void fadeIn(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(3000);

        // Animation fadeOut = new AlphaAnimation(1, 0);
        // fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        // fadeOut.setStartOffset(1000);
        // fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        //animation.addAnimation(fadeOut);
        view.setAnimation(animation);
    }
    /**
     * dip to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
