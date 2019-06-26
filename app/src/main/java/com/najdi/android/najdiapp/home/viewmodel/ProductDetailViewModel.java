package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.ProductListResponse;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.najdi.android.najdiapp.common.Constants.APPEND_ATTRIBUTE_STR;

public class ProductDetailViewModel extends BaseViewModel {
    public MutableLiveData<String> totalPrice = new MutableLiveData<>();
    public MutableLiveData<Integer> quantityCount = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableAddCartButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableProceed = new MutableLiveData<>();
    private String selectOptionPrice;
    private HashMap<String, String> attributHashMap;
    private ProductListResponse productListResponse;
    private int variationId;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        quantityCount.setValue(0);
    }

    public void setDefaultPrice(String defaultPrice) {
        selectOptionPrice = defaultPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice.setValue(getCurrencyConcatintedString(totalPrice));
    }

    private String getCurrencyConcatintedString(String price){
        String priceCurrency = price.concat(" ").
                concat(resourceProvider.getString(R.string.currency));
        return priceCurrency;
    }

    public void setQuantityCount(int quantity) {
        this.quantityCount.setValue(quantity);
    }

    public void incrementQuantity() {
        if (quantityCount.getValue() != null) {
            quantityCount.setValue(quantityCount.getValue() + 1);
        }
        updateTotalPrice(selectOptionPrice);
        enableAddCartButton();
    }


    public void decrementQuantity() {
        if (quantityCount.getValue() != null && quantityCount.getValue() > 0) {
            quantityCount.setValue(quantityCount.getValue() - 1);
        }
        updateTotalPrice(selectOptionPrice);
        enableAddCartButton();
    }

    public void updatePrice(ProductListResponse productListResponse, String selectedValue, int selectedId) {
        this.productListResponse = productListResponse;
        createAttributeForSelectedValue(selectedValue, selectedId);
        parent:
        for (ProductListResponse.VariationData variationData : productListResponse.getVariationsData()) {
            HashMap<String, String> variationType = variationData.getAttributes();
            for (Map.Entry<String, String> entry : variationType.entrySet()) {
                if (entry.getValue().equalsIgnoreCase(selectedValue)) {
                    selectOptionPrice = variationData.getVariationRegularPrice();
                    variationId = variationData.getVariation_id();
                    updateTotalPrice(selectOptionPrice);
                    break parent;
                }
            }
        }
    }

    private void createAttributeForSelectedValue(String selectedValue, int selectedId) {
        createHashMap();
        for (ProductListResponse.Attributes attributes : productListResponse.getAttributesList()) {
            if (attributes.getId() == selectedId) {
                attributHashMap.put(APPEND_ATTRIBUTE_STR + attributes.getSlug(), selectedValue);
                break;
            }
        }
        enableAddCartButton();
    }

    private void enableAddCartButton() {
        if (quantityCount.getValue() != null && quantityCount.getValue() > 0 &&
                attributHashMap != null && attributHashMap.size() == productListResponse
                .getAttributesList().size()) {
            enableAddCartButton.setValue(true);
            enableProceed.setValue(true);
        } else {
            enableAddCartButton.setValue(false);
            enableProceed.setValue(false);
        }
    }

    private void createHashMap() {
        if (attributHashMap == null) {
            attributHashMap = new HashMap<>();
        }
    }

    private void updateTotalPrice(String selectedOptionPrice) {
        if (selectedOptionPrice != null && quantityCount.getValue() != null) {
            int price = Integer.parseInt(selectedOptionPrice);
            int totPrice = price * quantityCount.getValue();
            totalPrice.setValue(getCurrencyConcatintedString(String.valueOf(totPrice)));
        }
    }

    public void reset() {
        if (attributHashMap == null) return;
        attributHashMap.clear();
        quantityCount.setValue(0);
        totalPrice.setValue("");
        enableAddCartButton();
    }

    public LiveData<BaseResponse> addToCart() {
        if (quantityCount.getValue() != null && productListResponse != null) {
            CartRequest cartRequest = new CartRequest();
            cartRequest.setQuantity(quantityCount.getValue());
            cartRequest.setProductId(productListResponse.getId());
            cartRequest.setVariationId(variationId);
            cartRequest.setVariation(attributHashMap);
            return repository.addToCart(cartRequest);
        }
        return null;
    }

    public LiveData<BaseResponse> removeCart(String itemKey) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cart_item_key", itemKey);
        return repository.removeCartItem(hashMap);
    }

}
