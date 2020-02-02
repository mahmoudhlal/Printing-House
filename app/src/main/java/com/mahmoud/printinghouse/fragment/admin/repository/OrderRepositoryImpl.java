package com.mahmoud.printinghouse.fragment.admin.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.fragment.admin.data.OrderDataSourceFactory;
import com.mahmoud.printinghouse.fragment.admin.data.OrderKeyedDataSource;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

public class OrderRepositoryImpl implements OrderRepository {


    private LiveData<PageKeyedDataSource<Integer, OrdersItem>> liveDataSource;
    LiveData<PagedList<OrdersItem>> itemPagedList;
    private String token ;
    private LiveData<NetworkState> networkState;

    public OrderRepositoryImpl(String token) {
        this.token = token;
    }

    @Override
    public LiveData<PagedList<OrdersItem>> getOrders() {
        OrderDataSourceFactory itemDataSourceFactory=new OrderDataSourceFactory(token);
        networkState = Transformations.switchMap(itemDataSourceFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        //getting live data source from data source library
        liveDataSource=itemDataSourceFactory.getSearchItemsLiveDataSource();
        //getting PagedList config
        PagedList.Config pagedListConfig=(new PagedList.Config.Builder()).
                setEnablePlaceholders(true).
                setPageSize(OrderKeyedDataSource.PAGE_SIZE).
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

