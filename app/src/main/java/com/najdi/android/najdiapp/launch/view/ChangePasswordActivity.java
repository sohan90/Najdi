package com.najdi.android.najdiapp.launch.view;

import android.os.Bundle;
import android.view.View;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityChangePasswordBinding;
import com.najdi.android.najdiapp.launch.viewmodel.ForgotPasswordViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ChangePasswordActivity extends BaseActivity {
    public static final String EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE = "change_password_screen_type";
    private ForgotPasswordViewModel viewModel;
    ActivityChangePasswordBinding binding;
    private int launchType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        getLaunchTypeFromBundle();
        initUi();
        initViewModel();
        bindViewModel();
        initClickListener();
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initUi() {
        if (launchType == Constants.OtpScreen.CHANGE_PASSWORD_OTP_SCREEN) {
            binding.oldPasswordLyt.setVisibility(View.GONE);
        } else {
            binding.oldPasswordLyt.setVisibility(View.VISIBLE);
        }
    }

    private void getLaunchTypeFromBundle() {
        if (getIntent() != null) {
            launchType = getIntent().getIntExtra(EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE, -1);
        }
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
    }

    private void initClickListener() {
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.save.setOnClickListener(v ->
                viewModel.validatePassword().observe(this, isValid -> {
                    if (isValid) {
                        updatePassword();
                    }
                }));
    }

    private void updatePassword() {
        showProgressDialog();
        String phoneNo = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PHONE_NO_KEY);
        viewModel.forgotUpdate(phoneNo, "", resourProvider.getCountryLang()).observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                DialogUtil.showAlertDialog(this, baseResponse.getData().getMessage(), (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
            }
        });
    }

}
