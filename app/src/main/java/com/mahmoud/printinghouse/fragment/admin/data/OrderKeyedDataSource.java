package com.mahmoud.printinghouse.fragment.admin.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;


import com.mahmoud.printinghouse.Remote.ApiUtils;
import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.models.orderResponse.OrderResponse;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderKeyedDataSource extends PageKeyedDataSource<Integer, OrdersItem> {

    private static final String TAG = "OrderKeyedDataSource";
    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;
    private String token;
    private MutableLiveData networkState;

    OrderKeyedDataSource(String token) {
        this.token = token;
        networkState = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, OrdersItem> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiUtils.getUserService().getOrders(token,FIRST_PAGE).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: "+response.body().getData().getOrders().size());
                        callback.onResult(response.body().getData().getOrders(),
                                null, FIRST_PAGE + 1);
                        networkState.postValue(NetworkState.LOADED);
                    }
                }else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, OrdersItem> callback) {
        /*ApiUtils.getUserService().search(token,local,query,FIRST_PAGE).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body()!= null){
                    callback.onResult(response.body().getData().getOrdersItems(), adjacentKey);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {

            }
        });*/
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, OrdersItem> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiUtils.getUserService().getOrders(token,params.key).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Integer key = response.body().getData().getPaginate().getTotalPages() > params.key
                                ? params.key + 1 : null;
                        callback.onResult(response.body().getData().getOrders(), key);
                        networkState.postValue(NetworkState.LOADED);
                    }
                }else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }
}
