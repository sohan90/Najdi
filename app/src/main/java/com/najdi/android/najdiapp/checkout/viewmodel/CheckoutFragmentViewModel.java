package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CheckoutFragmentViewModel extends BaseViewModel {
    private MutableLiveData<String> subTotal = new MutableLiveData<>();
    private MutableLiveData<String> totalLiveData = new MutableLiveData<>();

    public CheckoutFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getSubTotal() {
        return subTotal;
    }

    public MutableLiveData<String> getTotal() {
        return totalLiveData;
    }

    public void udpateTotal(List<CartResponse.CartData> cartDataList) {
        int total = 0;
        for (CartResponse.CartData cartData : cartDataList) {
            total += cartData.getLine_subtotal();
        }
        String totalStr = String.valueOf(total).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

        subTotal.setValue(totalStr);
        totalLiveData.setValue(totalStr);
    }

    public LiveData<BaseResponse> removeCart(String cartItemKey) {
        String userId = String.valueOf(PreferenceUtils.getValueInt(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cart_item_key", cartItemKey);
        hashMap.put("customer", userId);
        return repository.removeCartItem(hashMap);
    }

    public LiveData<BaseResponse> updateQuantity(UpdateCartRequest updateCartRequest) {
        return repository.updateItemQuantity(updateCartRequest);
    }
}
