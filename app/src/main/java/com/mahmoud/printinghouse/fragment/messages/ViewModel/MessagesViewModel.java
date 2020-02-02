package com.mahmoud.printinghouse.fragment.messages.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Remote.ApiUtils;
import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.fragment.messages.repository.MessagesRepository;
import com.mahmoud.printinghouse.models.GeneralResponse.Error;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralLiveData;
import com.mahmoud.printinghouse.models.SimpleResponse.SimpleResponse;
import com.mahmoud.printinghouse.models.messagesResponse.MessagesItem;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesViewModel extends ViewModel {
    private MessagesRepository repository ;

    private GeneralLiveData<SimpleResponse> sendMessage = new GeneralLiveData<>();

    MessagesViewModel(MessagesRepository repository) {
        this.repository = repository;
    }

    private LiveData<PagedList<MessagesItem>> messagesLiveData = new MutableLiveData<>();


    public LiveData<PagedList<MessagesItem>> getMessagesList() {
        return repository.getMessages();
    }

    public LiveData<NetworkState> getNetworkState() {
        return repository.getNetWorkState();
    }

    private void sendMessage(String token,String message , int userId , int orderId ){
        sendMessage.postLoading();
        ApiUtils.getUserService().sendMsg(token,message,String.valueOf(userId),String.valueOf(orderId)).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response) {
                if (response.isSuccessful()){
                    if (Objects.requireNonNull(response.body()).isStatus()){
                        sendMessage.postSuccess(response.body());
                    }else {
                        sendMessage.postError(new Error(response.body().getMsg(),null,response.code(),true));
                    }
                }else {
                    sendMessage.postError(new Error(response.message(),null,response.code(),true));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SimpleResponse> call, @NonNull Throwable t) {
                sendMessage.postError(new Error(t.getMessage(),t,null,true));
            }
        });
    }

    private GeneralLiveData<SimpleResponse> getSendMessage() {
        return sendMessage;
    }
}
