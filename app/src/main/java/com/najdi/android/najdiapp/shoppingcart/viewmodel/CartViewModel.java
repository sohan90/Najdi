package com.najdi.android.najdiapp.shoppingcart.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CartViewModel extends BaseViewModel {
    private MutableLiveData<String> cartItemKeyLiveData;
    private MutableLiveData<String> subtotalLiveData = new MutableLiveData<>();
    private MutableLiveData<String> totalLiveData = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getCartItemKeyLiveData() {
        if (cartItemKeyLiveData == null) {
            cartItemKeyLiveData = new MutableLiveData<>();
        }
        return cartItemKeyLiveData;
    }

    public MutableLiveData<String> getSubtotalLiveData() {
        return subtotalLiveData;
    }

    public MutableLiveData<String> getTotalLiveData() {
        return totalLiveData;
    }

    public void setTotal(List<CartResponse.CartData> cartDataList) {
        int subTotal = 0;
        for (CartResponse.CartData cartData : cartDataList) {
            int total = Integer.parseInt(cartData.getTm_epo_product_price_with_options());
            subTotal = total + subTotal;
        }
        subtotalLiveData.setValue(String.valueOf(subTotal));
        totalLiveData.setValue(String.valueOf(subTotal));
    }

    public LiveData<BaseResponse> removeCart(String itemKey) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cart_item_key", itemKey);
        return repository.removeCartItem(hashMap);
    }
}
