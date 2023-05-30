package com.mk_tech.delivery.ui.activity;


import static com.mk_tech.delivery.Utilities.SharedPref.USER_EMAIL;
import static com.mk_tech.delivery.ui.activity.MapsActivity.currentLocation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.NetworkUtil;
import com.mk_tech.delivery.Utilities.NothingSelectedSpinnerAdapter;
import com.mk_tech.delivery.Utilities.SharedPref;
import com.mk_tech.delivery.Utilities.UtilsClass;
import com.mk_tech.delivery.model.Address;
import com.mk_tech.delivery.model.AddressModel;
import com.mk_tech.delivery.model.AreaModel;
import com.mk_tech.delivery.model.CityModel;
import com.mk_tech.delivery.networkApis.ApiInterface;
import com.mk_tech.delivery.networkApis.Main_ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Activity_address_add extends AppCompatActivity {
    public static Activity_address_add context;
    public static Address addressModel;
    public List<CityModel.DataModel> cityDataL;
    public List<AreaModel.DataModel> areaDataL;
    ImageView ivBack;
    Button btnSave;
    EditText etBlock, etStreet, etAvenue, etHouseApartmentNo, etPhone, etAddressName, etBuildingNo;
    int cityId = 0, areaId = 0;
    ImageView ivMap;
    Spinner spinner_city, spinner_area;
    String requestType;
    TextView btnDelete;
    boolean updateFirstTime=true;
boolean isSetMapLocation=false;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_address_add_edit);
        context = this;
        UtilsClass.init_AlertDialog(context);
        Intent i = getIntent();
        requestType = i.getStringExtra("requestType");

        ivBack = findViewById(R.id.ivBack);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        spinner_area = findViewById(R.id.spinner_area);
        etAddressName = findViewById(R.id.etAddressName);
        etAvenue = findViewById(R.id.etAvenue);
        spinner_city = findViewById(R.id.spinner_city);
        etBlock = findViewById(R.id.etBlock);
        etStreet = findViewById(R.id.etStreet);
        etHouseApartmentNo = findViewById(R.id.etHouseApartmentNo);
        etPhone = findViewById(R.id.etPhone);
        etBuildingNo = findViewById(R.id.etBuildingNo);
        ivMap = findViewById(R.id.ivMap);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSetMapLocation=true;
                startActivity(new Intent(Activity_address_add.this, MapsActivity.class));
            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cityId = cityDataL.get(position - 1).getId();
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
                    if(requestType!=null && requestType.equals("update") && updateFirstTime){
                        updateFirstTime=false;
                    }else{
                        getLatLongFromAddress("Kuwait "+" "+areaDataL.get(position - 1).getName());

                    }
                    isSetMapLocation=false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (currentLocation == null || currentLocation.getLatitude() < 0 || !isSetMapLocation) {
                    UtilsClass.Show_AlertDialog(getString(R.string.error), getString(R.string.Please_open_map_select_location));

                } else if (etAddressName.getText().toString().isEmpty()
                        || cityId == 0
                        || areaId == 0

                ) {
                    UtilsClass.Show_AlertDialog(getString(R.string.error), getString(R.string.enter_address_name) + ", " + getString(R.string.city) + ", " + getString(R.string.area));

                } else {

                    if (requestType != null && requestType.equals("update")) {
                        updateAddress();
                    } else {
                        addAddress();

                    }
                }


            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog  alertDialog = new Dialog(Activity_address_add.this);

                alertDialog.setContentView(R.layout.custom_alert_dialog);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView txtBody = alertDialog.findViewById(R.id.txtBody);
                TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
                TextView tvYes = alertDialog.findViewById(R.id.tvYes);
                TextView tvNO = alertDialog.findViewById(R.id.tvNo);
                txtTitle.setText(R.string.sure_to_delete);
                txtBody.setText("");
                tvYes.setText(R.string.yes);
                tvNO.setVisibility(View.VISIBLE);
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAddress();
                    }
                });

                tvNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();


            }
        });

        if (requestType != null && requestType.equals("update")) {
            etStreet.setText(addressModel.getStreet());
            etAvenue.setText(addressModel.getAvenue());
            etHouseApartmentNo.setText(addressModel.getApartment_no());
            etPhone.setText(addressModel.getPhone());
            etAddressName.setText(addressModel.getName());
            etBuildingNo.setText(addressModel.getBuilding());
            etBlock.setText(addressModel.getBlock());
            cityId = addressModel.getCity_id();
            areaId = addressModel.getArea_id();
            currentLocation = new Location(LocationManager.GPS_PROVIDER);
            currentLocation.setLatitude(Double.parseDouble(addressModel.getLatitude()));
            currentLocation.setLongitude(Double.parseDouble(addressModel.getLongitude()));
            btnSave.setText(getString(R.string.update));
            btnDelete.setVisibility(View.VISIBLE);

        } else {
            currentLocation = new Location(LocationManager.GPS_PROVIDER);
            currentLocation.setLatitude(0.0);
            currentLocation.setLongitude(0.0);
        }
       // startActivity(new Intent(Activity_address_add.this, MapsActivity.class));
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

    public void addAddress() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.createAddresses(
                                cityId + "", areaId + "", etAddressName.getText().toString()
                                , SharedPref.Get_user_data(getApplicationContext(), USER_EMAIL)
                                , etPhone.getText().toString()
                                , etBlock.getText().toString()
                                , etStreet.getText().toString()
                                , etBuildingNo.getText().toString()
                                , etHouseApartmentNo.getText().toString()
                                , etAvenue.getText().toString()
                                , currentLocation.getLatitude() + ""
                                , currentLocation.getLongitude() + ""
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddressModel>() {
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
                            public void onNext(AddressModel value) {
                                if (value.getSuccess()) {
                                    finish();


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

    public void updateAddress() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.updateAddresses(
                                addressModel.getId()
                                , areaId + ""
                                , etAddressName.getText().toString()
                                , SharedPref.Get_user_data(getApplicationContext(), USER_EMAIL)
                                , etPhone.getText().toString()
                                , etBlock.getText().toString()
                                , etStreet.getText().toString()
                                , etBuildingNo.getText().toString()
                                , etHouseApartmentNo.getText().toString()
                                , etAvenue.getText().toString()
                                , currentLocation.getLatitude() + ""
                                , currentLocation.getLongitude() + "")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddressModel>() {
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
                            public void onNext(AddressModel value) {
                                if (value.getSuccess()) {
                                    finish();
                                    startActivity(new Intent(Activity_address_add.this, Activity_address.class));
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

    public void deleteAddress() {
        UtilsClass.init_Progress_Dialog(this);
        UtilsClass.Show_Progress_Dialog();

        try {
            if (NetworkUtil.isNetworkAvaliable(this)) {
                ApiInterface apiService = new Main_ApiClient().createService(ApiInterface.class, SharedPref.Get_token(this), SharedPref.Get_lan(this));
                Subscription mSubscription = apiService.deleteAddresses(
                                addressModel.getId() )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddressModel>() {
                            @Override
                            public void onCompleted() {

                                UtilsClass.Dismiss_Progress_Dialog();
                                startActivity(new Intent(Activity_address_add.this, Activity_address.class));

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                UtilsClass.Dismiss_Progress_Dialog();

                            }

                            @Override
                            public void onNext(AddressModel value) {
                                if (value.getSuccess()) {
                                    finish();
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

    private void getLatLongFromAddress(String address)
    {
        double lat= 0.0, lng= 0.0;

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<android.location.Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0)
            {
                lat=addresses.get(0).getLatitude();
                lng=addresses.get(0).getLongitude();

                currentLocation.setLatitude(lat);
                currentLocation.setLongitude(lng);

                Log.d("Latitude", ""+lat);
                Log.d("Longitude", ""+lng);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(Activity_address_add.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=mumbai&sensor=false");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
