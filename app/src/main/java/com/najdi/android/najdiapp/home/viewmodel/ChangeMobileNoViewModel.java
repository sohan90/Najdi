package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ChangeMobileNoViewModel extends BaseViewModel {
    MutableLiveData<String> newMobileNo = new MutableLiveData<>();
    MutableLiveData<String> oldMobileNo = new MutableLiveData<>();


    public MutableLiveData<String> getNewMobileNo() {
        return newMobileNo;
    }

    public MutableLiveData<String> getOldMobileNo() {
        return oldMobileNo;
    }

    public ChangeMobileNoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseResponse> updateMobileNo(String lang) {
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setMobile(oldMobileNo.getValue());
        forgotPaswwordRequest.setNew_mobile(newMobileNo.getValue());
        forgotPaswwordRequest.setLang(lang);
        return repository.mobileChange(forgotPaswwordRequest);
    }

}
