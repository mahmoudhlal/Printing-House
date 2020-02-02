package com.mahmoud.printinghouse.fragment.admin;

import android.content.Context;

import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.fragment.admin.repository.OrderRepository;
import com.mahmoud.printinghouse.fragment.admin.repository.OrderRepositoryImpl;
import com.mahmoud.printinghouse.fragment.messages.repository.MessagesRepository;
import com.mahmoud.printinghouse.fragment.messages.repository.MessagesRepositoryImpl;


public class Injection {

    public static OrderRepository provideOrderRepository(Context context) {
        return new OrderRepositoryImpl(provideToken(context));
    }

    public static MessagesRepository provideMessagesRepository(Context context) {
        return new MessagesRepositoryImpl(provideToken(context));
    }


    private static String provideToken(Context context) {
        return SharedPrefManager.getInstance(context).getToken();
    }

}
