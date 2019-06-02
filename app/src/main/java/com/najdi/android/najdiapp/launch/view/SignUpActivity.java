package com.najdi.android.najdiapp.launch.view;

import android.os.Bundle;

import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ActivitySignUpBinding;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;
import com.najdi.android.najdiapp.launch.viewmodel.SignUpViewModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initViewModel();
        setupBinding();
        initClickListener();
        subscribeValidationStatus();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
    }

    private void setupBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initClickListener() {
        binding.signUp.setOnClickListener((v) -> {
            showProgressDialog();
            viewModel.validate();
        });
    }

    private void subscribeValidationStatus() {
        viewModel.getValidationStatus().observe(this, valid -> {
            if (valid) {
                LiveData<SignupResponseModel> liveData = viewModel.registerUser();
                liveData.observe(this, signupResponseModel -> {
                    hideProgressDialog();
                    if (signupResponseModel != null) {
                        saveUserCredential(signupResponseModel);

                    }
                });
            } else {
                hideProgressDialog();
            }
        });
    }

    private void saveUserCredential(SignupResponseModel signupResponseModel) {
        PreferenceUtils.setValueInt(this, PreferenceUtils.USER_ID_KEY, signupResponseModel.getId());
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_EMAIL_KEY,
                signupResponseModel.getEmail());
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_NAME_KEY,
                signupResponseModel.getUsername());

    }
}
