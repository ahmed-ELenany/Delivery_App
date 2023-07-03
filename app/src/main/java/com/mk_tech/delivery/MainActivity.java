package com.mk_tech.delivery;

import static com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static MainActivity context;
    public static ImageView iconImgView;
    public LinearLayout llToolBar;
    public NavController navController;
    TextView tvTitle;
    ImageView ivBack;
     NavHostFragment navHostFragment;
    RelativeLayout relativeBottomContainer;
    LinearLayout llHome,  llCommission, llProfile;
    ImageView ivHome,  ivCommission, ivUserProfile, ivOffer;
    private ActivityMainBinding binding;

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        context = this;
        if (navController == null) {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            llToolBar = binding.toolBar;
            tvTitle = binding.tvTitle;
            ivBack = binding.ivBack;

            relativeBottomContainer = binding.relativeBottomContainer;
            llHome = binding.llHome;
            llCommission = binding.llCommission;
            llProfile = binding.llProfile;

            ivHome = binding.ivHome;
            ivCommission = binding.ivCommission;
            ivUserProfile = binding.ivUserProfile;

             navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            iconImgView = ivHome;


            llHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTabIcon(ivHome);
                    MainActivity.context.navController.navigate(R.id.navigation_ordersHistoryFragment);

                }
            });

            llCommission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTabIcon(ivCommission);
                    MainActivity.context.navController.navigate(R.id.navigation_OrdersCommissionFragment);

                }
            });

            llProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (SharedPref.Get_IsGuest(getApplicationContext())) {
                        MainActivity.context.navController.navigate(R.id.navigation_login);
                    } else {
                        setTabIcon(ivUserProfile);
                        MainActivity.context.navController.navigate(R.id.navigation_profileDetails);
                    }


                }
            });




        }

    }

    void setTabIcon(ImageView imageView) {

        ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.icon_menu_color);
        ImageViewCompat.setImageTintList(iconImgView, csl);

        iconImgView = imageView;
        ImageViewCompat.setImageTintList(iconImgView, AppCompatResources.getColorStateList(context, R.color.colorPrimary));

    }

    @Override
    public void onResume() {
        super.onResume();
     }

    public void setLayoutStyle(boolean bottomMenuVisible, boolean toolBarVisible, boolean isBack, boolean isFilter, boolean isShare, boolean isCart, int titleResource) {

        relativeBottomContainer.setVisibility(bottomMenuVisible ? View.VISIBLE : View.GONE);
        llToolBar.setVisibility(toolBarVisible ? View.VISIBLE : View.GONE);
        ivBack.setVisibility(isBack ? View.VISIBLE : View.GONE);

        tvTitle.setVisibility(titleResource <= 0 ? View.GONE : View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.popBackStack();
            }
        });
         tvTitle.setText(getString(titleResource));
    }

}