package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputLayout;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

public class SignUpViewModel extends BaseViewModel {
    public MutableLiveData<String> phoneNo = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirmPass = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> phoneNoError = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    public MutableLiveData<String> confirmPassError = new MutableLiveData<>();
    private MutableLiveData<Boolean> validateSuccess = new MutableLiveData<>();
    private MutableLiveData<String> toolbarTitle = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Boolean> getValidationStatus() {
        return validateSuccess;
    }

    @BindingAdapter("errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }

    public void validate() {
        if (phoneNo.getValue() != null &&
                phoneNo.getValue().startsWith("5") && phoneNo.getValue().length() ==
                Constants.MOBILE_NO_LENGTH) {
            phoneNoError.setValue(null);
        } else {
            phoneNoError.setValue(resourceProvider.getString(R.string.invalid_phone_no));
        }

        if (password.getValue() != null && password.getValue().length() >= Constants.PASSWORD_LENGTH) {
            passwordError.setValue(null);

        } else {
            passwordError.setValue(resourceProvider.getString(R.string.password_error));
        }

        if (password.getValue() != null && confirmPass.getValue() != null && confirmPass.getValue()
                .equals(password.getValue())) {

            confirmPassError.setValue(null);

        } else {
            confirmPassError.setValue(resourceProvider.getString(R.string.password_does_not_matched));
        }

        if (phoneNoError.getValue() == null && passwordError.getValue() == null &&
                confirmPassError.getValue() == null) {

            validateSuccess.setValue(true);
        } else {
            validateSuccess.setValue(false);
        }
    }

    public LiveData<BaseResponse> registerUser(String fcmToken) {
        SignupRequestModel signupRequestModel = new SignupRequestModel();
        signupRequestModel.setPassword(password.getValue());
        signupRequestModel.setMobile(phoneNo.getValue());
        signupRequestModel.setFcmToken(fcmToken);
        signupRequestModel.setLang(resourceProvider.getCountryLang());
        if (!TextUtils.isEmpty(name.getValue())) {
            signupRequestModel.setFullName(name.getValue());
            PreferenceUtils.setValueString(resourceProvider.getAppContext(), PreferenceUtils.USER_NAME_KEY,
                    name.getValue());
        }
        if (!TextUtils.isEmpty(email.getValue())) {
            signupRequestModel.setEmail(email.getValue());
            PreferenceUtils.setValueString(resourceProvider.getAppContext(), PreferenceUtils.USER_EMAIL_KEY,
                    email.getValue());
        }

        PreferenceUtils.setValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_PASSWORD, password.getValue());
        PreferenceUtils.setValueString(resourceProvider.getAppContext(), PreferenceUtils.USER_PHONE_NO_KEY,
                phoneNo.getValue());
        return repository.registerUser(signupRequestModel);
    }

    public MutableLiveData<String> getToolbarTitle() {
        return toolbarTitle;
    }

}
