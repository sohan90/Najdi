package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductDetailViewModel extends BaseViewModel {
    public MutableLiveData<String> totalPrice = new MutableLiveData<>();
    public MutableLiveData<Integer> quantityCount = new MutableLiveData<>();
    private String selectOptionPrice;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        quantityCount.setValue(0);
    }

    public void setDefaultPrice(String defaultPrice) {
        selectOptionPrice = defaultPrice;
    }

    public void increamentQuantity() {
        if (quantityCount.getValue() != null) {
            quantityCount.setValue(quantityCount.getValue() + 1);
        }
        updateTotalPrice(selectOptionPrice);
    }

    public void decreamentQuantity() {
        if (quantityCount.getValue() != null && quantityCount.getValue() > 0) {
            quantityCount.setValue(quantityCount.getValue() - 1);
        }
        updateTotalPrice(selectOptionPrice);
    }

    public void updatePrice(ProductListResponse productListResponse, String selectedValue) {
        parent:
        for (ProductListResponse.VariationData variationData : productListResponse.getVariationsData()) {
            HashMap<String, String> variationType = variationData.getAttributes();
            for (Map.Entry<String, String> entry : variationType.entrySet()) {
                if (entry != null && entry.getValue().equalsIgnoreCase(selectedValue)) {
                    selectOptionPrice = variationData.getVariationRegularPrice();
                    updateTotalPrice(selectOptionPrice);
                    break parent;
                }
            }
        }
    }

    private void updateTotalPrice(String selectedOptionPrice) {
        if (selectedOptionPrice != null && quantityCount.getValue() != null) {
            int price = Integer.parseInt(selectedOptionPrice);
            int totPrice = price * quantityCount.getValue();
            totalPrice.setValue(String.valueOf(totPrice));
        }

    }
}
