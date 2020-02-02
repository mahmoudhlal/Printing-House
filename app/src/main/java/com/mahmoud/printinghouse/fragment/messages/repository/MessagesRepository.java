package com.mahmoud.printinghouse.fragment.messages.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.models.messagesResponse.MessagesItem;

public interface MessagesRepository {
    LiveData<PagedList<MessagesItem>> getMessages();
    LiveData<NetworkState> getNetWorkState();
}