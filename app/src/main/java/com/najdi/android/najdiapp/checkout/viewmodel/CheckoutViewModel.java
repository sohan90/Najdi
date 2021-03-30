package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;
import android.location.Address;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

public class CheckoutViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> getCurrentLocationUpdateLiveData;
    private MutableLiveData<Address> addressMutableLiveData;
    private MutableLiveData<Integer> progressPercentage;
    private MutableLiveData<CartResponse> cartResponseMutableLiveData;
    private MutableLiveData<BillingAddress> billingMutableLiveData;
    private MutableLiveData<String> checkoutLiveData;
    private MutableLiveData<OrderResponse> orderResponseMutableLiveData;
    private MutableLiveData<Boolean> updateCartLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> cartCountNotification = new MutableLiveData<>();
    private MutableLiveData<Boolean> hideCart = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getGetCurrentLocationUpdateLiveData() {
        if (getCurrentLocationUpdateLiveData == null) {
            getCurrentLocationUpdateLiveData = new MutableLiveData<>();
        }
        return getCurrentLocationUpdateLiveData;
    }

    public MutableLiveData<Address> getAddressMutableLiveData() {
        if (addressMutableLiveData == null) {
            addressMutableLiveData = new MutableLiveData<>();
        }
        return addressMutableLiveData;
    }

    public MutableLiveData<Integer> getProgressPercentage() {
        if (progressPercentage == null) {
            progressPercentage = new MutableLiveData<>();
        }
        return progressPercentage;
    }

    public LiveData<CartResponse> fetchCart() {
        return repository.getCart();
    }

    public MutableLiveData<Boolean> updateCart() {
        return updateCartLiveData;
    }

    public MutableLiveData<CartResponse> getCartResponseMutableLiveData() {
        if (cartResponseMutableLiveData == null) {
            cartResponseMutableLiveData = new MutableLiveData<>();
        }
        return cartResponseMutableLiveData;
    }

    public MutableLiveData<BillingAddress> getBillingMutableLiveData() {
        if (billingMutableLiveData == null) {
            billingMutableLiveData = new MutableLiveData<>();
        }
        return billingMutableLiveData;
    }

    public MutableLiveData<String> getCheckoutLiveData() {
        if (checkoutLiveData == null) {
            checkoutLiveData = new MutableLiveData<>();
        }
        return checkoutLiveData;
    }

    public LiveData<OrderResponse> createOrder(String userId, BillingAddress billingAddress) {
        return repository.createOrder(userId, billingAddress);
    }

    public MutableLiveData<OrderResponse> orderResponseMutableLiveData() {
        if (orderResponseMutableLiveData == null) {
            orderResponseMutableLiveData = new MutableLiveData<>();
        }
        return orderResponseMutableLiveData;
    }

    public LiveData<BaseResponse> getCartCount() {
        return repository.getCartCount();
    }

    public LiveData<BaseResponse> clearCart() {
        return repository.clearCart();
    }

    public MutableLiveData<Boolean> getCartCountNotification() {
        return cartCountNotification;
    }

    public MutableLiveData<Boolean> getHideCart() {
        return hideCart;
    }

}
