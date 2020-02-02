package com.mahmoud.printinghouse.fragment.authPackage.register;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Constants.Constants;
import com.mahmoud.printinghouse.Utils.ParentClass;
import com.mahmoud.printinghouse.Utils.SharedPrefManager;
import com.mahmoud.printinghouse.Utils.Validator;
import com.mahmoud.printinghouse.databinding.FragmentRegisterBinding;
import com.mahmoud.printinghouse.fragment.authPackage.OnAuthClick;
import com.mahmoud.printinghouse.models.GeneralResponse.GeneralResponse;
import com.mahmoud.printinghouse.models.authResponse.Data;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements OnAuthClick {

    private FragmentRegisterBinding mBinding ;
    private NavController navController;

    private RegisterViewModel registerViewModel ;
    public RegisterFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mBinding.setListener(this);
        setEditText();
        registerViewModel.getSource().observe(this, userData -> {
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
                    Constants.CURRENT_ROLE=Constants.USER;
                    navController.navigate(R.id.action_registerFragment_to_homeFragment);
                    break;
            }
        });
    }

    private void setEditText() {
        mBinding.inputRegName.setTag(getResources().getString(R.string.name));
        mBinding.inputRegEmail.setTag(getResources().getString(R.string.email));
        mBinding.inputRegPhone.setTag(getResources().getString(R.string.phone));
        mBinding.inputRegPassword.setTag(getResources().getString(R.string.password));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                validate();
                break;
            case R.id.login:
                navController.navigateUp();
                break;
        }
    }

    private void validate() {
        EditText[] editTexts = new EditText[]{
                mBinding.inputRegName,
                mBinding.inputRegEmail,
                mBinding.inputRegPhone,
                mBinding.inputRegPassword};
        if (!Validator.validateInputField(editTexts,getActivity())){
            return;
        }
        registerViewModel.register(
                mBinding.inputRegName.getText().toString().trim(),
                mBinding.inputRegEmail.getText().toString().trim(),
                mBinding.inputRegPhone.getText().toString().trim(),
                mBinding.inputRegPassword.getText().toString().trim()
        );
    }
}
