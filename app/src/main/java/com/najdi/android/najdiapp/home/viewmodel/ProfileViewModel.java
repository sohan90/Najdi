package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.UpdateProfileModelRequest;

public class ProfileViewModel extends BaseViewModel {

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean validate(){
        boolean isValid = false;
        if (!TextUtils.isEmpty(name.getValue()) &&  !TextUtils.isEmpty(email.getValue())){
            isValid = true;
        }
        return isValid;
    }

    public LiveData<BaseResponse> updateProfile(String userId){
        UpdateProfileModelRequest request = new UpdateProfileModelRequest();
        request.setId(userId);
        request.setFullName(name.getValue());
        request.setEmail(email.getValue());
        request.setLang(resourceProvider.getCountryLang());
        return repository.updateProfile(request);
    }


}
