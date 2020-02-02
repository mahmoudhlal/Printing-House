package com.mahmoud.printinghouse.fragment.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoud.printinghouse.MainActivity;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.databinding.FragmentHomeBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnHomeClick {

    private NavController navController ;
    private FragmentHomeBinding mBinding ;
    public HomeFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mBinding.setClick(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.btnFiles:
                navController.navigate(R.id.action_homeFragment_to_filesFragment);
                break;
            case R.id.btnGifts:
                bundle.putString("type" , "gifts");
                navController.navigate(R.id.action_homeFragment_to_giftsFragment,bundle);
                break;
            case R.id.btnTshirts:
                bundle.putString("type" , "tshirts");
                navController.navigate(R.id.action_homeFragment_to_giftsFragment,bundle);
                break;
            case R.id.btnShields:
                navController.navigate(R.id.action_homeFragment_to_shieldsFragment);
                break;
            case R.id.messages:
                navController.navigate(R.id.action_homeFragment_to_messagesFragment);
                break;
            case R.id.logout:
                SharedPrefManager.getInstance(getContext()).Logout();
                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
        }
    }
}
