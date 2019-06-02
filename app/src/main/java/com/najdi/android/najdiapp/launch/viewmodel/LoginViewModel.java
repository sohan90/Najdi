package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;

import com.google.android.material.textfield.TextInputLayout;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;

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
            phoneNoError.setValue(null);
            isValid = true;
        }

        if (password.getValue() == null) {
            passwordError.setValue(resourceProvider.getString(R.string.invalid_pass));
            isValid = false;
        } else {
            passwordError.setValue(null);
        }
        validationStatus.setValue(isValid);
    }

    public LiveData<BaseResponse> login() {
        LoginRequestModel requestModel = new LoginRequestModel();
        requestModel.setUserName(phoneNo.getValue());
        requestModel.setPassword(password.getValue());
        return repository.loginUser(requestModel);
    }
}
