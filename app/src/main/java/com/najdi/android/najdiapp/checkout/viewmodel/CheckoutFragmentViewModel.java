package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.List;

import androidx.annotation.NonNull;
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
            total += cartData.getLineTotal();
        }
        subTotal.setValue(String.valueOf(total));
        totalLiveData.setValue(String.valueOf(total));
    }
}
