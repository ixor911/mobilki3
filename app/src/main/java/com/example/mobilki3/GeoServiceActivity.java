package com.example.mobilki3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Point;
import android.os.Bundle;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class GeoServiceActivity extends AppCompatActivity implements OnMapReadyCallback {
    private boolean switcher = true;
    private LatLng p1 = null, p2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_service);

        Button returnBtn = findViewById(R.id.returnBtn);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeoServiceActivity.this, MainActivity.class);
                switchActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(switcher) {
                    p1 = latLng;
                    switcher = false;
                } else {
                    p2 = latLng;
                    switcher = true;
                }

                if (p1 != null && p2 != null) {
                    int dist = (int)(SphericalUtil.computeDistanceBetween(p1, p2) / 1000);
                    Toast.makeText(getApplicationContext(), dist + "km", Toast.LENGTH_LONG).show();

                    p1 = null;
                    p2 = null;
                }

            }
        });

    }
    private void switchActivity(Intent intent) {
        try {
            finish();
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cant switch activity", Toast.LENGTH_LONG).show();
        }
    }
}