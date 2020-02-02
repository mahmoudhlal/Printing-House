package com.mahmoud.printinghouse.fragment.messages.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.fragment.messages.data.MessagesDataSourceFactory;
import com.mahmoud.printinghouse.fragment.messages.data.MessagesKeyedDataSource;
import com.mahmoud.printinghouse.models.messagesResponse.MessagesItem;

public class MessagesRepositoryImpl implements MessagesRepository {


    private LiveData<PageKeyedDataSource<Integer, MessagesItem>> liveDataSource;
    LiveData<PagedList<MessagesItem>> itemPagedList;
    private String token ;
    private LiveData<NetworkState> networkState;

    public MessagesRepositoryImpl(String token) {
        this.token = token;
    }

    @Override
    public LiveData<PagedList<MessagesItem>> getMessages() {
        MessagesDataSourceFactory itemDataSourceFactory=new MessagesDataSourceFactory(token);
        networkState = Transformations.switchMap(itemDataSourceFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        //getting live data source from data source library
        liveDataSource=itemDataSourceFactory.getSearchItemsLiveDataSource();
        //getting PagedList config
        PagedList.Config pagedListConfig=(new PagedList.Config.Builder()).
                setEnablePlaceholders(true).
                setPageSize(MessagesKeyedDataSource.PAGE_SIZE).
                build();

        //building the paged list
        itemPagedList=(new LivePagedListBuilder(itemDataSourceFactory,pagedListConfig)).build();
        return itemPagedList;
    }

    @Override
    public LiveData<NetworkState> getNetWorkState() {
        return networkState;
    }

}

