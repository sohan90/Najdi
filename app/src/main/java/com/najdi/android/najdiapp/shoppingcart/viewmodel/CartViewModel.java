package com.najdi.android.najdiapp.shoppingcart.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;

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
            int total = cartData.getLineTotal();
            subTotal = total + subTotal;
        }
        String total = String.valueOf(subTotal).concat(" ").
                concat(resourceProvider.getString(R.string.currency));
        subtotalLiveData.setValue(total);
        totalLiveData.setValue(total);
    }

    public LiveData<BaseResponse> removeCart(String itemKey) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cart_item_key", itemKey);
        return repository.removeCartItem(hashMap);
    }

    public LiveData<ProductListResponse> getIndividualProduct(int productId) {
        return repository.getIndividualProduct(productId);
    }

    public LiveData<BaseResponse> updateQuantity(UpdateCartRequest updateCartRequest) {
        return repository.updateItemQuantity(updateCartRequest);
    }

}
