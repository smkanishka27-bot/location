package com.example.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_CODE = 100;

    private FusedLocationProviderClient fusedLocationClient;

    private MaterialButton btnLocation;
    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnLocation = findViewById(R.id.btnLocation);
        txtStatus = findViewById(R.id.txtStatus);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        btnLocation.setOnClickListener(v -> getLocation());
    }

    private void getLocation() {

        txtStatus.setText("Status: Fetching location...");

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOCATION_PERMISSION_CODE);

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        txtStatus.setText("Status: Location Found");

                        showLocation(location);

                    } else {

                        txtStatus.setText("Status: GPS OFF");

                        showMessage("Please Turn ON GPS");
                    }
                });
    }

    private void showLocation(Location location) {

        String msg =
                "Latitude : " + location.getLatitude()
                        + "\n\nLongitude : " + location.getLongitude();

        new AlertDialog.Builder(this)
                .setTitle("📍 Current Location")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showMessage(String msg) {

        new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {

                getLocation();

            } else {

                txtStatus.setText("Status: Permission Denied");

                showMessage("Location Permission Denied");
            }
        }
    }
}