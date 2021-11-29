package com.example.goalog;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * MapActivity
 * place the marker of init location
 * get latitude and longitude data
 * ask the permission of location in app
 */
public class MapActivity extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;

    FusedLocationProviderClient fusedLocationProviderClient;
    private Button OK_press;
    List<String> permissionList = new ArrayList<>();

    /**
     * set up Map Activity
     * user can choose location at map and return value to last activity
     * @param saveInstanceState This is a previous saved state
     */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_map);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            Bundle bundle;//set the bundle
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);//get the location of the device.
            double longitude1 = location.getLongitude();
            double latitude1 = location.getLatitude();
            bundle = new Bundle();
            bundle.putString("LAT", String.valueOf(latitude1));
            bundle.putString("LOG", String.valueOf(longitude1));
            Fragment fragment = new MapFragment();//init fragment
            fragment.setArguments(bundle);//open fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();//reference from https://www.youtube.com/watch?v=YCFPClPjDIQ
            latitude = findViewById(R.id.latitude_Text);
            longitude = findViewById(R.id.longitude_Text);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                    MapActivity.this
            );

            OK_press = findViewById(R.id.OK);
            OK_press.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //change to new activity
                    Intent intent = new Intent(MapActivity.this, AddHabitEventActivity.class);
                    //send data
                    intent.putExtra("latitude", latitude.getText().toString());
                    intent.putExtra("longitude", longitude.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    /**
     * Use this factory method to create a marker of
     * this fragment using the provided parameters.
     *
     *  String s1
     *  String s2
     */
    public void f1(String s1, String s2){
        latitude=findViewById(R.id.latitude_Text);
        longitude=findViewById(R.id.longitude_Text);//get the location of mapFragment
        latitude.setText(s1);
        longitude.setText(s2);
    }

    /**
     * Given requestCode and return the resultCode
     * @param permissions
     * @param grantResults
     * @param requestCode
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            finish();
                            Toast.makeText(MapActivity.this, "Location permission denied", Toast.LENGTH_SHORT).show();
                        }else{
                            finish();
                            Toast.makeText(MapActivity.this, "Location permissions Granted, add location now!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }
    }
}
