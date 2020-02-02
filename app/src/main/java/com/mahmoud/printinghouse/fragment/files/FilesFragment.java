package com.mahmoud.printinghouse.fragment.files;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
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
import com.mahmoud.printinghouse.Utils.PathUtil;
import com.mahmoud.printinghouse.Utils.Permissions;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.Utils.TakePhotoUtils;
import com.mahmoud.printinghouse.databinding.FragmentFilesBinding;
import com.mahmoud.printinghouse.databinding.FragmentGiftsBinding;
import com.mahmoud.printinghouse.fragment.files.ViewModel.FilesViewModel;
import com.mahmoud.printinghouse.fragment.gifts.ViewModel.GiftsViewModel;
import com.mahmoud.printinghouse.models.local.ShieldType;
import com.mahmoud.printinghouse.models.location.Location;
import com.mahmoud.printinghouse.models.location.SharedViewModel;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.mahmoud.printinghouse.Utils.Constants.Constants.PICKFILE_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilesFragment extends Fragment implements FilesClick , OnMapReadyCallback {

    private FragmentFilesBinding mBinding ;
    private NavController navController ;
    private SharedViewModel locationViewModel ;
    private Location location = null ;
    private Bitmap photoBitmap;
    private FilesViewModel viewModel ;
    private Uri fileUri , localUri ;
    private String paperSize="" , paperType=""  ;
    private int pickerValue;
    private GoogleMap mMap;
    private LocationManager mLocationManager;

    public FilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        viewModel = ViewModelProviders.of(this).get(FilesViewModel.class);
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
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_files,container,false);
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
        if (localUri!=null) {
            mBinding.image.setPadding(0,0,0,0);
            generateImageFromPdf(localUri);
            mBinding.setFileName(
                    Objects.requireNonNull(localUri.getPath()).
                            substring(localUri.getPath().lastIndexOf("/")+1));
            mBinding.addFile.setText("File attached");
        }
        mBinding.numberPickerHorizontal.setValue(pickerValue);
        mBinding.numberPickerHorizontal.setListener(value -> pickerValue =value);
        mBinding.toolbar.title.setText("File printing");
        mBinding.toolbar.back.setOnClickListener(x->navController.navigateUp());
        mBinding.setClick(this);

        ArrayAdapter paperSizeAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.big_spinner_item, Constants.PAPER_SIZE);
        ParentClass.makeSpinner(paperSizeAdapter, mBinding.paperSizeSpin, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(adapterView.getItemAtPosition(i)).equals("Paper Size"))
                    paperSize = (adapterView.getItemAtPosition(i)).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        ArrayAdapter paperTypeAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.big_spinner_item, Constants.PAPER_TYPE);
        ParentClass.makeSpinner(paperTypeAdapter, mBinding.paperTypeSpin, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(adapterView.getItemAtPosition(i)).equals("Paper Type"))
                    paperType = (adapterView.getItemAtPosition(i)).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addFile:
                if(Permissions.getInstance()
                        .check_ReadStoragePermission(Objects.requireNonNull(getActivity()),this))
                    getDocumentFile();
                    break;
            case R.id.delete:
                mBinding.setFileName("");
                photoBitmap = null ;
                fileUri = null;
                break;
            case R.id.btnSend:
                validate();
                break;
            case R.id.txtAddress:
                if (Permissions.getInstance().check_Location_Permission(getActivity(),this))
                    navController.navigate(R.id.action_filesFragment_to_mapFragment);
                break;
        }
    }

    private void validate() {
        if (fileUri == null){
            Toast.makeText(getContext(), "add file", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBinding.numberPickerHorizontal.getValue() == 0){
            Toast.makeText(getContext(), "add copies number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(paperSize)){
            Toast.makeText(getContext(), "Select paper size", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(paperType)){
            Toast.makeText(getContext(), "Select paper type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location == null){
            Toast.makeText(getContext(), "add your address", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TAG", "validate: "+convertFileToMultiPart(fileUri));
       viewModel.addFile(SharedPrefManager.getInstance(getContext()).getToken(),"files",
                mBinding.numberPickerHorizontal.getValue(),location.address,String.valueOf(location.lat),
                String.valueOf(location.lat), paperSize,paperType,convertFileToMultiPart(fileUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICKFILE_REQUEST_CODE) {
                assert data != null;
                try {
                    fileUri=Uri.parse(PathUtil.getPath(Objects.requireNonNull(getContext()), data.getData()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                localUri=data.getData();
                if (localUri.getPath().endsWith(".pdf")||localUri.getPath().endsWith(".docx"))
                    generateImageFromPdf(data.getData());
                else
                    photoBitmap = TakePhotoUtils.getInstance().setImage(Constants.GALLERY,data.getData(),
                            getActivity(),mBinding.image);
                mBinding.setFileName(
                        Objects.requireNonNull(localUri.getPath()).
                                substring(localUri.getPath().lastIndexOf("/")+1));
                mBinding.addFile.setText("File attached");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.permission_read_data) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDocumentFile();
            }
            else
                Toast.makeText(getActivity(),getResources().getString(R.string.you_must_give_permissions_storage), Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == Constants.PERMISSION_ACCESS_LOCATION){
            for (int grantResult : grantResults)
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.you_must_give_permissions_location), Toast.LENGTH_SHORT).show();
                    return;
                }
            navController.navigate(R.id.action_filesFragment_to_mapFragment);
        }
    }

    private void getDocumentFile(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    private MultipartBody.Part convertFileToMultiPart(Uri uri){
        File file = new File(uri.toString());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file_path", file.getName(), requestFile);
    }

    private void generateImageFromPdf(Uri pdfUri) {
        Log.e("inn", "generate");
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(Objects.requireNonNull(getContext()));
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getActivity().getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            mBinding.image.setPadding(0,0,0,0);
            mBinding.image.setImageBitmap(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
            Log.e("inn", "generate");
        } catch (Exception e) {
            //todo with exception
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
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
