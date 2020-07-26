package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;

public class AboutViewModel extends BaseViewModel {
    public AboutViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseResponse> getAboutUs() {
        return repository.getAboutUs();
    }

    public LiveData<BaseResponse> privacyPolicy() {
        return repository.getPrivacyPolicy();
    }

    public LiveData<BaseResponse> termsCondition() {
        return repository.getTermsCondition();
    }

}
