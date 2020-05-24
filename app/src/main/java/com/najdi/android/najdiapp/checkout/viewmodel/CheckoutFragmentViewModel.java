package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.CouponRequest;
import com.najdi.android.najdiapp.checkout.model.CouponResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;

import java.util.HashMap;
import java.util.List;

public class CheckoutFragmentViewModel extends BaseViewModel {
    private MutableLiveData<String> subTotal = new MutableLiveData<>();
    private MutableLiveData<String> totalLiveData = new MutableLiveData<>();
    private MutableLiveData<String> couponCode = new MutableLiveData<>();
    private MutableLiveData<String> couponAmt = new MutableLiveData<>();
    private MutableLiveData<List<CartResponse.CartData>> adapterList = new MutableLiveData<>();

    public CheckoutFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getSubTotal() {
        return subTotal;
    }

    public MutableLiveData<String> getCouponCode() {
        return couponCode;
    }

    public MutableLiveData<String> getCouponAmt() {
        return couponAmt;
    }

    public MutableLiveData<String> getTotal() {
        return totalLiveData;
    }

    //Response from server "details": "Size:Full,Cutting way:Quarters,Head & Legs:Skin Removed,Packaging:Normal"
    public LiveData<List<CartResponse.CartData>> getVariationDetails(List<CartResponse.CartData> cartDataList) {
        for (CartResponse.CartData cartData : cartDataList) {
            HashMap<String, String> map = new HashMap<>();
            String[] subDetail = cartData.getDetails().split(",");
            for (String s : subDetail) {
                String[] variation = s.split(":");//"Size:Full
                map.put(variation[0], variation[1]);//Size, Full
                cartData.setVariation(map);
            }
        }
        adapterList.setValue(cartDataList);
        return adapterList;
    }

    public void udpateTotal(List<CartResponse.CartData> cartDataList) {
        float total = 0;
        for (CartResponse.CartData cartData : cartDataList) {
            total += Float.parseFloat(cartData.getSubtotalWithQtyPrc());
        }
        String totalStr = String.valueOf(total).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

        subTotal.setValue(totalStr);
        totalLiveData.setValue(totalStr);
    }

    public LiveData<BaseResponse> removeCart(String userId, String cartItemKey) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", cartItemKey);
        hashMap.put("user_id", userId);
        return repository.removeCartItem(hashMap);
    }

    public LiveData<BaseResponse> updateQuantity(UpdateCartRequest updateCartRequest) {
        return repository.updateItemQuantity(updateCartRequest);
    }

    public LiveData<CouponResponse> applyCoupon(CouponRequest couponRequest) {
        return repository.applyCoupon(couponRequest);
    }

    public LiveData<BaseResponse> removeCoupon(CouponRequest couponRequest) {
        return repository.removeCoupon(couponRequest);
    }

    public void updateCoupon(String couponCode, String discountAmount) {
        getCouponCode().setValue(couponCode);
        String disAmt = discountAmount.concat(" ")
                .concat(resourceProvider.getString(R.string.currency));
        getCouponAmt().setValue(disAmt);
    }
}
