package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class BankDetailViewModel extends BaseViewModel {

    public BankDetailViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseResponse> getBankDetail() {
        return repository.getBankDetails();
    }
}
