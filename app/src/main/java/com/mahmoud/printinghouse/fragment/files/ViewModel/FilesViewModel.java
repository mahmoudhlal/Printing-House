package com.mahmoud.printinghouse.fragment.files.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.mahmoud.printinghouse.Remote.ApiUtils;
import com.mahmoud.printinghouse.models.GeneralResponse.Error;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralLiveData;
import com.mahmoud.printinghouse.models.SimpleResponse.SimpleResponse;

import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilesViewModel extends ViewModel {
    private GeneralLiveData<SimpleResponse> liveData = new GeneralLiveData<>();

    public void addFile(String token, String type, int copies, String address,
                        String lat, String lng, String paperSize,String paperType,MultipartBody.Part image){
        liveData.postLoading();
        ApiUtils.getUserService().addFile(token, type,
                String.valueOf(copies), address, lat, lng,paperSize,paperType,"printType",image)
                .enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response) {
                if (response.isSuccessful()){
                    if (Objects.requireNonNull(response.body()).isStatus()){
                        liveData.postSuccess(response.body());
                    }else {
                        liveData.postError(new Error(response.body().getMsg(),null,response.code(),true));
                    }
                }else {
                    liveData.postError(new Error(response.message(),null,response.code(),true));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SimpleResponse> call, @NonNull Throwable t) {
                liveData.postError(new Error(t.getMessage(),t,null,true));
            }
        });
    }

    public GeneralLiveData<SimpleResponse> getLiveData(){return liveData;}
}
