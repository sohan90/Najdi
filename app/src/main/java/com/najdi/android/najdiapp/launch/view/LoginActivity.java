package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ActivityLoginBinding;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.launch.viewmodel.LoginViewModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initViewModel();
        binding.setViewModel(viewModel);
        setupBinding();
        initClickListener();
        subscribeForInputValidation();
    }

    private void setupBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    private void initClickListener() {
        binding.logIn.setOnClickListener(this);
        binding.guestLogin.setOnClickListener(this);
        binding.signUp.setOnClickListener(this);
    }

    private void launchSignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_in:
                viewModel.validate();
                break;

            case R.id.guest_login:
                launchHomeScreen();
                break;

            case R.id.sign_up:
                launchSignupActivity();
                break;
        }
    }

    private void subscribeForInputValidation() {
        viewModel.getValidationStatus().observe(this, isValid -> {
            if (isValid) {
                showProgressDialog();
                login();
            }
        });
    }

    private void login() {
        viewModel.login().observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.getData() != null) {
                BaseResponse.Data data = baseResponse.getData();
                saveCredential(data);
                launchHomeScreen();
                finish();
            }
        });
    }

    private void saveCredential(BaseResponse.Data data) {
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN, data.getToken());
        PreferenceUtils.setValueInt(this, PreferenceUtils.USER_ID_KEY, Integer.parseInt(data.getUserId()));
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_EMAIL_KEY,
                data.getUserEmail());
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_NAME_KEY,
                data.getUserNicename());
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

}
