package com.mk_tech.delivery.ui.activity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.PaginationScrollListener;
import com.mk_tech.delivery.Utilities.RecyclerItemClickListener;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.adapter.ProductAdapter;
import com.mk_tech.delivery.model.Product;
import com.mk_tech.delivery.model.ProductsModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityProducts extends AppCompatActivity {

    public static ActivityProducts context;
    RecyclerView RV;
    TextView tvTitle;
    ImageView ivBack;
    String type = "";
    GridLayoutManager gridLayoutManager;
    ProductAdapter productAdapter;
    List<Product> dataList = new ArrayList<>();
    int page = 1;
    boolean isLoading, haveNextPage;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        context = this;
        RV = findViewById(R.id.Rv);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i = getIntent();
        String type = i.getStringExtra("type");
        String title = i.getStringExtra("title");
        tvTitle.setText(title);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        productAdapter = new ProductAdapter(getApplicationContext(), dataList, "large",true);
        RV.setLayoutManager(gridLayoutManager);
        RV.setAdapter(productAdapter);
        RV.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),(view, position) -> {
            if(position<dataList.size()) {
                startActivity(new Intent(getApplicationContext(), Activity_product_details.class).putExtra("id", dataList.get(position).getId()).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        }));
        RV.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (!isLoading && haveNextPage) {
                    page++;
                    getProducts(type);
                }
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });

        getProducts(type);

    }

    public void getProducts(String type) {
        try {
            UtilsClass.init_Progress_Dialog(ActivityProducts.this);
            UtilsClass.Show_Progress_Dialog();
            isLoading = true;
            if (NetworkUtil.isNetworkAvaliable(getApplicationContext())) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(getApplicationContext()), SharedPref.Get_lan(getApplicationContext()));
                Subscription mSubscription = apiService.getProducts(type, page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ProductsModel>() {
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
                            public void onNext(ProductsModel value) {
                                isLoading = false;
                                if (value.getSuccess()) {
                                    dataList.addAll(value.getData());
                                    haveNextPage = value.getPaginate().getHas_pages();
                                    productAdapter.notifyDataSetChanged();

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


}