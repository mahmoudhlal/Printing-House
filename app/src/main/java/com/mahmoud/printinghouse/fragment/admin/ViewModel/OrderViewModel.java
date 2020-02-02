package com.mahmoud.printinghouse.fragment.admin.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Remote.ApiUtils;
import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.fragment.admin.repository.OrderRepository;
import com.mahmoud.printinghouse.models.GeneralResponse.Error;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralLiveData;
import com.mahmoud.printinghouse.models.SimpleResponse.SimpleResponse;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private OrderRepository repository ;

    private GeneralLiveData<SimpleResponse> sendMessage = new GeneralLiveData<>();
    OrderViewModel(OrderRepository repository) {
        this.repository = repository;
    }

    private LiveData<PagedList<OrdersItem>> ordersLiveData = new MutableLiveData<>();


    public LiveData<PagedList<OrdersItem>> getOrderList() {
        return repository.getOrders();
    }

    public LiveData<NetworkState> getNetworkState() {
        return repository.getNetWorkState();
    }

    public void sendMessage(String token,String message , int userId , int orderId ){
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

    public GeneralLiveData<SimpleResponse> getSendMessage() {
        return sendMessage;
    }
}
