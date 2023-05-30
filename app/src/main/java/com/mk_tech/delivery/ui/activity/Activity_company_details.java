package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.SliderView;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.adapter.ProductAdapter;
import com.mk_tech.delivery.model.Product;

import java.util.ArrayList;
import java.util.List;


public class Activity_company_details extends AppCompatActivity {

    ImageView ivClose, imageStore;
    LinearLayout llFilter,llDepartment,llOffers;
    TextView tv_title, tvNumProducts;
    SliderView slider;
    RecyclerView Rv;
    Skeleton skeleton;
    String viewType = "grid";
    ImageView ivViewLinear, ivViewGrid;
    int page = 1;
    boolean isLoading, haveNextPage;
   public List<Product> dataList = new ArrayList<>();
    ProductAdapter productAdapter;
    LinearLayoutManager mLayoutHomeManager7;
    GridLayoutManager gridLayoutManager;
    NestedScrollView nestedScrollView;
    String search, min, max;
    String price_sort = "";
    int id;
    public BottomSheetDialog bottomSheetDialogDepartment;
public  static Activity_company_details activity_company_details;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        activity_company_details=this;
        ivClose = findViewById(R.id.ivClose);
        llFilter = findViewById(R.id.llFilter);
        llDepartment=findViewById(R.id.llDepartment);
        ivViewLinear = findViewById(R.id.ivViewLinear);
        ivViewGrid = findViewById(R.id.ivViewGrid);
        tv_title = findViewById(R.id.tv_title);
        slider = findViewById(R.id.slider);
        imageStore = findViewById(R.id.imageStore);
        llOffers=  findViewById(R.id.llOffers);
        Rv = findViewById(R.id.Rv);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        skeleton = findViewById(R.id.skeletonLayout);
        tvNumProducts = findViewById(R.id.tvNumProducts);
        skeleton.showSkeleton();
        ImageView ivShare = findViewById(R.id.ivShare);

        mLayoutHomeManager7 = new LinearLayoutManager(getApplicationContext());
        mLayoutHomeManager7.setOrientation(RecyclerView.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        productAdapter = new ProductAdapter(getApplicationContext(), dataList, "large",false);
        Rv.setAdapter(productAdapter);



        llOffers.setVisibility(View.VISIBLE);


        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Wire store , Hey check out  companies: https://wiregcc.com/companies");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        skeleton.showSkeleton();
       // getCompaniesDetails(id);


    }

//    public void getCompaniesDetails(int id) {
//        UtilsClass.init_Progress_Dialog(Activity_company_details.this);
//        UtilsClass.init_AlertDialog(Activity_company_details.this);
//        //  UtilsClass.Show_Progress_Dialog();
//        isLoading = true;
//
//        try {
//            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
//                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
//                Subscription mSubscription = apiService.CompaniesFilter(id, search, min, max, price_sort, page)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new Subscriber<CompanyDetailsModel>() {
//                            @Override
//                            public void onCompleted() {
//                                isLoading = false;
//                                UtilsClass.Dismiss_Progress_Dialog();
//                                if(categoryList==null || categoryList.size()<=0){
//                                    getCompaniesCategory(id);
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                e.printStackTrace();
//                                UtilsClass.Dismiss_Progress_Dialog();
//
//                            }
//
//                            @Override
//                            public void onNext(CompanyDetailsModel value) {
//                                if (value.getSuccess()) {
//                                    dataList.addAll(value.getData().getProducts());
//                                    haveNextPage = !value.getPaginate().getNext_page_url().isEmpty();
//
//                                    slider.setSliderAdapter(new SliderAdapter(getApplication(), slider, value.getData().getSliders(), 1));
//                                    slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
//                                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                                    slider.startAutoCycle();
//
//                                    tv_title.setText(value.getData().getCompany().getName());
//                                    tvNumProducts.setText(value.getData().getCompany().getProducts_count() + " " + getString(R.string.Products));
//                                    Glide.with(getApplication()).applyDefaultRequestOptions(
//                                            new RequestOptions()
//                                                    .placeholder(R.drawable.picture)
//                                                    .error(R.drawable.picture)
//
//                                    ).load(value.getData().getCompany().getProfile()).into(imageStore);
//
//                                    setView();
//
//                                    skeleton.showOriginal();
//
//                                } else {
//                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());
//
//                                }
//
//                            }
//                        });
//
//
//            } else {
//                UtilsClass.Dismiss_Progress_Dialog();
//                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
//                isLoading = false;
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            UtilsClass.Dismiss_Progress_Dialog();
//            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
//            isLoading = false;
//
//
//        }
//
//    }


}
