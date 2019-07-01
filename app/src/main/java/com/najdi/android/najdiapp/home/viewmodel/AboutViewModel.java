package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.home.model.HtmlResponseForNajdi;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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


    public LiveData<HtmlResponseForNajdi> getAboutUs() {
        int pageId = ABOUT_US_ENG;
        if (LocaleUtitlity.getCountryLang().equals(Constants.ARABIC_LAN)) {
            pageId = ABOUT_US_ARABIC;
        }
        return repository.getHtmlConent(pageId);
    }

    public LiveData<HtmlResponseForNajdi> privacyPolicy() {
        int pageId = PRIVACY_ENG;
        if (LocaleUtitlity.getCountryLang().equals(Constants.ARABIC_LAN)) {
            pageId = PRIVACY_ARABIC;
        }
        return repository.getHtmlConent(pageId);
    }

    public LiveData<HtmlResponseForNajdi> termsCondition() {
        int pageId = TERMS_ENG;
        if (LocaleUtitlity.getCountryLang().equals(Constants.ARABIC_LAN)) {
            pageId = TERMS_ARABIC;
        }
        return repository.getHtmlConent(pageId);
    }

    public void setHtmlContent(String content){
        this.content.setValue(content);
    }

    public MutableLiveData<String> getContent() {
        return content;
    }
}
