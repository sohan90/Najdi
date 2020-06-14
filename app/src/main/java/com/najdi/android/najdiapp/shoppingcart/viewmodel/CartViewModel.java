package com.najdi.android.najdiapp.shoppingcart.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.HashMap;
import java.util.List;

public class CartViewModel extends BaseViewModel {
    public static final String SHOW_TAX = "1";
    private MutableLiveData<String> cartItemKeyLiveData;
    private MutableLiveData<String> subtotalLiveData = new MutableLiveData<>();
    private MutableLiveData<String> totalLiveData = new MutableLiveData<>();
    private MutableLiveData<Float> taxAmount = new MutableLiveData<>();
    private MutableLiveData<Integer> showTaxUi = new MutableLiveData<>();
    private String showTax;


    public CartViewModel(@NonNull Application application) {
        super(application);
        showTaxUi.setValue(View.GONE);
    }

    public MutableLiveData<String> getCartItemKeyLiveData() {
        if (cartItemKeyLiveData == null) {
            cartItemKeyLiveData = new MutableLiveData<>();
        }
        return cartItemKeyLiveData;
    }

    public MutableLiveData<Float> taxAmount() {
        return taxAmount;
    }

    public MutableLiveData<Integer> getShowTaxUi() {
        return showTaxUi;
    }

    public void setShowTax(String showTax, float taxAmount) {
        this.showTax = showTax;
        if (showTax.equals(SHOW_TAX)){
            this.showTaxUi.setValue(View.VISIBLE);
            this.taxAmount.setValue(taxAmount);
        }
    }

    public MutableLiveData<String> getSubtotalLiveData() {
        return subtotalLiveData;
    }

    public MutableLiveData<String> getTotalLiveData() {
        return totalLiveData;
    }

    public void setTotal(List<CartResponse.CartData> cartDataList) {
        float subTotal = 0;
        for (CartResponse.CartData cartData : cartDataList) {
            float total = Float.parseFloat(cartData.getSubtotalWithQtyPrc());
            subTotal = total + subTotal;
        }
        String subTotlStr = String.valueOf(subTotal).concat(" ").concat(resourceProvider.getString(R.string.currency));
        subtotalLiveData.setValue(subTotlStr);

        float total = subTotal;
        if (taxAmount.getValue() != null){
            total = subTotal + taxAmount.getValue();
        }
        String totalStr = String.valueOf(total).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

        totalLiveData.setValue(totalStr);
    }

    public LiveData<BaseResponse> removeCart(String itemKey) {
        String userId = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", itemKey);
        hashMap.put("user_id", userId);
        return repository.removeCartItem(hashMap);
    }


    public LiveData<BaseResponse> updateQuantity(UpdateCartRequest updateCartRequest) {
        return repository.updateItemQuantity(updateCartRequest);
    }

}
