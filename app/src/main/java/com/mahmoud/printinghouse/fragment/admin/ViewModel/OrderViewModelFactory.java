package com.mahmoud.printinghouse.fragment.admin.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mahmoud.printinghouse.fragment.admin.repository.OrderRepository;


public class OrderViewModelFactory implements ViewModelProvider.Factory {

    private OrderRepository repository;

    public OrderViewModelFactory(OrderRepository repository) {
        this.repository = repository;
    }


    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OrderViewModel(repository);
    }
}
