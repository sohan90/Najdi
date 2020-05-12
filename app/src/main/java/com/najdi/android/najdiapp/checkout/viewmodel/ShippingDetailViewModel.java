package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;
import android.location.Address;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.launch.model.BillingAddress;

public class ShippingDetailViewModel extends BaseViewModel {
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> phoneNo = new MutableLiveData<>();
    private MutableLiveData<String> street = new MutableLiveData<>();
    private MutableLiveData<String> buildingNO = new MutableLiveData<>();
    private MutableLiveData<String> city = new MutableLiveData<>();
    private MutableLiveData<String> province = new MutableLiveData<>();
    private MutableLiveData<String> postalCode = new MutableLiveData<>();
    private MutableLiveData<Boolean> proceed = new MutableLiveData<>();
    private Address googleAddress;


    public ShippingDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void updatePersonalInfo(String name, String email, String phoneNo) {
        this.name.setValue(name);
        this.email.setValue(email);
        this.phoneNo.setValue(phoneNo);
    }

    public void updateAddress(Address address) {
        googleAddress = address;
        buildingNO.setValue(address.getFeatureName());
        street.setValue(address.getSubLocality());
        city.setValue(address.getLocality());
        province.setValue(address.getAdminArea());
        postalCode.setValue(address.getPostalCode());
    }

    public MutableLiveData<String> getStreet() {
        return street;
    }

    public MutableLiveData<String> getBuildingNO() {
        return buildingNO;
    }

    public MutableLiveData<String> getCity() {
        return city;
    }

    public MutableLiveData<String> getProvince() {
        return province;
    }

    public MutableLiveData<String> getPostalCode() {
        return postalCode;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getPhoneNo() {
        return phoneNo;
    }

    public LiveData<Boolean> validate() {
        boolean canProceed = true;
        if (buildingNO.getValue() == null) {
            canProceed = false;
        } else if (city.getValue() == null) {
            canProceed = false;
        } else if (province.getValue() == null) {
            canProceed = false;
        } else if (postalCode.getValue() == null) {
            canProceed = false;
        } else if (street.getValue() == null) {
            canProceed = false;
        }
        proceed.setValue(canProceed);
        return proceed;
    }

    public BillingAddress getBillingObject() {
        BillingAddress billing = new BillingAddress();
        billing.setFull_name(name.getValue());
        billing.setEmail(email.getValue());
        billing.setPhone(phoneNo.getValue().replace("966",""));
        billing.setAddress(buildingNO.getValue());
        billing.setMap_address(street.getValue());
        billing.setCity(city.getValue());
        billing.setState(province.getValue());
        billing.setPostcode(postalCode.getValue());
        billing.setCountry(province.getValue());
        billing.setLat(String.valueOf(googleAddress.getLatitude()));
        billing.setLng(String.valueOf(googleAddress.getLongitude()));
        return billing;
    }
}

