package com.mahmoud.printinghouse.fragment.admin.data;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

public class OrderDataSourceFactory extends DataSource.Factory {

    //creating mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, OrdersItem>>
            itemLiveDataSource=new MutableLiveData<>();
    private String token;
    private MutableLiveData<OrderKeyedDataSource> mutableLiveData= new MutableLiveData<>();

    public OrderDataSourceFactory(String token) {
        this.token = token;
    }
    @Override
    public DataSource create() {
        //getting source data object
        OrderKeyedDataSource itemDataSource=new OrderKeyedDataSource(token);
        //posting datasource to get values
        itemLiveDataSource.postValue(itemDataSource);
        mutableLiveData.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OrdersItem>> getSearchItemsLiveDataSource() {
        return itemLiveDataSource;
    }

    public MutableLiveData<OrderKeyedDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
