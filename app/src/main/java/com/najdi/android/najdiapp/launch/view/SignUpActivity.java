package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivitySignUpBinding;
import com.najdi.android.najdiapp.home.view.AboutUsFragment;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;
import com.najdi.android.najdiapp.launch.viewmodel.SignUpViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.NetworkUtility;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SCREEN_TYPE;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SIGN_UP_SUCCESS_MSG;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SIGN_UP_TEMP_ID;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.FCM_TOKEN_KEY;

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
        subscribeForToolBarTitle();
        hideToolBarFilterCart();
    }

    private void hideToolBarFilterCart() {
        binding.toolBarLyt.filter.setVisibility(View.INVISIBLE);
        binding.toolBarLyt.cartImageLyt.setVisibility(View.INVISIBLE);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
    }

    private void setupBinding() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void subscribeForToolBarTitle() {
        viewModel.getToolbarTitle().observe(this, s -> {
            binding.frgLyt.setVisibility(View.VISIBLE);
            binding.toolBarLyt.backArrow.setVisibility(View.VISIBLE);
            binding.toolBarLyt.title.setVisibility(View.VISIBLE);
            binding.toolBarLyt.title.setText(s);
            binding.toolBarLyt.backArrow.setOnClickListener(v -> onBackPressed());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.frgLyt.setVisibility(View.GONE);
    }

    private void initClickListener() {
        binding.signUp.setOnClickListener((v) -> {
            showProgressDialog();
            viewModel.validate();
        });
        binding.signIn.setOnClickListener(v -> launchLoginScreen());
        binding.terms.setOnClickListener(v -> {
            Fragment fragment = AboutUsFragment.createInstance(Constants.HtmlScreen.TERMS_CONDITION);
            launchTermConditionScreen(fragment, "terms_condition");
        });
        binding.privacy.setOnClickListener(v -> {
            Fragment privacyFragment = AboutUsFragment.createInstance(Constants.HtmlScreen.PRIVACY_POLICY);
            launchTermConditionScreen(privacyFragment, "privacy_policy");
        });
    }

    private void launchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void subscribeValidationStatus() {
        viewModel.getValidationStatus().observe(this, valid -> {
            if (valid) {
                if (NetworkUtility.isNetworkConnected(this)) {
                    String fcmToken = PreferenceUtils.getValueString(this, FCM_TOKEN_KEY);
                    LiveData<BaseResponse> liveData = viewModel.registerUser(fcmToken);
                    liveData.observe(this, signupResponseModel -> {
                        hideProgressDialog();
                        if (signupResponseModel != null) {
                            if (signupResponseModel.isStatus()) {
                                launchOTPScreen(signupResponseModel.getTempId(), signupResponseModel.getMessage());
                                finish();

                            } else {
                                DialogUtil.showAlertDialog(this, signupResponseModel.
                                                getMessage(),
                                        (dialog, which) -> dialog.dismiss());
                            }
                        }
                    });
                } else {
                    DialogUtil.showAlertDialogNegativeVector(this, getString(R.string.no_network_msg)
                    , (d, w) -> d.dismiss());
                }
            } else {
                hideProgressDialog();
            }
        });
    }

    private void launchOTPScreen( String tempId, String successMsg) {
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(EXTRA_SCREEN_TYPE, Constants.OtpScreen.SIGN_UP_SCREEN);
        intent.putExtra(EXTRA_SIGN_UP_TEMP_ID, tempId);
        intent.putExtra(EXTRA_SIGN_UP_SUCCESS_MSG, successMsg);
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

    private void launchTermConditionScreen(Fragment fragment, String tag) {
        binding.fragmentContainer.getId();
        binding.frgLyt.setVisibility(View.VISIBLE);
        FragmentHelper.replaceFragment(this, fragment, tag, true,
                binding.fragmentContainer.getId());

    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount > 0) {
            binding.frgLyt.setVisibility(View.GONE);
        }
        super.onBackPressed();
    }
}
