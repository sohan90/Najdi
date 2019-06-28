package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;
import android.location.Address;

import com.najdi.android.najdiapp.checkout.model.LineItemModelRequest;
import com.najdi.android.najdiapp.checkout.model.OrderRequest;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public LiveData<OrderResponse> createOrder(int userId, List<CartResponse.CartData> cartData,
                                               String paymentMode, BillingAddress billingAddress) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomer_id(userId);
        orderRequest.setBilling(billingAddress);
        orderRequest.setPayment_method(paymentMode);
        if (paymentMode.equalsIgnoreCase("cod")) {
            orderRequest.setPayment_method_title("Cash On Delivery");
        } else {
            orderRequest.setPayment_method_title("Direct Bank Transfer");
        }

        List<LineItemModelRequest> list = new ArrayList<>();
        for (CartResponse.CartData cartData1 : cartData) {
            LineItemModelRequest lineItemModelRequest = new LineItemModelRequest();
            lineItemModelRequest.setProduct_id(cartData1.getProductId());
            lineItemModelRequest.setQuantity(cartData1.getQuantity());
            lineItemModelRequest.setVariations(cartData1.getVariation());
            list.add(lineItemModelRequest);
        }

        orderRequest.setLine_items(list);
        return repository.createOrder(userId, orderRequest);
    }

    public MutableLiveData<OrderResponse> orderResponseMutableLiveData() {
        if (orderResponseMutableLiveData == null) {
            orderResponseMutableLiveData = new MutableLiveData<>();
        }
        return orderResponseMutableLiveData;
    }

    public LiveData<BaseResponse> getCartCount(){
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
