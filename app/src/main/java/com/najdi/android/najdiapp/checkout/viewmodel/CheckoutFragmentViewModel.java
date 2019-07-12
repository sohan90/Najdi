package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CheckoutFragmentViewModel extends BaseViewModel {
    private MutableLiveData<String> subTotal = new MutableLiveData<>();
    private MutableLiveData<String> totalLiveData = new MutableLiveData<>();
    private MutableLiveData<List<CartResponse.CartData>> adapterList = new MutableLiveData<>();

    public CheckoutFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getSubTotal() {
        return subTotal;
    }

    public MutableLiveData<String> getTotal() {
        return totalLiveData;
    }

    public LiveData<List<CartResponse.CartData>> sortvariation(CartResponse cartResponse) {
        List<CartResponse.CartData> cartdataList = cartResponse.getData().getCartdata();

        for (CartResponse.CartData cartData : cartdataList) {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            if (cartData.getVariation().containsKey("attribute_pa_size")) {
                hashMap.put("attribute_pa_size", cartData.getVariation().get("attribute_pa_size"));
            }
            if (cartData.getVariation().containsKey("attribute_pa_cutting_way")) {
                hashMap.put("attribute_pa_cutting_way", cartData.getVariation().get("attribute_pa_cutting_way"));
            }
            if (cartData.getVariation().containsKey("attribute_pa_head")) {
                hashMap.put("attribute_pa_head", cartData.getVariation().get("attribute_pa_head"));
            }
            if (cartData.getVariation().containsKey("attribute_pa_legs")) {
                hashMap.put("attribute_pa_legs", cartData.getVariation().get("attribute_pa_legs"));
            }
            hashMap.putAll(cartData.getVariation());
            cartData.setVariation(hashMap);
        }
        adapterList.setValue(cartdataList);
        return adapterList;
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
