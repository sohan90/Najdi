package com.najdi.android.najdiapp.launch.model;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.najdi.android.najdiapp.common.Constants.OtpScreen.CHANGE_MOBILE_VERIFY;

public class OtpViewModel extends BaseViewModel {

    private MutableLiveData<String> one = new MutableLiveData<>();
    private MutableLiveData<String> two = new MutableLiveData<>();
    private MutableLiveData<String> three = new MutableLiveData<>();
    private MutableLiveData<String> four = new MutableLiveData<>();
    private MutableLiveData<String> phoneNoLiveData = new MutableLiveData<>();
    private String newMobileNO;
    private int screenType;


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

    public LiveData<BaseResponse> mobileNoverify(String mobile, String newMobileNo) {
        String otp = one.getValue() + two.getValue() + three.getValue() + four.getValue();
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setMobile(mobile);
        forgotPaswwordRequest.setNew_mobile(newMobileNo);
        forgotPaswwordRequest.setOtp(otp);
        forgotPaswwordRequest.setLang(resourceProvider.getCountryLang());
        return repository.mobileChangeVerify(forgotPaswwordRequest);
    }


    public LiveData<BaseResponse> login(String username, String password) {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        String phoneNo = "966" + username;
        loginRequestModel.setUserName(phoneNo);
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


    public LiveData<BaseResponse> resendOtp(String newMobileNo) {
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setMobile(newMobileNo);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.resendOtp(otpRequestModel);
    }

    public void setNewMobile(String newMobileNo) {
        this.newMobileNO = newMobileNo;
        if (screenType == CHANGE_MOBILE_VERIFY) {
            phoneNoLiveData.setValue(newMobileNo);
        }
    }

    public void setScreenType(int screenType) {
        this.screenType = screenType;

    }
}
