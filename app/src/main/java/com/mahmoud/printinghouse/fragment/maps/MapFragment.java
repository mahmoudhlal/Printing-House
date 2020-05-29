package com.mahmoud.printinghouse.fragment.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.ParentClass;
import com.mahmoud.printinghouse.models.location.SharedViewModel;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;


@SuppressLint("ValidFragment")
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    private NavController navController;
    private GoogleMap mMap;
    private double lat, lng;
    private LocationManager mLocationManager;
    private boolean gpsCheck = false;
    private MarkerOptions marker;
    private SharedViewModel locationViewModel ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.btnLocation).setOnClickListener(this);
        //searchBar();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationManager = (LocationManager) Objects.requireNonNull(getActivity()).
                getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else /*if (lat != 0 && lng != 0)*/ {
            getCurrentLocation();
            LatLng myLoc = new LatLng(lat, lng);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLoc)
                    .zoom(12)
                    .bearing(90)
                    .tilt(40)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 10));
            //mMap.addMarker(new MarkerOptions().position(myLoc));
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(() -> {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            getCurrentLocation();
            mMap.setMyLocationEnabled(true);
            LatLng myLoc = new LatLng(lat, lng);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLoc)
                    .zoom(12)
                    .bearing(90)
                    .tilt(40)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 13));
            //mapClicked(myLoc);
            return true;
        });
        //mMap.setOnMapClickListener(this::mapClicked);
    }

    private void buildAlertMessageNoGps() {
        final androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.pleaseOpenGPS))
                .setCancelable(false)
                .setPositiveButton(R.string.open, (dialog, id) -> {
                    gpsCheck = true;
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                })
                .setNegativeButton(R.string.close, (dialog, id) -> dialog.cancel());
        final androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gpsCheck) {
            mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                getCurrentLocation();
                LatLng myLoc = new LatLng(lat, lng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myLoc)
                        .zoom(12)
                        .bearing(90)
                        .tilt(40)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 10));
                //mMap.addMarker(new MarkerOptions().position(myLoc));
            }
        }

    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return bestLocation;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {

                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        Locale locale = new Locale(ParentClass.getLocalization(Objects.requireNonNull(getActivity())));
        geocoder = new Geocoder(getActivity(), locale);
        String address = "";
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException | IndexOutOfBoundsException e) {
            Toast.makeText(getActivity(), getString(R.string.no_detected_address), Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private void mapClicked(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(12)
                .bearing(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        /*marker = new MarkerOptions()
                .position(latLng).title((getAddress(latLng.latitude, latLng.longitude)));


        mMap.clear();
        mMap.addMarker(marker);
        */
        lat = latLng.latitude;
        lng = latLng.longitude;
    }


    private void getCurrentLocation() {
        mLocationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = getLastKnownLocation();

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100,
                    0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationManager.removeUpdates(this);
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void searchBar() {
       /* if (!Places.isInitialized()) {
            Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        Objects.requireNonNull(autocompleteFragment).setPlaceFields(Arrays.asList(Place.Field.ADDRESS,
                Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mapClicked(place.getLatLng());
                Log.d("OPOP" , place.getName()+"\n"+place.getAddress()) ;
            }
            @Override
            public void onError(@NonNull Status status) {

            }
        });
*/
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLocation) {
            //if (lat == 0 && lng == 0)
            markerPicker();
            com.mahmoud.printinghouse.models.location.Location l =
                    new com.mahmoud.printinghouse.models.location.Location(getAddress(lat, lng), lat, lng);
            locationViewModel.locationLiveData.setValue(l);
            navController.navigateUp();
        }
    }

    private void markerPicker(){
        LatLng latLng = mMap.getCameraPosition().target ;
        lat = latLng.latitude ;
        lng = latLng.longitude ;
    }

}
