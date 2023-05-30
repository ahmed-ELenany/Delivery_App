package com.mk_tech.delivery.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.CompanyAdapter;
import com.mk_tech.delivery.adapter.SliderAdapter;
import com.mk_tech.delivery.model.CompaniesModel;
import com.mk_tech.delivery.model.Company;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_companies extends AppCompatActivity {

    ImageView ivClose;
    SliderView slider;
    RecyclerView Rv;
    Skeleton skeleton;
    String viewType = "grid";
    String companyType;
    ImageView ivViewLinear, ivViewGrid;
    int page = 1;
    boolean isLoading, haveNextPage;
    List<Company> dataList = new ArrayList<>();
    CompanyAdapter companyAdapter;
    String layoutType = "small";
    LinearLayoutManager mLayoutHomeManager7;
    GridLayoutManager gridLayoutManager;
    NestedScrollView nestedScrollView;
    int typeCategoriesId = 0, workshopId = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);
        ivClose = findViewById(R.id.ivClose);
        ivViewLinear = findViewById(R.id.ivViewLinear);
        ivViewGrid = findViewById(R.id.ivViewGrid);
        slider = findViewById(R.id.slider);
        Rv = findViewById(R.id.Rv);
        skeleton = findViewById(R.id.skeletonLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        skeleton.showSkeleton();
        ImageView ivShare = findViewById(R.id.ivShare);
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

        ivViewLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewType = "linear";
                setView();
            }
        });

        ivViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewType = "grid";
                setView();
            }
        });



        Intent i = getIntent();
        companyType = i.getStringExtra("type");
        typeCategoriesId = i.getIntExtra("typeCategoriesId", 0);
        workshopId = i.getIntExtra("workshopId", 0);

        mLayoutHomeManager7 = new LinearLayoutManager(getApplicationContext());
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        companyAdapter = new CompanyAdapter(getApplicationContext(), dataList, layoutType, companyType);
        Rv.setAdapter(companyAdapter);

        skeleton.showSkeleton();

        if (typeCategoriesId > 0) {
            getCompaniesByCategory(typeCategoriesId);
        } else if (workshopId > 0) {
            getCompaniesByWorkshop(workshopId);
        } else if (companyType.isEmpty()) {
            getCompanies();
        } else {
            getCompaniesByType(companyType);
        }

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (!isLoading && haveNextPage) {
                        page++;
                        if (typeCategoriesId > 0) {
                            getCompaniesByCategory(typeCategoriesId);
                        } else if (workshopId > 0) {
                            getCompaniesByWorkshop(workshopId);
                        } else if (companyType.isEmpty()) {
                            getCompanies();
                        } else {
                            getCompaniesByType(companyType);
                        }

                    }
                }
            }
        });

    }

    public void getCompanies() {
        UtilsClass.init_Progress_Dialog(Activity_companies.this);
        UtilsClass.init_AlertDialog(Activity_companies.this);
        //  UtilsClass.Show_Progress_Dialog();
        isLoading = true;

        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.getCompanies(page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CompaniesModel>() {
                            @Override
                            public void onCompleted() {
                                isLoading = false;
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();
                                isLoading = false;

                            }

                            @Override
                            public void onNext(CompaniesModel value) {
                                if (value.getSuccess()) {
                                    dataList.addAll(value.getData().getCompanies());
                                    haveNextPage = !value.getPaginate().getNext_page_url().isEmpty();
                                    slider.setSliderAdapter(new SliderAdapter(getApplication(), slider, value.getData().getSliders(), 1));
                                    slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                                    setView();
                                    skeleton.showOriginal();

                                } else {
                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());

                                }

                            }
                        });


            } else {
                UtilsClass.Dismiss_Progress_Dialog();
                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
                isLoading = false;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UtilsClass.Dismiss_Progress_Dialog();
            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
            isLoading = false;


        }

    }

    public void getCompaniesByType(String type) {
        UtilsClass.init_Progress_Dialog(Activity_companies.this);
        UtilsClass.init_AlertDialog(Activity_companies.this);
        //  UtilsClass.Show_Progress_Dialog();
        isLoading = true;

        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.getCompaniesByType(type, page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CompaniesModel>() {
                            @Override
                            public void onCompleted() {
                                isLoading = false;
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();
                                isLoading = false;

                            }

                            @Override
                            public void onNext(CompaniesModel value) {
                                if (value.getSuccess()) {
                                    dataList.addAll(value.getData().getCompanies());
                                    haveNextPage = !value.getPaginate().getNext_page_url().isEmpty();

                                    slider.setSliderAdapter(new SliderAdapter(getApplication(), slider, value.getData().getSliders(), 1));
                                    slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                     setView();
                                    skeleton.showOriginal();

                                } else {
                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());

                                }

                            }
                        });


            } else {
                UtilsClass.Dismiss_Progress_Dialog();
                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
                isLoading = false;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UtilsClass.Dismiss_Progress_Dialog();
            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
            isLoading = false;

        }

    }

    public void getCompaniesByCategory(int id) {
        UtilsClass.init_Progress_Dialog(Activity_companies.this);
        UtilsClass.init_AlertDialog(Activity_companies.this);
        //  UtilsClass.Show_Progress_Dialog();
        isLoading = true;

        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.getCompaniesByCategory(id, page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CompaniesModel>() {
                            @Override
                            public void onCompleted() {
                                isLoading = false;
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();
                                isLoading = false;

                            }

                            @Override
                            public void onNext(CompaniesModel value) {
                                if (value.getSuccess()) {
                                    dataList.addAll(value.getData().getCompanies());
                                    haveNextPage = !value.getPaginate().getNext_page_url().isEmpty();

                                    slider.setSliderAdapter(new SliderAdapter(getApplication(), slider, value.getData().getSliders(), 1));
                                    slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                     setView();
                                    skeleton.showOriginal();

                                } else {
                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());

                                }

                            }
                        });


            } else {
                UtilsClass.Dismiss_Progress_Dialog();
                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
                isLoading = false;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UtilsClass.Dismiss_Progress_Dialog();
            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
            isLoading = false;


        }

    }

    public void getCompaniesByWorkshop(int id) {
        UtilsClass.init_Progress_Dialog(Activity_companies.this);
        UtilsClass.init_AlertDialog(Activity_companies.this);
        //  UtilsClass.Show_Progress_Dialog();
        isLoading = true;

        try {
            if (NetworkUtil.isNetworkAvaliable(getApplication())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplication()), SharedPref.Get_lan(getApplication()));
                Subscription mSubscription = apiService.getCompaniesByWorkshop(id, page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CompaniesModel>() {
                            @Override
                            public void onCompleted() {
                                isLoading = false;
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();
                                isLoading = false;

                            }

                            @Override
                            public void onNext(CompaniesModel value) {
                                if (value.getSuccess()) {
                                    dataList.addAll(value.getData().getCompanies());
                                    haveNextPage = !value.getPaginate().getNext_page_url().isEmpty();

                                    slider.setSliderAdapter(new SliderAdapter(getApplication(), slider, value.getData().getSliders(), 1));
                                    slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                     setView();
                                    skeleton.showOriginal();

                                } else {
                                    UtilsClass.Show_AlertDialog("failed !", value.getMessage());

                                }

                            }
                        });


            } else {
                UtilsClass.Dismiss_Progress_Dialog();
                UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
                isLoading = false;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            UtilsClass.Dismiss_Progress_Dialog();
            UtilsClass.Show_AlertDialog("Error !", "Internet connection failed");
            isLoading = false;


        }

    }

    void setView() {
        if (viewType.equals("linear")) {
            layoutType = "large";
            Rv.setLayoutManager(mLayoutHomeManager7);
//           Rv.addOnScrollListener(new PaginationScrollListener(mLayoutHomeManager7) {
//                @Override
//                protected void loadMoreItems() {
//                    if (!isLoading && haveNextPage){
//                        page++;
//                        if(type.isEmpty()){
//                            getCompanies();
//                        }else{
//                            getCompaniesByType(type);
//                        }
//
//                    }
//                }
//
//                @Override
//                public boolean isLastPage() {
//                    return false;
//                }
//
//                @Override
//                public boolean isLoading() {
//                    return false;
//                }
//            });
            companyAdapter.notifyDataSetChanged();
        } else {
            layoutType = "small";
//            Rv.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
//                @Override
//                protected void loadMoreItems() {
//                    if (!isLoading && haveNextPage){
//                        page++;
//                        if(type.isEmpty()){
//                            getCompanies();
//                        }else{
//                            getCompaniesByType(type);
//                        }
//
//                    }
//                }
//
//                @Override
//                public boolean isLastPage() {
//                    return false;
//                }
//
//                @Override
//                public boolean isLoading() {
//                    return false;
//                }
//            });
            Rv.setLayoutManager(gridLayoutManager);
            companyAdapter.notifyDataSetChanged();

        }

    }
}
