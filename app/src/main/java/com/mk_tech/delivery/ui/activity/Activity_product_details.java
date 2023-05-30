package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.core.widget.NestedScrollView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.mk_tech.delivery.MainActivity;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.SliderAdapter;
import com.mk_tech.delivery.model.CartModel;
import com.mk_tech.delivery.model.ProductModel;
import com.mk_tech.delivery.model.SliderModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_product_details extends AppCompatActivity {

    public static Activity_product_details activity_product_details;
    public Double itemPrice = 0.0;

    int quantity = 1;
    ImageView ivClose, ivWish;
    TextView tvAddCard, tvOutOfStoke, tvCompanyName, tv_title, tv_Description, tvPrice, tvQuantityDecrement, tvQuantity, tvQuantityIncrement, tvDiscount;
    SliderView slider;
    LinearLayout llAddToCart;
    ProductModel productData;
    NestedScrollView nestedScrollView;
    LinearLayout llToolBar;
    Skeleton skeleton;
    int scrollY = 0;
    int stock = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        activity_product_details = this;
        nestedScrollView = findViewById(R.id.nestedScrollView);
        ivClose = findViewById(R.id.ivClose);
        tvAddCard = findViewById(R.id.tvAddCard);
        tvOutOfStoke = findViewById(R.id.tvOutOfStoke);
        ivWish = findViewById(R.id.ivWish);
        tv_title = findViewById(R.id.tv_title);
        slider = findViewById(R.id.slider);
        tv_Description = findViewById(R.id.tv_Description);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantityDecrement = findViewById(R.id.tvQuantityDecrement);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvQuantityIncrement = findViewById(R.id.tvQuantityIncrement);
        tvDiscount = findViewById(R.id.tvDiscount);
        llAddToCart = findViewById(R.id.llAddToCart);
        ImageView ivShare = findViewById(R.id.ivShare);
        llToolBar = findViewById(R.id.llToolBar);
        Calendar calendar = Calendar.getInstance();
        skeleton = findViewById(R.id.skeletonLayout);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        skeleton.showSkeleton();
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        tvQuantityIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity < stock) {
                    quantity++;
                    setTotal();
                }

            }
        });

        tvQuantityDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) {
                    quantity--;
                    setTotal();
                }

            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Wire store , Hey check out  products: https://wiregcc.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        ivWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishlistsAddRemove();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartsCreate();


            }
        });

        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);
        if (id != 0) {
            getProductDetails(id);
        }
    }

    public void setTotal() {
        Double additionTotalPrice = 0.0;
        tvQuantity.setText(quantity + "");

        double total = itemPrice + additionTotalPrice;
        tvAddCard.setText(getString(R.string.add_to_cart) + " (" + total * quantity + " " + getString(R.string.currency) + " )");

    }

    public void getProductDetails(int id) {
        UtilsClass.init_Progress_Dialog(Activity_product_details.this);
        UtilsClass.init_AlertDialog(Activity_product_details.this);
        //UtilsClass.Show_Progress_Dialog();
        skeleton.showSkeleton();
        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.getProductDetails(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ProductModel>() {
                            @Override
                            public void onCompleted() {

                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();

                            }

                            @Override
                            public void onNext(ProductModel value) {
                                if (value.getSuccess()) {

                                    productData = value;
                                    List<SliderModel> mSliderItems = new ArrayList<>();
                                    for (int i = 0; i < value.getData().getImages().size(); i++) {
                                        SliderModel sliderModel = new SliderModel();
                                        sliderModel.setUrl(value.getData().getImages().get(i).getUrl());
                                        mSliderItems.add(sliderModel);
                                    }
                                    slider.setSliderAdapter(new SliderAdapter(Activity_product_details.this, slider, mSliderItems, 2));
                                    slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                    slider.startAutoCycle();
                                    stock = value.getData().getStock();
                                    if (stock <= 0) {
                                        llAddToCart.setVisibility(View.GONE);
                                        tvOutOfStoke.setVisibility(View.VISIBLE);
                                    }
                                    tv_title.setText(value.getData().getName());
                                    tvCompanyName.setText(value.getData().getCompany());
                                    tvPrice.setText(value.getData().getNew_price() + " " + getString(R.string.currency));
                                    itemPrice = Double.valueOf(value.getData().getNew_price());
                                    if (value.getData().getFullDescription() != null) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            tv_Description.setText(Html.fromHtml(value.getData().getFullDescription(), Html.FROM_HTML_MODE_COMPACT));
                                        } else {
                                            tv_Description.setText(Html.fromHtml(value.getData().getFullDescription()));
                                        }
                                    } else {
                                        tv_Description.setText("");

                                    }

                                    if (!value.getData().getOld_price().equals("0.000")) {
                                        tvDiscount.setText(value.getData().getOld_price() + " " + getString(R.string.currency));
                                        tvDiscount.setVisibility(View.VISIBLE);

                                    }

                                    if (value.getData().getIs_favoriet()) {
                                        ColorStateList csl = AppCompatResources.getColorStateList(getApplicationContext(), R.color.gold);
                                        ImageViewCompat.setImageTintList(ivWish, csl);
                                    }


                                    setTotal();
                                    skeleton.showOriginal();
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

    public void wishlistsAddRemove() {
        UtilsClass.init_Progress_Dialog(Activity_product_details.this);
        UtilsClass.init_AlertDialog(Activity_product_details.this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.wishlistsAddRemove(productData.getData().getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ProductModel>() {
                            @Override
                            public void onCompleted() {
                                UtilsClass.Dismiss_Progress_Dialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();
                                 startActivity(new Intent(Activity_product_details.this,LoginActivity.class));
                            }

                            @Override
                            public void onNext(ProductModel value) {
                                if (value.getSuccess()) {
                                    UtilsClass.Show_AlertDialog(getString(R.string.wishlist), value.getMessage());
                                    if (value.getMessage().contains("Added") || value.getMessage().contains("اضافة")) {
                                        ColorStateList csl = AppCompatResources.getColorStateList(getApplicationContext(), R.color.gold);
                                        ImageViewCompat.setImageTintList(ivWish, csl);
                                    } else {
                                        ColorStateList csl = AppCompatResources.getColorStateList(getApplicationContext(), R.color.text_primaryDark);
                                        ImageViewCompat.setImageTintList(ivWish, csl);
                                    }
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

    public void cartsCreate() {
        UtilsClass.init_Progress_Dialog(Activity_product_details.this);
        UtilsClass.init_AlertDialog(Activity_product_details.this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.cartsCreate(
                                productData.getData().getId(),
                                quantity, null)
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
                                 startActivity(new Intent(Activity_product_details.this,LoginActivity.class));

                            }

                            @Override
                            public void onNext(CartModel value) {
                                if (value.getSuccess()) {
                                    UtilsClass.Show_AlertDialog(getString(R.string.add_to_cart), getString(R.string.successfully));
                                    finish();
                                    MainActivity.context.navController.navigate(R.id.navigation_cart);
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

    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private final int mImageViewHeight;

        public ScrollPositionObserver() {
            mImageViewHeight = 250;
        }

        @Override
        public void onScrollChanged() {
            scrollY = Math.min(Math.max(nestedScrollView.getScrollY(), 0), mImageViewHeight);

            if (scrollY >= mImageViewHeight) {
                llToolBar.setBackgroundColor(Color.WHITE);

            } else {
                llToolBar.setBackgroundColor(Color.TRANSPARENT);

            }

            // changing position of ImageView
            // slider.setTranslationY((-1 * scrollY+10));

            //  float alpha = mImageViewHeight-(scrollY / (float) mImageViewHeight);
            //   slider.setAlpha(alpha);
        }


    }
}
