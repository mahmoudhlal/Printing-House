package com.mahmoud.printinghouse.fragment.gifts;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mahmoud.printinghouse.databinding.FragmentGiftsBinding;
import com.mahmoud.printinghouse.fragment.gifts.ViewModel.GiftsViewModel;
import com.mahmoud.printinghouse.models.location.Location;
import com.mahmoud.printinghouse.models.location.SharedViewModel;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class GiftsFragment extends Fragment implements GiftsClick , OnMapReadyCallback {

    private FragmentGiftsBinding mBinding ;
    private NavController navController ;
    private SharedViewModel locationViewModel ;
    private Location location = null ;
    private Bitmap photoBitmap;
    private GiftsViewModel viewModel ;
    private  String TYPE ;
    private boolean isGallery ;
    private int pickerValue ;
    private GoogleMap mMap;

    public GiftsFragment() {}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE = getArguments() != null ? getArguments().getString("type") : null;
        locationViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        viewModel = ViewModelProviders.of(this).get(GiftsViewModel.class);
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
                    Log.d("TAG", "onViewCreated: "+add.getError().getMsg());
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_gifts,container,false);
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
        if (photoBitmap!=null) {
            mBinding.image.setPadding(0,0,0,0);
            mBinding.image.setImageBitmap(photoBitmap);
            mBinding.setFileName(TakePhotoUtils.getInstance()
                    .mCurrentPhotoPath.substring(TakePhotoUtils.getInstance()
                            .mCurrentPhotoPath.lastIndexOf("/")+1));
            mBinding.addPhoto.setText("File attached");
        }
        mBinding.numberPickerHorizontal.setValue(pickerValue);
        mBinding.numberPickerHorizontal.setListener(value -> pickerValue=value);
        mBinding.toolbar.title.setText(TYPE.equals("gifts")?"Gift printing":"T shirts printing");
        mBinding.toolbar.back.setOnClickListener(x->navController.navigateUp());
        mBinding.setClick(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addPhoto:
                TakePhotoUtils.getInstance().SelectPhotoDialog(getActivity(),this);
                break;
            case R.id.delete:
                mBinding.addPhoto.setText("Add File");
                mBinding.setFileName("");
                photoBitmap = null ;
                mBinding.image.setImageBitmap(null);
                break;
            case R.id.btnSend:
                validate();
                break;
            case R.id.txtAddress:
                if (Permissions.getInstance().check_Location_Permission(getActivity(),this))
                    navController.navigate(R.id.action_giftsFragment_to_mapFragment);
                break;
        }
    }

    private void validate() {
        if (photoBitmap == null){
            Toast.makeText(getContext(), "add photo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBinding.numberPickerHorizontal.getValue() == 0){
            Toast.makeText(getContext(), "add copies number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location == null){
            Toast.makeText(getContext(), "add your address", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TAG", "validate: "+TYPE+"   "+mBinding.numberPickerHorizontal.getValue()
                +"   "+location.address+"   "+String.valueOf(location.lat)
                +"   "+ String.valueOf(location.lat)+"\n"+SharedPrefManager.getInstance(getContext()).getToken());
        viewModel.addGiftOrTshiert(SharedPrefManager.getInstance(getContext()).getToken(),TYPE,
                mBinding.numberPickerHorizontal.getValue(),location.address,String.valueOf(location.lat),
                String.valueOf(location.lat),TakePhotoUtils.getInstance().getMultiPartFromBitmap(photoBitmap, Objects.requireNonNull(getContext()),"file_path"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GALLERY) {
                assert data != null;
                photoBitmap = TakePhotoUtils.getInstance().setImage(Constants.GALLERY,data.getData(),
                                getActivity(),mBinding.image);
                TakePhotoUtils.getInstance()
                        .mCurrentPhotoPath= Objects.requireNonNull(data.getData()).getPath();
                mBinding.setFileName(
                        Objects.requireNonNull(data.getData().getPath()).
                                substring(data.getData().getPath().lastIndexOf("/")+1));
            }else {
                photoBitmap = TakePhotoUtils.getInstance().setImage(Constants.CAMERA,null,
                        getActivity(),mBinding.image);
                mBinding.setFileName(TakePhotoUtils.getInstance()
                        .mCurrentPhotoPath.substring(TakePhotoUtils.getInstance()
                                .mCurrentPhotoPath.lastIndexOf("/")+1));
            }
            mBinding.addPhoto.setText("File attached");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.permission_read_data) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TakePhotoUtils.getInstance().ChooseImageGallery(Objects.requireNonNull(getActivity()),
                        this);
            }
            else
                Toast.makeText(getActivity(),getResources().getString(R.string.you_must_give_permissions_storage), Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == Constants.permission_camera_code){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    /*&& grantResults[1] == PackageManager.PERMISSION_GRANTED*/) {
                TakePhotoUtils.getInstance().ChooseImageCamera(Objects.requireNonNull(getActivity()),
                        this);
            }
            else
                Toast.makeText(getActivity(), getResources().getString(R.string.you_must_give_permissions_storage), Toast.LENGTH_SHORT).show();
        } else if (requestCode == Constants.PERMISSION_ACCESS_LOCATION){
            for (int grantResult : grantResults)
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.you_must_give_permissions_location), Toast.LENGTH_SHORT).show();
                    return;
                }
            navController.navigate(R.id.action_giftsFragment_to_mapFragment);
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
