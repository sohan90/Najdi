package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;

public class ContactUsViewModel extends BaseViewModel {
    MutableLiveData<String> message = new MutableLiveData<>();


    public MutableLiveData<String> getMessage() {
        return message;
    }

    public ContactUsViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseResponse> contactUs(String phone, String userId) {
        LiveData<BaseResponse> liveData = null;
        if (message.getValue() != null) {
            ContactUsRequest contactUsRequest = new ContactUsRequest();
            contactUsRequest.setMessage(message.getValue() + " " + "\n" + phone);
            contactUsRequest.setUseId(userId);
            contactUsRequest.setLang(resourceProvider.getCountryLang());

            liveData = repository.contactUs(contactUsRequest);
        }
        return liveData;
    }
}
