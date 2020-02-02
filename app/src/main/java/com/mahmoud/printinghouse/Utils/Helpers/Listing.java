package com.mahmoud.printinghouse.Utils.Helpers;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Utils.NetworkState;


public class Listing<T> {
    // the LiveData of paged lists for the UI to observe
    public LiveData<PagedList<T>> pagedList ;
    // represents the network request status to show to the user
    public LiveData<NetworkState> networkState;
    // represents the refresh status to show to the user. Separate from network, this
    // value is importantly only when refresh is requested.
    public LiveData<NetworkState> refreshState;
   /* // refreshes the whole data and fetches it from scratch.
    public Unit refresh;
    // retries any failed requests.
    public Unit retry;

    public Listing(LiveData<PagedList<T>> pagedList,
                   LiveData<NetworkState> networkState,
                   LiveData<NetworkState> refreshState,
                   Unit refresh, Unit retry) {
        this.pagedList = pagedList;
        this.networkState = networkState;
        this.refreshState = refreshState;
        this.refresh = refresh;
        this.retry = retry;
    }*/

    public Listing(LiveData<PagedList<T>> pagedList, LiveData<NetworkState> networkState) {
        this.pagedList = pagedList;
        this.networkState = networkState;
    }


}
