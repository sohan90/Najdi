package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.FragmentForgotPasswordBinding;
import com.najdi.android.najdiapp.launch.viewmodel.ForgotPasswordViewModel;
import com.najdi.android.najdiapp.launch.viewmodel.LoginViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SCREEN_TYPE;

public class ForgotPasswordFragment extends BaseFragment {

    FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel viewModel;
    private LoginViewModel loginActivityViewModel;

    public static ForgotPasswordFragment createInstance() {
        return new ForgotPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password,
                container, false);

        initActivityViewModel();
        initViewModel();
        bindViewModel();
        initClickListener();
        setData();
        return binding.getRoot();
    }

    private void setData() {
        if (getActivity() == null) return;
        viewModel.getPhoneno().setValue(PreferenceUtils.getValueString(getActivity(),
                PreferenceUtils.USER_PHONE_NO_KEY));
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initClickListener() {
        binding.submit.setOnClickListener(v -> {
            showProgressDialog();
            String lang = resourceProvider.getCountryLang();
            LiveData<BaseResponse> liveData = viewModel.forgotPasswordRequest(lang);
            liveData.observe(this, baseResponse -> {
                hideProgressDialog();
                if (baseResponse != null) {
                    DialogUtil.showAlertDialog(getActivity(), baseResponse.getData().getMessage(),
                            (dialog, which) -> {
                                dialog.dismiss();
                                launchOtpScreen();
                            });

                }
            });
        });
        binding.numberTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.submit.setEnabled(true);
                } else {
                    binding.submit.setEnabled(false);
                }
            }
        });
    }

    private void launchOtpScreen() {
        Intent intent = new Intent(getActivity(), OtpActivity.class);
        intent.putExtra(EXTRA_SCREEN_TYPE, Constants.OtpScreen.FORGOT_PASSWORD_SCREEN);
        startActivity(intent);
    }

    private void initActivityViewModel() {
        if (getActivity() == null) return;
        loginActivityViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginActivityViewModel.getToolbarTitle().setValue(getString(R.string.forgot_password));
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
    }
}
