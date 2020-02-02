package com.mahmoud.printinghouse.fragment.admin;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mahmoud.printinghouse.MainActivity;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Constants.Constants;
import com.mahmoud.printinghouse.Utils.ParentClass;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.databinding.FragmentAdminBinding;
import com.mahmoud.printinghouse.fragment.admin.ViewModel.OrderViewModel;
import com.mahmoud.printinghouse.fragment.admin.ViewModel.OrderViewModelFactory;
import com.mahmoud.printinghouse.fragment.admin.adapter.OrderAdapter;
import com.mahmoud.printinghouse.fragment.admin.sendMessage.CreateMessageDialog;
import com.mahmoud.printinghouse.fragment.admin.sendMessage.SendMessageOnClick;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralResponse;
import com.mahmoud.printinghouse.models.SimpleResponse.SimpleResponse;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment implements SendMessageOnClick {
    private static final String TAG = "AdminFragment";
    private FragmentAdminBinding mBinding ;
    private NavController navController ;
    private OrderViewModel viewModel ;
    private OrderAdapter orderAdapter ;
    private CreateMessageDialog createMessageDialog;
    private int USER_ID , ORDER_ID ;
    public AdminFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParentClass.hideKeyboard(getActivity());
        viewModel = ViewModelProviders.of(this,
                new OrderViewModelFactory(Injection.provideOrderRepository(getContext())))
                .get(OrderViewModel.class);
        viewModel.getSendMessage().observe(this, message -> {
            switch (message.getStatus()) {
                case Loading:
                    createMessageDialog.isLoading(true);
                    break;
                case Failure:
                    createMessageDialog.isLoading(false);
                    if (message.getError().getMsg()!=null)
                        Toast.makeText(getContext(), message.getError().getMsg(), Toast.LENGTH_SHORT).show();
                    else
                        ParentClass.handleException(getContext(),message.getError().getThrowable());
                    break;
                case Success:
                    createMessageDialog.isLoading(false);
                    createMessageDialog.dismiss();
                    Toast.makeText(getContext(), "message has been sent successfully", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mBinding.logout.setOnClickListener(x->{
            SharedPrefManager.getInstance(getContext()).Logout();
            Objects.requireNonNull(getActivity()).finish();
            startActivity(new Intent(getActivity(), MainActivity.class));
        });
        orderAdapter = new OrderAdapter();
        orderAdapter.setOnItemClickListener((view1, model, position) -> {
            switch (view1.getId()){
                case R.id.sendMsg:
                    ORDER_ID = model.getId();
                    USER_ID = Integer.valueOf(model.getUser().getId()) ;
                    inflateSendMessageDialog();
                    break;
                case R.id.img:
                    openAsType(model);
                    break;
            }
        });
        mBinding.printingRec.setAdapter(orderAdapter);
        viewModel.getOrderList().observe(this, myListViewModels->orderAdapter.submitList(myListViewModels));
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
            orderAdapter.setNetworkState(networkState);
        });
    }

    private void inflateSendMessageDialog() {
        createMessageDialog = new CreateMessageDialog();
        createMessageDialog.show(getChildFragmentManager(), "");
        createMessageDialog.setCancelable(true);
        createMessageDialog.setOnClickView(this);
    }

    @Override
    public void onClick(String msg) {
        viewModel.sendMessage(SharedPrefManager.getInstance(getContext()).getToken(),msg,USER_ID,ORDER_ID);
    }

    private void openAsType(OrdersItem item){
        switch (item.getType()){
            case "shields":
                Bundle bundle = new Bundle();
                bundle.putString("URL" , item.getShieldImage());
                navController.navigate(R.id.action_adminFragment_to_webViewFragment,bundle);
                break;
            case "gifts":
            case "tshirts":
                List<String> images = new ArrayList<>();
                images.add(item.getFilePath());
                new ImageViewer.Builder<>(getContext(), images)
                        .setFormatter(s -> s)
                        .setStartPosition(0)
                        .hideStatusBar(false)
                        .allowZooming(true)
                        .allowSwipeToDismiss(true)
                        .show();
                break;
            case "files":
                if(item.getFilePath().endsWith(".png")
                        ||item.getFilePath().endsWith(".jpg")
                        ||item.getFilePath().endsWith(".jpeg")){
                    List<String> imgs = new ArrayList<>();
                    imgs.add(item.getFilePath());
                    new ImageViewer.Builder<>(getContext(), imgs)
                            .setFormatter(s -> s)
                            .setStartPosition(0)
                            .hideStatusBar(false)
                            .allowZooming(true)
                            .allowSwipeToDismiss(true)
                            .show();
                }else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getFilePath()));
                    startActivity(browserIntent);
                }
                break;
        }

    }
}
