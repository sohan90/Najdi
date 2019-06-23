package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
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
        binding.signIn.setOnClickListener(v -> launchLoginScreen());
    }

    private void launchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void subscribeValidationStatus() {
        viewModel.getValidationStatus().observe(this, valid -> {
            if (valid) {
                LiveData<SignupResponseModel> liveData = viewModel.registerUser();
                liveData.observe(this, signupResponseModel -> {
                    hideProgressDialog();
                    if (signupResponseModel != null) {
                        saveUserCredential(signupResponseModel);
                        launchOTPScreen();
                        finish();

                    }
                });
            } else {
                hideProgressDialog();
            }
        });
    }

    private void launchOTPScreen() {
        Intent intent = new Intent(this, OtpActivity.class);
        startActivity(intent);
    }

    private void saveUserCredential(SignupResponseModel signupResponseModel) {
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_PHONE_NO_KEY,
                signupResponseModel.getBilling().getPhone());
        PreferenceUtils.setValueInt(this, PreferenceUtils.USER_ID_KEY, signupResponseModel.getId());
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_EMAIL_KEY,
                signupResponseModel.getEmail());
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_NAME_KEY,
                signupResponseModel.getUsername());

    }
}
