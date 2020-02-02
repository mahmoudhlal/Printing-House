package com.mahmoud.printinghouse.fragment.messages.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.mahmoud.printinghouse.Remote.ApiUtils;
import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.models.messagesResponse.MessagesItem;
import com.mahmoud.printinghouse.models.messagesResponse.MessagesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesKeyedDataSource extends PageKeyedDataSource<Integer, MessagesItem> {

    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;
    private String token;
    private MutableLiveData networkState;

    MessagesKeyedDataSource(String token) {
        this.token = token;
        networkState = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, MessagesItem> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiUtils.getUserService().getMessages(token,FIRST_PAGE).enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessagesResponse> call, @NonNull Response<MessagesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        callback.onResult(response.body().getData().getMessages(),
                                null, FIRST_PAGE + 1);
                        networkState.postValue(NetworkState.LOADED);
                    }
                }else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessagesResponse> call, @NonNull Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, MessagesItem> callback) {
        /*ApiUtils.getUserService().search(token,local,query,FIRST_PAGE).enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessagesResponse> call, @NonNull Response<MessagesResponse> response) {
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body()!= null){
                    callback.onResult(response.body().getData().getOrdersItems(), adjacentKey);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessagesResponse> call, @NonNull Throwable t) {

            }
        });*/
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, MessagesItem> callback) {
        networkState.postValue(NetworkState.LOADING);
        ApiUtils.getUserService().getMessages(token,params.key).enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessagesResponse> call, @NonNull Response<MessagesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Integer key = response.body().getData().getPaginate().getTotalPages() > params.key
                                ? params.key + 1 : null;
                        callback.onResult(response.body().getData().getMessages(), key);
                        networkState.postValue(NetworkState.LOADED);
                    }
                }else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessagesResponse> call, @NonNull Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }
}
