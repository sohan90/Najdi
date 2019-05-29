package com.najdi.android.najdiapp.launch.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;
import com.najdi.android.najdiapp.NajdiApplication;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SignUpViewModel extends AndroidViewModel {
    private final ResourceProvider resourceProvider;
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
        NajdiApplication najdiApplication = (NajdiApplication) application;
        resourceProvider = najdiApplication.getResourceProvider();
    }


    public LiveData<Boolean> getValidationStatus() {
        return validateSuccess;
    }

    @BindingAdapter("bind:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }

    public void validate() {
        boolean valid = true;
        if (TextUtils.isEmpty(phoneNo.getValue())) {
            phoneNoError.setValue(resourceProvider.getString(R.string.invalid_phone_no));
        } else {
            phoneNoError.setValue(null);
        }
        if (TextUtils.isEmpty(password.getValue())) {
            passwordError.setValue(resourceProvider.getString(R.string.invalid_pass));
        } else {
            passwordError.setValue(null);
        }
        if (TextUtils.isEmpty(confirmPass.getValue()) && password.getValue() != null &&
                !password.getValue().equals(confirmPass.getValue())) {
            confirmPassError.setValue(resourceProvider.getString(R.string.invalid_pass));
            valid = false;
        } else {
            confirmPassError.setValue(null);
        }
        validateSuccess.setValue(valid);
    }

    public void registerUser() {

    }

}
