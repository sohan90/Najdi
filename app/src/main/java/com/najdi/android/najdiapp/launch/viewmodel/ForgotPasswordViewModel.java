package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ForgotPasswordViewModel extends BaseViewModel {

    MutableLiveData<String> phoneno = new MutableLiveData<>();
    MutableLiveData<String> oldPassword = new MutableLiveData<>();
    MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    MutableLiveData<String> password = new MutableLiveData<>();
    MutableLiveData<Boolean> validatePasswordStatus = new MutableLiveData<>();
    MutableLiveData<String> passwordErrorField = new MutableLiveData<>();


    public MutableLiveData<String> getPhoneno() {
        return phoneno;
    }

    public MutableLiveData<String> getPasswordErrorField() {
        return passwordErrorField;
    }

    public MutableLiveData<String> getOldPassword() {
        return oldPassword;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseResponse> forgotPasswordRequest(String lang) {
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setMobile(phoneno.getValue());
        forgotPaswwordRequest.setLang(lang);

        return repository.forgotPasswordRequest(forgotPaswwordRequest);
    }

    public LiveData<BaseResponse> forgotUpdate(String phoneNo, String otp, String lang) {
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setMobile(phoneNo);
        forgotPaswwordRequest.setOtp(otp);
        forgotPaswwordRequest.setLang(lang);
        forgotPaswwordRequest.setPassword(password.getValue());

        return repository.forgotUpdate(forgotPaswwordRequest);
    }

    public LiveData<Boolean> validatePassword() {
        boolean isValid = false;
        if (password.getValue() != null && confirmPassword.getValue() != null) {
            if (password.getValue().equals(confirmPassword.getValue())) {
                isValid = true;
            } else {
                passwordErrorField.setValue(resourceProvider.getString(R.string.password_mismatched));
            }
        } else {
            ToastUtils.getInstance(resourceProvider.getActivityContext()).showShortToast(
                    resourceProvider.getString(R.string.please_fill));
        }
        validatePasswordStatus.setValue(isValid);
        return validatePasswordStatus;
    }
}
