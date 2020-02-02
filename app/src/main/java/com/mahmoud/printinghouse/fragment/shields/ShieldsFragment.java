package com.mahmoud.printinghouse.fragment.shields;


import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Constants.Constants;
import com.mahmoud.printinghouse.Utils.ParentClass;
import com.mahmoud.printinghouse.Utils.Permissions;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.Utils.TakePhotoUtils;
import com.mahmoud.printinghouse.databinding.FragmentShieldsBinding;
import com.mahmoud.printinghouse.fragment.shields.ViewModel.ShieldsViewModel;
import com.mahmoud.printinghouse.models.local.ShieldType;
import com.mahmoud.printinghouse.models.location.Location;
import com.mahmoud.printinghouse.models.location.SharedViewModel;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShieldsFragment extends Fragment implements ShieldsClick, OnMapReadyCallback {
    private SharedViewModel locationViewModel ;
    private ShieldsViewModel viewModel ;
    private NavController navController ;
    private FragmentShieldsBinding mBinding ;
    private String SelectedURL ;
    private Location location ;
    private ShieldType shieldType ;
    private GoogleMap mMap;

    public ShieldsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        viewModel = ViewModelProviders.of(this).get(ShieldsViewModel.class);
        viewModel.getLiveData().observe(this,add->{
            switch (add.getStatus()) {
                case Loading:
                    ParentClass.showWaiting(getContext());
                    break;
                case Failure:
                    ParentClass.dismissDialog();
                    if (add.getError().getMsg()!=null)
                        Toast.makeText(getContext(), add.getError().getMsg(), Toast.LENGTH_SHORT).show();
                    else
                        ParentClass.handleException(getContext(),add.getError().getThrowable());
                    break;
                case Success:
                    ParentClass.dismissDialog();
                    Toast.makeText(getContext(), "Your request has been sent successfully", Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                    break;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_shields,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(Objects.requireNonNull(mapFragment).getView()).setClickable(true);
        mapFragment.getMapAsync(this);
        mBinding.toolbar.title.setText("Shield Printing");
        mBinding.toolbar.back.setOnClickListener(x->navController.navigateUp());
        mBinding.setClick(this);
        locationViewModel.getLocationLiveData().observe(this, location -> {
            this.location = location ;
            mBinding.setAddress(location.address);
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.big_spinner_item, Constants.shields);
        ParentClass.makeSpinner(arrayAdapter, mBinding.sheildsTypeSpin, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((ShieldType)adapterView.getItemAtPosition(i)).title.equals("Shields")) {
                    mBinding.txtShow.setVisibility(View.GONE);
                    mBinding.txtShield.setVisibility(View.GONE);
                    mBinding.imgShield.setVisibility(View.GONE);
                }else {
                    mBinding.txtShow.setVisibility(View.VISIBLE);
                    mBinding.txtShield.setVisibility(View.VISIBLE);
                    mBinding.imgShield.setVisibility(View.VISIBLE);
                    mBinding.txtShield.setText(((ShieldType)adapterView.getItemAtPosition(i)).title);
                    mBinding.imgShield.setImageResource(((ShieldType)adapterView.getItemAtPosition(i)).imgId);
                }
                SelectedURL = ((ShieldType)adapterView.getItemAtPosition(i)).url;
                shieldType = ((ShieldType)adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtAddress:
                if (Permissions.getInstance().check_Location_Permission(getActivity(),this))
                    navController.navigate(R.id.action_shieldsFragment_to_mapFragment);
                break;
            case R.id.txtShow:
                Bundle bundle = new Bundle();
                bundle.putString("URL" , SelectedURL);
                navController.navigate(R.id.action_shieldsFragment_to_webViewFragment,bundle);
                break;
            case R.id.btnSend:
                validate();
                break;
            case R.id.imgShield:
                /*List<String> images = new ArrayList<>();
                images.add(item.getFilePath());
                new ImageViewer.Builder<>(getContext(), images)
                        .setFormatter(s -> s)
                        .setStartPosition(0)
                        .hideStatusBar(false)
                        .allowZooming(true)
                        .allowSwipeToDismiss(true)
                        .show();*/
                break;
        }
    }

    private void validate() {
        if (TextUtils.isEmpty(SelectedURL)){
            Toast.makeText(getContext(), "Select shield", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if (!mBinding.radioHonor.isChecked() && !mBinding.radioThanks.isChecked()){
            Toast.makeText(getContext(), "Select formula", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (location == null){
            Toast.makeText(getContext(), "add your address", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.addShield(SharedPrefManager.getInstance(getContext()).getToken(),"shields",
                "honor",shieldType.url,shieldType.title,
                location.address,String.valueOf(location.lat),
                String.valueOf(location.lat));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_ACCESS_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navController.navigate(R.id.action_shieldsFragment_to_mapFragment);
            }
            else Toast.makeText(getActivity(),getResources().getString(R.string.you_must_give_permissions_location), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap =googleMap;
        addMarker(30.0596185,31.3285053,"");
        locationViewModel.getLocationLiveData().observe(this, location -> {
            this.location = location ;
            addMarker(location.lat,location.lng,location.address);
            //mBinding.setAddress(location.address);
        });
    }

    private void addMarker(double lat, double lng,String title) {
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(title));
        marker.showInfoWindow();
    }


}
