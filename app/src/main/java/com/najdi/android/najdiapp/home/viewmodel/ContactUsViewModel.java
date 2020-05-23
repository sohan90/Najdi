package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;
import com.najdi.android.najdiapp.home.model.User;

public class ContactUsViewModel extends BaseViewModel {
    MutableLiveData<String> message = new MutableLiveData<>();
    MutableLiveData<String> name = new MutableLiveData<>();
    MutableLiveData<String> phone = new MutableLiveData<>();
    MutableLiveData<String> email = new MutableLiveData<>();


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
        if (TextUtils.isEmpty(name.getValue())) {
            isValid = false;
        } else if (TextUtils.isEmpty(phone.getValue())) {
            isValid = false;
        } else if (TextUtils.isEmpty(email.getValue())) {
            isValid = false;
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
