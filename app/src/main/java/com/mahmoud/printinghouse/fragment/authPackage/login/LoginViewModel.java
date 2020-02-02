package com.mahmoud.printinghouse.fragment.authPackage.login;

import androidx.lifecycle.ViewModel;

import com.mahmoud.printinghouse.Remote.ApiUtils;
import com.mahmoud.printinghouse.models.GeneralResponse.Error;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralLiveData;
import com.mahmoud.printinghouse.models.authResponse.AuthResponse;
import com.mahmoud.printinghouse.models.authResponse.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private GeneralLiveData<Data> source = new GeneralLiveData<>();

    public void login(String email,String pass){
        source.postLoading();
        ApiUtils.getUserService().Login(email, pass).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        source.postSuccess(response.body().getData());
                    }else {
                        source.postError(new Error(response.body().getMsg(),null,null,true));
                    }
                }else {
                    source.postError(new Error(response.message(),null,null,true));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                source.postError(new Error(null,t,null,true));
            }
        });
    }

    public GeneralLiveData<Data> getSource() {
        return source;
    }

}
