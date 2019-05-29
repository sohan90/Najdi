package com.najdi.android.najdiapp.launch;

import android.os.Bundle;

import com.najdi.android.najdiapp.BaseActivity;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ActivitySignUpBinding;
import com.najdi.android.najdiapp.launch.viewmodel.SignUpViewModel;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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

    private void subscribeValidationStatus() {
        viewModel.getValidationStatus().observe(this, valid -> {
            if (valid) {
                viewModel.registerUser();
            }
        });
    }

    private void setupBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
    }

    private void initClickListener() {
        binding.signUp.setOnClickListener((v) -> viewModel.validate());
    }
}
