package com.mahmoud.printinghouse.fragment.messages;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoud.printinghouse.MainActivity;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.databinding.FragmentMessageBinding;
import com.mahmoud.printinghouse.fragment.admin.Injection;
import com.mahmoud.printinghouse.fragment.admin.ViewModel.OrderViewModel;
import com.mahmoud.printinghouse.fragment.admin.ViewModel.OrderViewModelFactory;
import com.mahmoud.printinghouse.fragment.admin.adapter.OrderAdapter;
import com.mahmoud.printinghouse.fragment.messages.ViewModel.MessagesViewModel;
import com.mahmoud.printinghouse.fragment.messages.ViewModel.MessagesViewModelFactory;
import com.mahmoud.printinghouse.fragment.messages.adapter.MessagesAdapter;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    private static final String TAG = "MessagesFragment";
    private FragmentMessageBinding messageBinding ;
    private NavController navController ;
    private MessagesViewModel viewModel ;
    private MessagesAdapter messagesAdapter ;

    public MessagesFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                new MessagesViewModelFactory(Injection.provideMessagesRepository(getContext())))
                .get(MessagesViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        messageBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_message,container,false);
        return messageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        messageBinding.toolbar.title.setText("Messages");
        messageBinding.toolbar.back.setOnClickListener(x->navController.navigateUp());
        messagesAdapter = new MessagesAdapter();
        messageBinding.recMessages.setAdapter(messagesAdapter);
        viewModel.getMessagesList().observe(this, myListViewModels->messagesAdapter.submitList(myListViewModels));
        /*
         *  When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         *
         * */
        viewModel.getNetworkState().observe(this, networkState->{

            switch (networkState.getStatus()){
                case RUNNING:
                    Log.d(TAG, "onViewCreated: RUNNING");
                case SUCCESS:
                    Log.d(TAG, "onViewCreated: SUCCESS");
                case FAILED:
                    Log.d(TAG, "onViewCreated: FAILED");
            }
            messagesAdapter.setNetworkState(networkState);
        });
    }
}
