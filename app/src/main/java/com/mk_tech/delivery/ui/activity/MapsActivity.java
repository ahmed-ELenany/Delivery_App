package com.mk_tech.delivery.ui.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.LocaleHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE = 101;
    public static Location currentLocation;
    public static MapsActivity context;
     TextView btn_ok;
    ImageView ivBack;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;
        ivBack = findViewById(R.id.ivBack);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
         fetchLocation();
    }

    private void fetchLocation() {



                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MapsActivity.this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng;
        if (currentLocation.getLatitude() <= 0) {
            latLng = new LatLng(29.295577, 47.943492);

        } else {
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                currentLocation = new Location(LocationManager.GPS_PROVIDER);
                currentLocation.setLatitude(latLng.latitude);
                currentLocation.setLongitude(latLng.longitude);
                googleMap.addMarker(new MarkerOptions()
                        .title("My address location")
                        .position(latLng));

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }
}