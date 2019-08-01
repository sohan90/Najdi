package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ContactUsViewModel extends BaseViewModel {
    MutableLiveData<String> message = new MutableLiveData<>();


    public MutableLiveData<String> getMessage() {
        return message;
    }

    public ContactUsViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseResponse> contactUs(String phoneNo) {
        LiveData<BaseResponse> liveData = null;
        if (message.getValue() != null) {
            ContactUsRequest contactUsRequest = new ContactUsRequest();
            contactUsRequest.setMessage(message.getValue() + " " + "\n" + phoneNo);
            contactUsRequest.setUser_name(phoneNo);
            contactUsRequest.setSubject("Customer's Message");

            liveData = repository.contactUs(contactUsRequest);
        }
        return liveData;
    }
}
