package com.najdi.android.najdiapp.launch.model;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OtpViewModel extends BaseViewModel {

    private MutableLiveData<String> one = new MutableLiveData<>();
    private MutableLiveData<String> two = new MutableLiveData<>();
    private MutableLiveData<String> three = new MutableLiveData<>();
    private MutableLiveData<String> four = new MutableLiveData<>();
    private MutableLiveData<String> phoneNoLiveData = new MutableLiveData<>();


    public MutableLiveData<String> getOne() {
        return one;
    }

    public MutableLiveData<String> getTwo() {
        return two;
    }

    public MutableLiveData<String> getThree() {
        return three;
    }

    public MutableLiveData<String> getFour() {
        return four;
    }

    public MutableLiveData<String> getPhoneNo() {
        String phoneNo = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_PHONE_NO_KEY);
        this.phoneNoLiveData.setValue(phoneNo);

        return phoneNoLiveData;
    }

    public OtpViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseResponse> verifyOtp(String mobile) {
        String otp = one.getValue() + two.getValue() + three.getValue() + four.getValue();
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setMobile(mobile);
        otpRequestModel.setOtp(otp);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.verifyOtp(otpRequestModel);
    }

    public LiveData<BaseResponse> verifyOtpForForgotPassword(String mobile) {
        String otp = one.getValue() + two.getValue() + three.getValue() + four.getValue();
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setMobile(mobile);
        otpRequestModel.setOtp(otp);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.verifyForgotOtp(otpRequestModel);
    }

    public LiveData<BaseResponse> login(String username, String password) {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setUserName(username);
        loginRequestModel.setPassword(password);
        return repository.loginToken(loginRequestModel);
    }

    public LiveData<BaseResponse> resendOtp() {
        String phoneNo = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_PHONE_NO_KEY);
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setMobile(phoneNo);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.resendOtp(otpRequestModel);
    }
}
