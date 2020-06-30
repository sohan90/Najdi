package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.ActivityChangePasswordBinding;
import com.najdi.android.najdiapp.launch.viewmodel.ForgotPasswordViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import static com.najdi.android.najdiapp.common.Constants.OtpScreen.OLD_USER_FLOW;
import static com.najdi.android.najdiapp.common.Constants.OtpScreen.RESET_PASSWORD_OTP_SCREEN;

public class ChangePasswordActivity extends BaseActivity {
    public static final String EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE = "change_password_screen_type";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_TOKEN = "extra_token";
    private ForgotPasswordViewModel viewModel;
    private ActivityChangePasswordBinding binding;
    private int launchType;
    private String token;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        getLaunchTypeFromBundle();
        initViewModel();
        bindViewModel();
        initUi();
        initClickListener();
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initUi() {
        if (launchType == RESET_PASSWORD_OTP_SCREEN || launchType == OLD_USER_FLOW) {
            viewModel.getOldPassword().setValue("");// setting default value for validating
            binding.oldPasswordLyt.setVisibility(View.GONE);
        } else {
            binding.oldPasswordLyt.setVisibility(View.VISIBLE);//change password flow
            binding.oldPassword.requestFocus();
        }
    }

    private void getLaunchTypeFromBundle() {
        if (getIntent() != null) {
            launchType = getIntent().getIntExtra(EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE, -1);
            if (getIntent().hasExtra(EXTRA_TOKEN) && getIntent().hasExtra(EXTRA_USER_ID)) {
                token = getIntent().getStringExtra(EXTRA_TOKEN);
                userId = getIntent().getStringExtra(EXTRA_USER_ID);
            }
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
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
        LiveData<BaseResponse> liveData;
        if (launchType == RESET_PASSWORD_OTP_SCREEN) {

            liveData = viewModel.forgotUpdate(token, this.userId);

        } else if (launchType == OLD_USER_FLOW) {
            liveData = viewModel.appMigrationResetPassword(userId, token);

        } else {
            //change password flow
            String userId = PreferenceUtils.getValueString(this, PreferenceUtils.USER_ID_KEY);
            liveData = viewModel.changePassword(userId);
        }

        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.isStatus()) {
                    if (launchType == OLD_USER_FLOW) {
                        launchLoginScreen();
                    } else {
                        DialogUtil.showAlertDialog(this, getString(R.string.password_upd_msg),
                                (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        });
                    }
                } else {
                    DialogUtil.showAlertDialogNegativeVector(this, baseResponse.getMessage()
                            , (d, w) -> d.dismiss());
                }
            } else {
                ToastUtils.getInstance(this).showLongToast(getString(R.string.something_went_wrong));
            }
        });
    }

    private void launchLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }


    private void saveCredential(String userId, String token) {
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN, token);
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_ID_KEY, userId);
    }

}
