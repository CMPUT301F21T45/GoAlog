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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MapActivity extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;

    FusedLocationProviderClient fusedLocationProviderClient;
    private Button OK_press;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_map);
        Bundle bundle;
        //set the bundle
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //reference from https://www.ibm.com/docs/hr/mpf/7.1.0?topic=permissions-location-services-in-android
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            // Write you code here if permission already given.
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //get the location of the device.
        double longitude1 = location.getLongitude();
        double latitude1 = location.getLatitude();
        bundle = new Bundle();
        bundle.putString("LAT", String.valueOf(latitude1));
        bundle.putString("LOG", String.valueOf(longitude1));
        //init fragment
        Fragment fragment = new MapFragment();
        //open fragment
        fragment.setArguments(bundle);
        ////reference from https://www.youtube.com/watch?v=YCFPClPjDIQ
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
        latitude = findViewById(R.id.latitude_Text);
        longitude = findViewById(R.id.longitude_Text);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                MapActivity.this
        );
        OK_press=findViewById(R.id.OK);
        OK_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change to new activity
                Intent intent=new Intent(MapActivity.this,AddHabitEventActivity.class);
                //send data
                intent.putExtra("latitude",latitude.getText().toString());
                intent.putExtra("longitude",longitude.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });


    }

    public void f1(String s1, String s2){
        //get the location of mapFragment
        latitude=findViewById(R.id.latitude_Text);
        longitude=findViewById(R.id.longitude_Text);
        //set the location
        latitude.setText(s1);
        longitude.setText(s2);
    }


}
