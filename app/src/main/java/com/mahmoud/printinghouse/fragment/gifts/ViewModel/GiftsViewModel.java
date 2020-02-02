package com.mahmoud.printinghouse.fragment.gifts.ViewModel;

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

public class GiftsViewModel extends ViewModel {
    private GeneralLiveData<SimpleResponse> liveData = new GeneralLiveData<>();

    public void addGiftOrTshiert(String token, String type, int copies, String address, String lat, String lng, MultipartBody.Part image){
        liveData.postLoading();
        ApiUtils.getUserService().addGiftOrTshirt(token, type,
                String.valueOf(copies), address, lat, lng, image).enqueue(new Callback<SimpleResponse>() {
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
