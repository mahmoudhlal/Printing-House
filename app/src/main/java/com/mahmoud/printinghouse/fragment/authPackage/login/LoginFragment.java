package com.mahmoud.printinghouse.fragment.authPackage.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Constants.Constants;
import com.mahmoud.printinghouse.Utils.ParentClass;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.Utils.Validator;
import com.mahmoud.printinghouse.databinding.FragmentLoginBinding;
import com.mahmoud.printinghouse.fragment.authPackage.OnAuthClick;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralResponse;
import com.mahmoud.printinghouse.models.authResponse.Data;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements OnAuthClick {

    private NavController navController ;
    private FragmentLoginBinding mBinding;
    private LoginViewModel viewModel ;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mBinding.setListener(this);
        setEditText();
        viewModel.getSource().observe(this, userData -> {
            switch (userData.getStatus()) {
                case Loading:
                    ParentClass.showWaiting(getContext());
                    break;
                case Failure:
                    ParentClass.dismissDialog();
                    if ( userData.getError().getMsg()!=null)
                        Toast.makeText(getContext(), userData.getError().getMsg(), Toast.LENGTH_SHORT).show();
                    else
                        ParentClass.handleException(getContext(),userData.getError().getThrowable());
                    break;
                case Success:
                    ParentClass.dismissDialog();
                    SharedPrefManager.getInstance(getContext()).setUserData(userData.getData());
                    SharedPrefManager.getInstance(getContext()).setLoginStatus(true);
                    Constants.CURRENT_ROLE=userData.getData().getRole();
                    openHome(Constants.CURRENT_ROLE);
                    break;
            }
        });


    }

    private void setEditText() {
        mBinding.inputRegName.setTag(getResources().getString(R.string.email));
        mBinding.inputRegPassword.setTag("password  ");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enter:
                validate();
                break;
            case R.id.register:
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
                break;
        }
    }

    private void validate() {
        EditText[] editTexts = new EditText[]{
                mBinding.inputRegName,
                mBinding.inputRegPassword};
        if (!Validator.validateInputField(editTexts,getActivity())){
            return;
        }
        viewModel.login(
                mBinding.inputRegName.getText().toString().trim(),
                mBinding.inputRegPassword.getText().toString().trim()
        );
    }

    private void openHome(String role) {
        switch (role) {
            case Constants.ADMIN:
                navController.navigate(R.id.action_loginFragment_to_adminFragment);
                break;
            case Constants.USER:
                navController.navigate(R.id.action_loginFragment_to_homeFragment);
                break;
        }
    }
}
