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
    TextView tvTitle, tvCartProductsNum;
    ImageView ivBack, ivFilter, ivShare;
    RelativeLayout RlCart;
    NavHostFragment navHostFragment;
    RelativeLayout relativeBottomContainer;
    LinearLayout llHome, llLove, llNotifications, llProfile, llOffer;
    ImageView ivHome, ivLove, ivNotifications, ivUserProfile, ivOffer;
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
            ivFilter = binding.ivFilter;
            ivShare = binding.ivShare;
            RlCart = binding.RlCart;
            relativeBottomContainer = binding.relativeBottomContainer;
            llHome = binding.llHome;
            llLove = binding.llLove;
            llNotifications = binding.llNotifications;
            llProfile = binding.llProfile;
            llOffer = binding.llOffer;
            tvCartProductsNum = binding.tvCartProductsNum;
            ivHome = binding.ivHome;
            ivLove = binding.ivLove;
            ivNotifications = binding.ivNotifications;
            ivUserProfile = binding.ivUserProfile;
            ivOffer = binding.ivOffer;
            navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            iconImgView = ivHome;


            llHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTabIcon(ivHome);
                    MainActivity.context.navController.navigate(R.id.navigation_home);

                }
            });

            llNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTabIcon(ivNotifications);
                    MainActivity.context.navController.navigate(R.id.navigation_notification);

                }
            });

            llProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (SharedPref.Get_IsGuest(getApplicationContext())) {
                        MainActivity.context.navController.navigate(R.id.navigation_login);
                    } else {
                        setTabIcon(ivUserProfile);
                        MainActivity.context.navController.navigate(R.id.navigation_profile);
                    }


                }
            });

            llOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTabIcon(ivOffer);
                    MainActivity.context.navController.navigate(R.id.navigation_offer);
                }
            });

            RlCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.context.navController.navigate(R.id.navigation_cart);

                }
            });
        }

    }

    void setTabIcon(ImageView imageView) {
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) iconImgView.getLayoutParams();
        params1.width = dpToPx(32);
        params1.height = dpToPx(32);
        params1.topMargin = dpToPx(23);
        iconImgView.setLayoutParams(params1);
        iconImgView.setPadding(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3));
        iconImgView.setBackgroundResource(R.drawable.round_second);
        ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.colorPrimary);
        ImageViewCompat.setImageTintList(iconImgView, csl);

        iconImgView = imageView;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iconImgView.getLayoutParams();
        params.width = dpToPx(55);
        params.height = dpToPx(55);
        params.topMargin = 0;
        iconImgView.setLayoutParams(params);
        iconImgView.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        iconImgView.setBackgroundResource(R.drawable.round_primary_dark);
        ImageViewCompat.setImageTintList(iconImgView, AppCompatResources.getColorStateList(context, R.color.text_primary));

    }

    @Override
    public void onResume() {
        super.onResume();
        tvCartProductsNum.setText(SharedPref.Get_user_data(getApplicationContext(), "CART_PRODUCTS_NUM"));
    }

    public void setLayoutStyle(boolean bottomMenuVisible, boolean toolBarVisible, boolean isBack, boolean isFilter, boolean isShare, boolean isCart, int titleResource) {

        relativeBottomContainer.setVisibility(bottomMenuVisible ? View.VISIBLE : View.GONE);
        llToolBar.setVisibility(toolBarVisible ? View.VISIBLE : View.GONE);
        ivBack.setVisibility(isBack ? View.VISIBLE : View.GONE);
        ivFilter.setVisibility(isFilter ? View.VISIBLE : View.GONE);
        RlCart.setVisibility(isCart ? View.VISIBLE : View.GONE);
        ivShare.setVisibility(isShare ? View.VISIBLE : View.GONE);
        tvTitle.setVisibility(titleResource <= 0 ? View.GONE : View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.popBackStack();
            }
        });
        tvCartProductsNum.setText(SharedPref.Get_user_data(getApplicationContext(), "CART_PRODUCTS_NUM"));
        tvTitle.setText(getString(titleResource));
    }

}