package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;
import com.najdi.android.najdiapp.home.model.User;

public class ContactUsViewModel extends BaseViewModel {
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> phone = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> phoneError = new MutableLiveData<>();
    private MutableLiveData<String> emailError = new MutableLiveData<>();


    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getEmailError() {
        return emailError;
    }

    public MutableLiveData<String> getPhoneError() {
        return phoneError;
    }

    public ContactUsViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseResponse> contactUs(String userId) {
        LiveData<BaseResponse> liveData = null;
        if (message.getValue() != null) {
            ContactUsRequest contactUsRequest = new ContactUsRequest();
            contactUsRequest.setUseId(userId);
            contactUsRequest.setName(name.getValue());
            contactUsRequest.setPhone(phone.getValue());
            contactUsRequest.setEmail(email.getValue());
            contactUsRequest.setMessage(message.getValue() + " " + "\n" + phone);
            contactUsRequest.setLang(resourceProvider.getCountryLang());

            liveData = repository.contactUs(contactUsRequest);
        }
        return liveData;
    }

    public boolean validateFields() {
        boolean isValid = true;
        phoneError.setValue("");
        emailError.setValue("");
        if (TextUtils.isEmpty(name.getValue())) {
            isValid = false;
        } else if (TextUtils.isEmpty(phone.getValue()) || !phone.getValue().startsWith("5") ||
                phone.getValue().length() < Constants.MOBILE_NO_LENGTH) {
            isValid = false;
            phoneError.setValue(resourceProvider.getString(R.string.mobile_no_error));
        } else if (!TextUtils.isEmpty(email.getValue()) && !Patterns.EMAIL_ADDRESS
                .matcher(email.getValue()).matches()) {
            isValid = false;
            emailError.setValue(resourceProvider.getString(R.string.invalid_email));
        } else if (TextUtils.isEmpty(message.getValue())) {
            isValid = false;
        }
        return isValid;
    }

    public LiveData<BaseResponse> getUserDetail(String userId) {
        return repository.getUserDetail(userId);
    }

    public void setDetail(User user) {
        name.setValue(user.getFull_name());
        phone.setValue(user.getPhone());
        email.setValue(user.getEmail());
    }
}
