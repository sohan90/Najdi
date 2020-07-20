package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityLoginBinding;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.launch.viewmodel.LoginViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.NetworkUtility;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.List;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SCREEN_TYPE;
import static com.najdi.android.najdiapp.launch.view.OtpActivity.EXTRA_SIGN_UP_TEMP_ID;
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
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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
                login();
            }
        });
    }

    private void login() {
        String fcmToken = PreferenceUtils.getValueString(this, FCM_TOKEN_KEY);
        if (NetworkUtility.isNetworkConnected(this)) {
            showProgressDialog();
            viewModel.login(fcmToken).observe(this, baseResponse -> {
                hideProgressDialog();
                if (baseResponse != null) {
                    if (baseResponse.isStatus()) {
                        if (baseResponse.getMigrateStatus() == BaseResponse.OLD_USER) { // for migrating the old user to the new system in the backend
                            saveCredential(null, baseResponse.getUserToken());
                            launchOTPscreen(baseResponse.getUserid());
                        } else {
                            saveCredential(baseResponse.getUserid(), baseResponse.getUserToken());
                            fetchCityForProducts();// new user as usual flow
                        }

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
                        DialogUtil.showAlertDialogNegativeVector(this, message,
                                (dialog, which) -> dialog.dismiss());
                    }
                }
            });
        } else {
            DialogUtil.showAlertDialogNegativeVector(this, getString(R.string.no_network_msg),
                    (d, w)-> d.dismiss());
        }
    }

    private void launchOTPscreen(String userId) {
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(EXTRA_SCREEN_TYPE, Constants.OtpScreen.OLD_USER_FLOW);
        intent.putExtra(EXTRA_SIGN_UP_TEMP_ID, userId);
        startActivity(intent);
    }

    private void fetchCityForProducts() {
        viewModel.getCityList(resourProvider.getCountryLang())
                .observe(this, cityListModelResponse -> {
                    if (cityListModelResponse != null && cityListModelResponse.isStatus()) {
                        List<CityListModelResponse.City> cityList = cityListModelResponse.getCities();
                        addDisposable(getCityNameList(cityList)
                                .subscribe(strCity ->
                                        showPopupwindow(strCity, cityList)));

                    } else {
                        launchHomeScreen();
                    }
                });
    }

    protected void showPopupwindow(List<String> strings,
                                   List<CityListModelResponse.City> cityList) {
        String title = getString(R.string.select_city);
        binding.blurLyt.setAlpha(0.5f);
        DialogUtil.showPopupWindow(this,
                binding.container, title, strings, pos -> {
                    binding.blurLyt.setAlpha(0f);
                    CityListModelResponse.City city = cityList.get(pos);
                    saveCityId(city.getId());
                    launchHomeScreen();
                }, () -> binding.blurLyt.setAlpha(0f));

    }


    private void saveCredential(String userId, String token) {
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN, token);
        PreferenceUtils.setValueString(this, PreferenceUtils.USER_ID_KEY, userId);
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
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
