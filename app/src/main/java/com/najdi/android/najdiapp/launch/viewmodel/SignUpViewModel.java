package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;

import com.google.android.material.textfield.TextInputLayout;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
        boolean valid = false;
        if (phoneNo.getValue() != null) {
            phoneNoError.setValue(null);
        } else {
            phoneNoError.setValue(resourceProvider.getString(R.string.invalid_phone_no));
        }

        if (password.getValue() != null && password.getValue().length() <= 6) {
            passwordError.setValue(null);

        } else {
            passwordError.setValue(resourceProvider.getString(R.string.invalid_pass));
        }

        if (password.getValue() != null && confirmPass.getValue() != null && confirmPass.getValue()
                .equals(password.getValue())) {

            confirmPassError.setValue(null);
            valid = true;

        } else {
            confirmPassError.setValue(resourceProvider.getString(R.string.invalid_pass));
        }
        validateSuccess.setValue(valid);
    }

    public LiveData<SignupResponseModel> registerUser() {
        SignupRequestModel signupRequestModel = new SignupRequestModel();
        signupRequestModel.setEmail(email.getValue());
        signupRequestModel.setPassword(password.getValue());
        signupRequestModel.setUsername(name.getValue());
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setPhone(phoneNo.getValue());
        signupRequestModel.setBilling(billingAddress);
        return repository.registerUser(signupRequestModel);
    }

}
