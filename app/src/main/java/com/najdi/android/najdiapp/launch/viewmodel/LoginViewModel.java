package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<String> phoneNo = new MutableLiveData<>();
    public MutableLiveData<String> phoneNoError = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validationStatus = new MutableLiveData<>();
    private MutableLiveData<String> toolbarTitle = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    @BindingAdapter("errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }

    public LiveData<Boolean> getValidationStatus() {
        return validationStatus;
    }

    public void validate() {
        boolean isValid;
        if (phoneNo.getValue() == null) {
            phoneNoError.setValue(resourceProvider.getString(R.string.invalid_phone_no));
            isValid = false;
        } else {
            if (phoneNo.getValue().startsWith("5") && phoneNo.getValue().length() >= 8) {
                phoneNoError.setValue(null);
                isValid = true;
            } else {
                phoneNoError.setValue(resourceProvider.getString(R.string.invalid_phone_no));
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(password.getValue())) {
            passwordError.setValue(resourceProvider.getString(R.string.invalid_pass));
            isValid = false;
        } else {
            passwordError.setValue(null);
        }
        validationStatus.setValue(isValid);
    }

    public LiveData<BaseResponse> login() {
        LoginRequestModel requestModel = new LoginRequestModel();
        String phoneNo = "966" + this.phoneNo.getValue();
        requestModel.setUserName(phoneNo);
        requestModel.setPassword(password.getValue());
        PreferenceUtils.setValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_PHONE_NO_KEY, this.phoneNo.getValue());
        PreferenceUtils.setValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_PASSWORD, password.getValue());
        return repository.loginToken(requestModel);
    }

    public MutableLiveData<String> getToolbarTitle() {
        return toolbarTitle;
    }
}
