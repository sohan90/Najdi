package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;

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

    public boolean validate() {
        boolean isValid = false;
        if (oldMobileNo.getValue() != null && oldMobileNo.getValue().startsWith("5")
                && oldMobileNo.getValue().length() >= 8) {

            if (newMobileNo.getValue() != null && newMobileNo.getValue().startsWith("5")
                    && newMobileNo.getValue().length() >= 8) {

                isValid = true;
            }
        }

        return isValid;
    }

    public LiveData<BaseResponse> updateMobileNo(String lang, String userId) {
        ForgotPaswwordRequest forgotPaswwordRequest = new ForgotPaswwordRequest();
        forgotPaswwordRequest.setMobile(newMobileNo.getValue());
        forgotPaswwordRequest.setLang(lang);
        forgotPaswwordRequest.setUserId(userId);
        return repository.mobileChange(forgotPaswwordRequest);
    }

}
