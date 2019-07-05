package com.najdi.android.najdiapp.home.view;

import android.content.Intent;
import android.os.Bundle;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityChangeMobileNumberBinding;
import com.najdi.android.najdiapp.home.viewmodel.ChangeMobileNoViewModel;
import com.najdi.android.najdiapp.launch.view.OtpActivity;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_NEW_MOBILE_NO;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SCREEN_TYPE;

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

    private void initClickListener() {
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.update.setOnClickListener(v -> {
            showProgressDialog();
            LiveData<BaseResponse> liveData = viewModel.updateMobileNo(resourProvider.getCountryLang());
            liveData.observe(this, baseResponse -> {
                hideProgressDialog();
                if (baseResponse != null) {
                    DialogUtil.showAlertDialog(this, baseResponse.getData().getMessage(),
                            (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                                launchOtpActivity(viewModel.getNewMobileNo());
                            });
                }
            });
        });
    }

    private void launchOtpActivity(MutableLiveData<String> newMobileNo) {
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(EXTRA_SCREEN_TYPE, Constants.OtpScreen.CHANGE_MOBILE_VERIFY);
        intent.putExtra(EXTRA_NEW_MOBILE_NO, newMobileNo.getValue());
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

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChangeMobileNoViewModel.class);
    }
}
