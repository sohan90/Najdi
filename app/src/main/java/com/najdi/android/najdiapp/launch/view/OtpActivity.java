package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityOtpBinding;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.launch.model.OtpViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;
import static com.najdi.android.najdiapp.common.Constants.OTP_TIME;
import static com.najdi.android.najdiapp.common.Constants.OtpScreen.CHANGE_MOBILE_VERIFY;
import static com.najdi.android.najdiapp.common.Constants.OtpScreen.FORGOT_PASSWORD_SCREEN;
import static com.najdi.android.najdiapp.common.Constants.OtpScreen.OLD_USER_FLOW;
import static com.najdi.android.najdiapp.common.Constants.OtpScreen.SIGN_UP_SCREEN;
import static com.najdi.android.najdiapp.launch.view.ChangePasswordActivity.EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE;
import static com.najdi.android.najdiapp.launch.view.ChangePasswordActivity.EXTRA_TOKEN;
import static com.najdi.android.najdiapp.launch.view.ChangePasswordActivity.EXTRA_USER_ID;

public class OtpActivity extends BaseActivity {
    ActivityOtpBinding binding;
    private OtpViewModel viewModel;
    public static final String EXTRA_SCREEN_TYPE = "extra_screen_type_otp";
    public static final String EXTRA_NEW_MOBILE_NO = "extra_mobile_no";
    public static final String EXTRA_SIGN_UP_TEMP_ID = "extra_temp_id";
    public static final String EXTRA_SIGN_UP_SUCCESS_MSG = "extra_sign_up_succss_msg";
    private int screenType;
    private String newMobileNo;
    private String tempId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        getLaunchTypeFromBundle();
        initializeViewModel();
        bindViewModel();
        initClickListener();
        startTimerFor60s();
        updateViewModel();
    }

    private void updateViewModel() {
        viewModel.setScreenType(screenType);
        viewModel.setNewMobile(newMobileNo);
    }

    private void getLaunchTypeFromBundle() {
        if (getIntent() != null) {
            screenType = getIntent().getIntExtra(EXTRA_SCREEN_TYPE, -1);
            if (getIntent().hasExtra(EXTRA_NEW_MOBILE_NO)) {
                newMobileNo = getIntent().getStringExtra(EXTRA_NEW_MOBILE_NO);
            }
            if (getIntent().hasExtra(EXTRA_SIGN_UP_TEMP_ID)) {
                tempId = getIntent().getStringExtra(EXTRA_SIGN_UP_TEMP_ID);
                if (getIntent().hasExtra(EXTRA_SIGN_UP_SUCCESS_MSG)) {
                    String successMsg = getIntent().getStringExtra(EXTRA_SIGN_UP_SUCCESS_MSG);
                    DialogUtil.showAlertDialog(this, successMsg,
                            (dialog, which) -> dialog.dismiss());
                }
            }
        }
    }

    private void startTimerFor60s() {
        binding.resend.setEnabled(false);
        addDisposable(Observable
                .intervalRange(1, OTP_TIME, 0, 1, TimeUnit.SECONDS)
                .map(l -> OTP_TIME - l)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(sec -> {
                    String appendSec = sec + "s";
                    binding.resend.setText(getString(R.string.resend_code_in, appendSec));
                }).doOnComplete(() -> {
                    binding.resend.setEnabled(true);
                    binding.resend.setText(getString(R.string.resend_code));
                })
                .subscribe());

    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(OtpViewModel.class);
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
        LiveData<BaseResponse> liveData;
        if (screenType == CHANGE_MOBILE_VERIFY) {
            liveData = viewModel.resendOtpForChangeMobileNo(tempId);

        } else if (screenType == FORGOT_PASSWORD_SCREEN) {
            liveData = viewModel.forgotresendOtp();
        } else {
            liveData = viewModel.resendOtp(tempId);
        }
        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.isStatus()) {
                    startTimerFor60s();
                    String phone = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PHONE_NO_KEY);
                    String msg = getString(R.string.verification_password_sent_to, phone);
                    DialogUtil.showAlertDialog(this, msg, (dialog, which) -> dialog.dismiss());
                } else {
                    DialogUtil.showAlertDialogNegativeVector(this, baseResponse.getMessage(),
                            (dialog, which) -> dialog.dismiss());
                }

            }
        });
    }

    private void verifyOtp() {
        showProgressDialog();
        String token_ = PreferenceUtils.getValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN);
        LiveData<BaseResponse> liveData = null;
        switch (screenType) {
            case SIGN_UP_SCREEN:
                liveData = viewModel.verifyOtp(String.valueOf(tempId));
                break;

            case CHANGE_MOBILE_VERIFY:
                liveData = viewModel.mobileNoverify(tempId);// change mobile no verification
                break;

            case FORGOT_PASSWORD_SCREEN:
                String token = PreferenceUtils.getValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN);
                liveData = viewModel.verifyOtpForForgotPassword(tempId, token);// forgot password flow
                break;

            case OLD_USER_FLOW:
                liveData = viewModel.verifyAppMigration(tempId, token_);
                break;

        }

        if (liveData == null) return;

        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                if (screenType == SIGN_UP_SCREEN) {
                    fetchCityForProducts();
                    //login();// sign up flow
                } else if (screenType == CHANGE_MOBILE_VERIFY) {
                    PreferenceUtils.setValueString(this,
                            PreferenceUtils.USER_PHONE_NO_KEY, newMobileNo);
                    DialogUtil.showAlertDialog(this, baseResponse.getMessage(), (d, w) -> {
                        d.dismiss();
                        finish();

                    });

                } else if (screenType == FORGOT_PASSWORD_SCREEN) {
                    launchChangePasswordScreen(baseResponse.getToken(), baseResponse.getUserid(),
                            Constants.OtpScreen.RESET_PASSWORD_OTP_SCREEN);
                    finish();// forgot password flow
                } else { //old user migration flow
                    launchChangePasswordScreen(baseResponse.getUserToken(), tempId, OLD_USER_FLOW);
                    finish();
                }
            } else {
                if (baseResponse == null) return;
                DialogUtil.showAlertDialogNegativeVector(this, baseResponse.getMessage(),
                        (dialog, which) -> dialog.dismiss());
            }
        });
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
                        login();
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
                    login();
                }, () -> binding.blurLyt.setAlpha(0f));

    }

    private void launchChangePasswordScreen(String token, String userId, @Constants.OtpScreen int launchType) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra(EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE, launchType);
        intent.putExtra(EXTRA_TOKEN, token);
        intent.putExtra(EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    private void login() {
        showProgressDialog();
        String userName = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PHONE_NO_KEY);
        String password = PreferenceUtils.getValueString(this, PreferenceUtils.USER_PASSWORD);
        String fcmToken = PreferenceUtils.getValueString(this, PreferenceUtils.FCM_TOKEN_KEY);
        LiveData<BaseResponse> liveData = viewModel.login(userName, password, fcmToken);
        liveData.observe(this, baseResponse -> {

            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                String loginToken = baseResponse.getUserToken();
                PreferenceUtils.setValueString(this, PreferenceUtils.USER_LOGIIN_TOKEN,
                        loginToken);
                PreferenceUtils.setValueString(this, PreferenceUtils.USER_ID_KEY,
                        baseResponse.getUserid());

                launchHomeScreen();

            } else {
                if (baseResponse != null && !baseResponse.isStatus()) {
                    String message = getString(R.string.incorrect_password);
                    if (LocaleUtitlity.getCountryLang().equalsIgnoreCase(ARABIC_LAN)) {
                        message = getString(R.string.incorrect_password_arabic);
                    }
                    DialogUtil.showAlertDialogNegativeVector(this, message,
                            (dialog, which) -> dialog.dismiss());
                }
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
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
