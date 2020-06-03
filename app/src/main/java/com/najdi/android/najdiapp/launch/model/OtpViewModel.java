package com.najdi.android.najdiapp.launch.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

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

    public LiveData<BaseResponse> verifyOtp(String tempId) {
        String otp = one.getValue() + two.getValue() + three.getValue() + four.getValue();
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setTempId(tempId);
        otpRequestModel.setOtp(otp);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.verifyOtp(otpRequestModel);
    }

    public LiveData<BaseResponse> verifyOtpForForgotPassword(String tempId, String token) {
        String otp = one.getValue() + two.getValue() + three.getValue() + four.getValue();
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setTempId(tempId);
        otpRequestModel.setOtp(otp);
        // otpRequestModel.setToken(token);
        // otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.verifyForgotOtp(otpRequestModel);
    }

    public LiveData<BaseResponse> mobileNoverify(String tempId) {
        String otp = one.getValue() + two.getValue() + three.getValue() + four.getValue();
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setTempId(tempId);
        forgotPaswwordRequest.setOtp(otp);
        forgotPaswwordRequest.setLang(resourceProvider.getCountryLang());
        return repository.mobileChangeVerify(forgotPaswwordRequest);
    }


    public LiveData<BaseResponse> login(String username, String password, String fcmToken) {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        // String phoneNo = "966" + username;
        loginRequestModel.setPhone(username);
        loginRequestModel.setPassword(password);
        loginRequestModel.setFcmToken(fcmToken);
        loginRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.loginToken(loginRequestModel);
    }

    public LiveData<BaseResponse> resendOtp(String temp_id) {
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setTempId(temp_id);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.resendOtp(otpRequestModel);
    }

    public LiveData<BaseResponse> resendOtpForChangeMobileNo(String temp_id) {
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setTempId(temp_id);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.resendOtpForChangeMobileNo(otpRequestModel);
    }

    public LiveData<BaseResponse> forgotresendOtp() {
        String phoneNo = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_PHONE_NO_KEY);
        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setPhone(phoneNo);
        otpRequestModel.setLang(resourceProvider.getCountryLang());
        return repository.forgotresendOtp(otpRequestModel);
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

    public LiveData<CityListModelResponse> getCityList(String lang) {
        return repository.getCityList(lang);
    }
}
