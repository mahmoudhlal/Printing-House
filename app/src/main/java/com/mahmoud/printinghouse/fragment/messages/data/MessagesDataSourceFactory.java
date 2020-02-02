package com.mahmoud.printinghouse.fragment.messages.data;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.mahmoud.printinghouse.models.messagesResponse.MessagesItem;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

public class MessagesDataSourceFactory extends DataSource.Factory {

    //creating mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, MessagesItem>>
            itemLiveDataSource=new MutableLiveData<>();
    private String token;
    private MutableLiveData<MessagesKeyedDataSource> mutableLiveData= new MutableLiveData<>();

    public MessagesDataSourceFactory(String token) {
        this.token = token;
    }
    @Override
    public DataSource create() {
        //getting source data object
        MessagesKeyedDataSource itemDataSource=new MessagesKeyedDataSource(token);
        //posting datasource to get values
        itemLiveDataSource.postValue(itemDataSource);
        mutableLiveData.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, MessagesItem>> getSearchItemsLiveDataSource() {
        return itemLiveDataSource;
    }

    public MutableLiveData<MessagesKeyedDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
