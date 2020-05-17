package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;

public class AboutViewModel extends BaseViewModel {
    private static final int ABOUT_US_ENG = 576;
    private static final int ABOUT_US_ARABIC = 1092;
    private static final int TERMS_ENG = 238;
    private static final int TERMS_ARABIC = 2533;
    private static final int PRIVACY_ENG = 2531;
    private static final int PRIVACY_ARABIC = 2536;

    private MutableLiveData<String> content = new MutableLiveData<>();

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

    public void setHtmlContent(String content){
        this.content.setValue(content);
    }

    public MutableLiveData<String> getContent() {
        return content;
    }
}
