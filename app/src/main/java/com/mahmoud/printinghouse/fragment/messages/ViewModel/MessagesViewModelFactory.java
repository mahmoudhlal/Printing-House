package com.mahmoud.printinghouse.fragment.messages.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mahmoud.printinghouse.fragment.messages.repository.MessagesRepository;


public class MessagesViewModelFactory implements ViewModelProvider.Factory {

    private MessagesRepository repository;

    public MessagesViewModelFactory(MessagesRepository repository) {
        this.repository = repository;
    }


    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MessagesViewModel(repository);
    }
}
