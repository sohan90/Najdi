package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.ActivityOtpBinding;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.launch.model.OtpViewModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class OtpActivity extends BaseActivity {
    ActivityOtpBinding binding;
    private OtpViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        initializeViewModel();
        bindViewModel();
        initClickListener();
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(OtpViewModel.class);
        binding.one.requestFocus();
        binding.one.setCursorVisible(true);
    }

    private void initClickListener() {
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.one.addTextChangedListener(new GenericTextWatcher(binding.one));
        binding.two.addTextChangedListener(new GenericTextWatcher(binding.two));
        binding.three.addTextChangedListener(new GenericTextWatcher(binding.three));
        binding.four.addTextChangedListener(new GenericTextWatcher(binding.four));

        binding.verify.setOnClickListener(v -> verifyOtp());
        binding.resend.setOnClickListener(v -> resendOtp());
    }

    private void resendOtp() {
        showProgressDialog();
        viewModel.resendOtp().observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.getData() != null) {
                    ToastUtils.getInstance(this).showShortToast(baseResponse.getData().getMessage());
                }
            }
        });
    }

    private void verifyOtp() {
        showProgressDialog();
        LiveData<BaseResponse> liveData = viewModel.verifyOtp(PreferenceUtils.getValueString(this,
                PreferenceUtils.USER_PHONE_NO_KEY));

        liveData.observe(this, baseResponse -> {
            if (baseResponse != null) {
                login();
            } else {
                hideProgressDialog();
            }
        });
    }

    private void login() {
        String userName = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PHONE_NO_KEY);
        String password = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PASSWORD);
        LiveData<BaseResponse> liveData = viewModel.login(userName, password);
        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.getData() != null) {
                    String loginToken = baseResponse.getData().getToken();
                    PreferenceUtils.setValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN,
                            loginToken);
                    launchHomeScreen();
                }
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.one:
                    if (text.length() == 1)
                        binding.two.requestFocus();
                    break;

                case R.id.two:
                    if (text.length() == 1)
                        binding.three.requestFocus();
                    else if (text.length() == 0)
                        binding.one.requestFocus();
                    break;

                case R.id.three:
                    if (text.length() == 1)
                        binding.four.requestFocus();
                    else if (text.length() == 0)
                        binding.two.requestFocus();
                    break;

                case R.id.four:
                    if (text.length() == 0)
                        binding.three.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}