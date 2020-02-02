package com.mahmoud.printinghouse.fragment.admin.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

public interface OrderRepository {
    LiveData<PagedList<OrdersItem>> getOrders();
    LiveData<NetworkState> getNetWorkState();
}