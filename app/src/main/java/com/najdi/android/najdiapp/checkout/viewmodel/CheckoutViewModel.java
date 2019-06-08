package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;
import android.location.Address;

import com.najdi.android.najdiapp.common.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class CheckoutViewModel extends BaseViewModel {

    MutableLiveData<Boolean> getCurrentLocationUpdateLiveData;
    MutableLiveData<Address> addressMutableLiveData;

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getGetCurrentLocationUpdateLiveData() {
        if (getCurrentLocationUpdateLiveData == null) {
            getCurrentLocationUpdateLiveData = new MutableLiveData<>();
        }
        return getCurrentLocationUpdateLiveData;
    }

    public MutableLiveData<Address> getAddressMutableLiveData(){
        if (addressMutableLiveData == null){
            addressMutableLiveData = new MutableLiveData<>();
        }return addressMutableLiveData;
    }
}
