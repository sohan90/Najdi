package com.najdi.android.najdiapp.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityChangeMobileNumberBinding;
import com.najdi.android.najdiapp.home.viewmodel.ChangeMobileNoViewModel;
import com.najdi.android.najdiapp.launch.view.OtpActivity;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_NEW_MOBILE_NO;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SCREEN_TYPE;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SIGN_UP_TEMP_ID;

public class ChangeMobileNoActivity extends BaseActivity {

    private ActivityChangeMobileNumberBinding binding;
    private ChangeMobileNoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_mobile_number);
        initViewModel();
        bindViewModel();
        initClickListener();
        setData();
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ChangeMobileNoViewModel.class);
    }

    private void initClickListener() {
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.update.setOnClickListener(v -> {
            boolean isValid = viewModel.validate();
            if (isValid) {
                updateMobileNo();
            } else {
                DialogUtil.showAlertDialogNegativeVector(this,
                        getString(R.string.enter_valid_phone_no),
                        (d, h) -> d.dismiss());
            }
        });

    }

    private void updateMobileNo() {
        showProgressDialog();
        String userId = PreferenceUtils.getValueString(this, PreferenceUtils.USER_ID_KEY);
        LiveData<BaseResponse> liveData = viewModel.updateMobileNo(resourProvider
                .getCountryLang(), userId);
        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse == null) return;
            if (baseResponse.isStatus()) {
                DialogUtil.showAlertDialog(this, baseResponse.getMessage(),
                        (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                            launchOtpActivity(baseResponse.getTempId(),
                                    viewModel.getNewMobileNo().getValue());
                        });
            } else {
                DialogUtil.showAlertDialog(this, baseResponse.getMessage(),
                        (d, which) -> d.dismiss());
            }
        });
    }

    private void launchOtpActivity(String tempId, String newMobileNO) {
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(EXTRA_SCREEN_TYPE, Constants.OtpScreen.CHANGE_MOBILE_VERIFY);
        intent.putExtra(EXTRA_NEW_MOBILE_NO, newMobileNO);
        intent.putExtra(EXTRA_SIGN_UP_TEMP_ID, tempId);
        startActivity(intent);
    }

    private void setData() {
        String phoneNo = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PHONE_NO_KEY);
        viewModel.getOldMobileNo().setValue(phoneNo);
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }
}
