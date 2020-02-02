package com.mahmoud.printinghouse.fragment.splash;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Constants.Constants;
import com.mahmoud.printinghouse.Utils.ProjectUtils;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private NavController navController;


    public SplashFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).getWindow().clearFlags
                (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loading();
    }

    private void loading() {
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                if (SharedPrefManager.getInstance(getContext()).getLoginStatus())
                    openHomeActivity(Constants.CURRENT_ROLE);
                else
                    openLogin();
            }
        }.start();
    }


    private void openLogin() {
        navController.navigate(R.id.action_splashFragment_to_loginFragment);
    }


    private void openHomeActivity(String role) {
        switch (role) {
            case Constants.ADMIN:
                navController.navigate(R.id.action_splashFragment_to_adminFragment);
                break;
            case Constants.USER:
                navController.navigate(R.id.action_splashFragment_to_homeFragment);
                break;
        }
    }



}
