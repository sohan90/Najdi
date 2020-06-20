package com.najdi.android.najdiapp.checkout.viewmodel;

import android.app.Application;
import android.view.View;

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

import static com.najdi.android.najdiapp.shoppingcart.viewmodel.CartViewModel.SHOW_TAX;

public class CheckoutFragmentViewModel extends BaseViewModel {
    private MutableLiveData<String> subTotal = new MutableLiveData<>();
    private MutableLiveData<String> totalLiveData = new MutableLiveData<>();
    private MutableLiveData<String> couponCode = new MutableLiveData<>();
    private MutableLiveData<String> couponAmt = new MutableLiveData<>();
    private MutableLiveData<List<CartResponse.CartData>> adapterList = new MutableLiveData<>();
    private MutableLiveData<Float> taxAmount = new MutableLiveData<>();
    private MutableLiveData<Integer> showTaxUi = new MutableLiveData<>();
    private String showTax;

    public CheckoutFragmentViewModel(@NonNull Application application) {
        super(application);
        showTaxUi.setValue(View.GONE);
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
            if (cartData.getDetails() == null) continue;
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

    public void udpateTotal(List<CartResponse.CartData> cartDataList) {
        float subTotal = 0;
        for (CartResponse.CartData cartData : cartDataList) {
            subTotal += Float.parseFloat(cartData.getSubtotalWithQtyPrc());
        }
        String subTotalStr = String.valueOf(subTotal).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

        this.subTotal.setValue(subTotalStr);

        float total = subTotal;
        if (taxAmount.getValue() != null){
            total = total + taxAmount.getValue();
        }
        String totalStr = String.valueOf(total).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

        totalLiveData.setValue(totalStr);
    }

    public void updateTotal(String subTotal, String totalAmnt){
        String subTotalStr = String.valueOf(subTotal).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

        this.subTotal.setValue(subTotalStr);

        String totalStr = String.valueOf(totalAmnt).concat(" ").
                concat(resourceProvider.getString(R.string.currency));

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

        /*if (totalLiveData.getValue() == null) return;
        String price = totalLiveData.getValue().split(" ")[0];//total price is concatenated with currency
        float updatedPrice = Float.parseFloat(price) - Float.parseFloat(discountAmount);
        String value = String.valueOf(updatedPrice);
        totalLiveData.setValue(value.concat(" ")
                .concat(resourceProvider.getString(R.string.currency)));*/
    }
}
