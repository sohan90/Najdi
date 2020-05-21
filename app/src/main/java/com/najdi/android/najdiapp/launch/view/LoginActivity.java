package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityLoginBinding;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.launch.viewmodel.LoginViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.FCM_TOKEN_KEY;

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
        subscribeForToolBarTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.frgLyt.setVisibility(View.GONE);
        FragmentHelper.popBackStack(this);

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
        binding.forgotPassword.setOnClickListener(this);
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

            case R.id.forgot_password:
                launchForgotPasswordScreen();
                break;
        }
    }

    private void launchForgotPasswordScreen() {
        Fragment fragment = ForgotPasswordFragment.createInstance();
        FragmentHelper.replaceFragment(this, fragment, Constants.FragmentTags.FORGOT_PASSWORD,
                true, binding.fragmentContainer.getId());
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
        String fcmToken = PreferenceUtils.getValueString(this, FCM_TOKEN_KEY);
        viewModel.login(fcmToken).observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.isStatus()) {
                    saveCredential(baseResponse.getUserid(), baseResponse.getUserToken());
                    launchHomeScreen();
                    finish();

                } else {
                    String message;
                    if (baseResponse.getMessage() == null) {
                         message = getString(R.string.incorrect_password);
                        if (LocaleUtitlity.getCountryLang().equalsIgnoreCase(ARABIC_LAN)) {
                            message = getString(R.string.incorrect_password_arabic);
                        }
                    } else {
                        message = baseResponse.getMessage();
                    }
                    DialogUtil.showAlertDialog(this, message,
                            (dialog, which) -> dialog.dismiss());
                }
            }
        });
    }

    private void saveCredential(String userId, String token) {
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN, token);
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_ID_KEY, userId);
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
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
