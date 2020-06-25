package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.HashMap;

public class ForgotPasswordViewModel extends BaseViewModel {

    private MutableLiveData<String> phoneno = new MutableLiveData<>();
    private MutableLiveData<String> oldPassword = new MutableLiveData<>();
    private MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<Boolean> validatePasswordStatus = new MutableLiveData<>();
    private MutableLiveData<String> passwordErrorField = new MutableLiveData<>();


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
        PreferenceUtils.setValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_PHONE_NO_KEY, phoneno.getValue());
        return repository.forgotPasswordRequest(forgotPaswwordRequest);
    }

    public LiveData<BaseResponse> forgotUpdate(String token, String userId) {
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setToken(token);
        forgotPaswwordRequest.setUserId(userId);
        forgotPaswwordRequest.setPassword(password.getValue());
        forgotPaswwordRequest.setLang(resourceProvider.getCountryLang());
        return repository.forgotUpdate(forgotPaswwordRequest);
    }

    public boolean validate() {
        boolean isValid = false;
        if (phoneno.getValue() != null && phoneno.getValue().startsWith("5")
                && phoneno.getValue().length() >= 8) {

            isValid = true;
        }
        return isValid;
    }

    public LiveData<BaseResponse> changePassword(String id) {
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setCp(oldPassword.getValue());
        forgotPaswwordRequest.setNp(password.getValue());
        forgotPaswwordRequest.setCnp(confirmPassword.getValue());
        forgotPaswwordRequest.setId(id);
        return repository.changePassword(forgotPaswwordRequest);
    }

    public LiveData<Boolean> validatePassword() {
        boolean isValid = false;
        if (password.getValue() != null && confirmPassword.getValue() != null &&
                oldPassword.getValue() != null) {

            if (password.getValue().length() >= 8 && confirmPassword.getValue().length() >= 8) {

                if (password.getValue().equals(confirmPassword.getValue())) {
                    isValid = true;
                } else {
                    passwordErrorField.setValue(resourceProvider.getString(R.string.password_mismatched));
                }
            } else {
                passwordErrorField.setValue(resourceProvider.getString(R.string.invalid_pass));
            }
        } else {
            DialogUtil.showAlertDialogNegativeVector(resourceProvider.getActivityContext(),
                    resourceProvider.getString(R.string.please_fill), (dialog, which) ->
                            dialog.dismiss());
        }
        validatePasswordStatus.setValue(isValid);
        return validatePasswordStatus;
    }

    public LiveData<BaseResponse> appMigrationResetPassword(String userId, String token) {
        HashMap<String, String> request = new HashMap<>();
        request.put("user_id", userId);
        request.put("token", token);
        request.put("password", confirmPassword.getValue());
        return repository.resetPasswordForAppMigration(request);
    }
}
