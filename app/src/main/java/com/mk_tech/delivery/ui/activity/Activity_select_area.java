package com.mk_tech.delivery.ui.activity;


import static com.mk_tech.delivery.Utilities.SharedPref.Save_area_city;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.NothingSelectedSpinnerAdapter;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.AreaModel;
import com.mk_tech.delivery.model.CityModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_select_area extends AppCompatActivity {
    public List<CityModel.DataModel> cityDataL;
    public List<AreaModel.DataModel> areaDataL;
    ImageView ivBack;
    Button btnSave;
    int cityId = 0, areaId = 0;
    Spinner spinner_city, spinner_area;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        UtilsClass.init_AlertDialog(Activity_select_area.this);
        if (!SharedPref.Get_user_data(getApplicationContext(), SharedPref.CITY_ID).isEmpty() && !SharedPref.Get_user_data(getApplicationContext(), SharedPref.AREA_ID).isEmpty()) {
            cityId = Integer.parseInt(SharedPref.Get_user_data(getApplicationContext(), SharedPref.CITY_ID));
            areaId = Integer.parseInt(SharedPref.Get_user_data(getApplicationContext(), SharedPref.AREA_ID));

        }
        ivBack = findViewById(R.id.ivBack);
        btnSave = findViewById(R.id.btnSave);
        spinner_area = findViewById(R.id.spinner_area);
        spinner_city = findViewById(R.id.spinner_city);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cityId = cityDataL.get(position - 1).getId();
                    Save_area_city(getApplicationContext(), SharedPref.CITY_ID, cityId + "");
                    Save_area_city(getApplicationContext(), SharedPref.CITY, cityDataL.get(position - 1).getName());
                    getArea();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    areaId = areaDataL.get(position - 1).getId();
                    Save_area_city(getApplicationContext(), SharedPref.AREA_ID, areaId + "");
                    Save_area_city(getApplicationContext(), SharedPref.AREA, areaDataL.get(position - 1).getName());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getCity();
    }

    public void getCity() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.getCities()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<CityModel>() {
                            @Override
                            public void onCompleted() {
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onNext(CityModel value) {
                                if (value.getSuccess()) {
                                    cityDataL = value.getData();
                                    int index = -1;
                                    List<String> listData = new ArrayList<>();
                                    for (int i = 0; i < cityDataL.size(); i++) {
                                        listData.add(cityDataL.get(i).getName());
                                        if (cityDataL.get(i).getId() == cityId) {
                                            index = i;
                                        }
                                    }
                                    //Creating the ArrayAdapter instance having the country list
                                    ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, listData);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_city.setPrompt(getString(R.string.city));
                                    spinner_city.setAdapter(new NothingSelectedSpinnerAdapter(
                                            aa,
                                            R.layout.government_spinner_row_nothing_selected,
                                            getApplicationContext()));
                                    spinner_city.setSelection(index + 1);


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

    public void getArea() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, "", SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.getAreas(cityId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AreaModel>() {
                            @Override
                            public void onCompleted() {

                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onNext(AreaModel value) {
                                if (value.getSuccess()) {
                                    areaDataL = value.getData();
                                    int index = -1;
                                    List<String> listData = new ArrayList<>();
                                    for (int i = 0; i < areaDataL.size(); i++) {
                                        listData.add(areaDataL.get(i).getName());
                                        if (areaDataL.get(i).getId() == areaId) {
                                            index = i;
                                        }
                                    }
                                    //Creating the ArrayAdapter instance having the country list
                                    ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, listData);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_area.setPrompt(getString(R.string.area));
                                    spinner_area.setAdapter(new NothingSelectedSpinnerAdapter(
                                            aa,
                                            R.layout.area_spinner_row_nothing_selected,
                                            getApplicationContext()));
                                    spinner_area.setSelection(index + 1);


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


}
