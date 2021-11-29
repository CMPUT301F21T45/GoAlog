
package com.example.goalog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * MapFragment
 * Welcome Slogan
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment} factory method to
 * create an instance of this fragment.
 */

public class MapFragment extends Fragment {
    double latitude;
    double longitude;
    GoogleMap mMap;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //init map fragment

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                //reference from https://www.youtube.com/watch?v=YCFPClPjDIQ
                mMap = googleMap;
                //set the googleMap
                String LAT = getArguments().getString("LAT");
                String LON = getArguments().getString("LOG");
                //get the string from mapActivity
                placeMarker(LAT+":"+LON, Double.valueOf(LAT), Double.valueOf(LON));
                MapActivity m1=(MapActivity) getActivity();
                //mark the default location
                m1.f1(LAT,LON);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {

                        //CLICK MAP
                        //INIT map marker
                        MarkerOptions markerOptions=new MarkerOptions();
                        //set position
                        markerOptions.position(latLng);
                        //set title of marker
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                        //return value
                        markerOptions.title(latLng.latitude+":"+ latLng.longitude);
                        //send data
                        String Latitude=String.valueOf(latitude);
                        String Longitude=String.valueOf(longitude);
                        MapActivity m1=(MapActivity) getActivity();
                        //send the data from fragment to activity
                        m1.f1(Latitude,Longitude);
                        //remove
                        googleMap.clear();
                        //animating
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        //add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
            /**
             * Use this factory method to create a marker of
             * this fragment using the provided parameters.
             *
             * @title double lat.
             * @title double lon.
             */
            public void placeMarker(String title, double lat, double lon) {
                //place the mark of the map fragment
                if (mMap != null) {
                    LatLng marker = new LatLng(lat, lon);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
                    //move Camera to selected mark
                    mMap.addMarker(new MarkerOptions().title(title).position(marker));
                    //add the marker to the map fragment
                }
            }
        });

        //return view
        return view;
    }


}