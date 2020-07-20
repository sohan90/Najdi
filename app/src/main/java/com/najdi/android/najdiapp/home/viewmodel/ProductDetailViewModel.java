package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.AttributeOptionModel;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailViewModel extends BaseViewModel {

    public MutableLiveData<String> totalPriceLivdData = new MutableLiveData<>();
    public MutableLiveData<Integer> quantityCount = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableAddCartButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableProceed = new MutableLiveData<>();
    public MutableLiveData<String> notes = new MutableLiveData<>();

    private MutableLiveData<Boolean> getVariaitionQuantity;
    private float basePrice;
    private HashMap<String, String> attributHashMap;
    private int setMaxvariationQuantity;
    private int inStock;
    private int totalAttribute;
    private float totalPrice;
    private HashMap<String, Float> attrOptPriceMap = new HashMap<>();

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        quantityCount.setValue(0);
    }

    public void setDefaultPrice(String defaultPrice) {
        basePrice = Float.parseFloat(defaultPrice);
    }

    public void setTotalAttributOptSize(int attributOptSize) {
        this.totalAttribute = attributOptSize;
        createHashMap();
        enableAddCartButton();
    }

    public void setDefaultQuantity() {
        quantityCount.setValue(0);
        incrementQuantity();
    }

    public void setTotalPriceLivdData(String totalPriceLivdData) {
        this.totalPriceLivdData.setValue(getCurrencyConcatenatedString(totalPriceLivdData));
    }

    private String getCurrencyConcatenatedString(String price) {
        return price.concat(" ").concat(resourceProvider
                .getString(R.string.currency));
    }

    public void setQuantityCount(int quantity) {
        this.quantityCount.setValue(quantity);
    }

    public void incrementQuantity() {
        if (quantityCount.getValue() != null && quantityCount.getValue() <= setMaxvariationQuantity) {
            quantityCount.setValue(quantityCount.getValue() + 1);
            updateTotalPrice();
            enableAddCartButton();
        }
    }

    public void decrementQuantity() {
        if (quantityCount.getValue() != null && quantityCount.getValue() > 1) {
            quantityCount.setValue(quantityCount.getValue() - 1);
        }
        updateTotalPrice();
        enableAddCartButton();
    }

    public void setMaxVariationQuantity(int quantity) {
        this.setMaxvariationQuantity = quantity;
    }

    public  void inStock(int stock){
        inStock = stock;
    }


    public void updatePrice(String selectedAttrId, AttributeOptionModel
            attributeOptionModel) {

        if (attributeOptionModel == null) return;

        float attrOptPrice = Float.parseFloat(TextUtils.isEmpty(attributeOptionModel.getOptionPrice())
                ? "0" : attributeOptionModel.getOptionPrice());
        attrOptPriceMap.put(selectedAttrId, attrOptPrice);
        updateTotalPrice();
        createAttributeForSelectedValue(selectedAttrId, attributeOptionModel.getId());
    }

    public int getVariationId() {
        return 0;
    }

    private void createAttributeForSelectedValue(String selectedValue,
                                                 String optAttrId) {

        attributHashMap.put(selectedValue, optAttrId);
        enableAddCartButton();
    }

    private void enableAddCartButton() {
        if (quantityCount.getValue() != null && quantityCount.getValue() > 0 &&
                attributHashMap != null && attributHashMap.size() == totalAttribute
                && inStock > 0) {

            enableAddCartButton.setValue(true);

        } else {

            enableAddCartButton.setValue(false);
        }
    }

    private void createHashMap() {
        if (attributHashMap == null) {
            attributHashMap = new HashMap<>();
        }
    }

    private void updateTotalPrice() {
        if (quantityCount.getValue() != null) {
            float totalSelectionPrice = getPriceFromMap();
            totalPrice = (this.basePrice + totalSelectionPrice) * quantityCount.getValue();
            totalPriceLivdData.setValue(getCurrencyConcatenatedString(String.valueOf(totalPrice)));
        }
    }

    private float getPriceFromMap() {
        float totalPrice = 0;
        for (Map.Entry<String, Float> map : attrOptPriceMap.entrySet()) {
            totalPrice = totalPrice + map.getValue();
        }
        return totalPrice;
    }

    public void reset() {
        if (attributHashMap == null) return;
        attributHashMap.clear();
        quantityCount.setValue(0);
        notes.setValue("");// reset notes
        incrementQuantity();
        attrOptPriceMap.clear();// clear attribute price
        updateTotalPrice();
    }

    public LiveData<BaseResponse> addToCart(String productId) {
        if (attributHashMap == null) return null;
        List<String> attributeIdList = new ArrayList<>();
        List<String> attributeOptIdList = new ArrayList<>();
        for (Map.Entry<String, String> map : attributHashMap.entrySet()) {
            attributeIdList.add(map.getKey());
            attributeOptIdList.add(map.getValue());
        }
        CartRequest cartRequest = new CartRequest();
        if (quantityCount.getValue() != null) {
            cartRequest.setProductId(productId);
            cartRequest.setAttributes(attributeIdList.toString().replace("[", "")
                    .replace("]", ""));
            cartRequest.setProductAttributeOptions(attributeOptIdList.toString()
                    .replace("[", "").replace("]", ""));
            cartRequest.setPrice(String.valueOf(basePrice));
            cartRequest.setSubtotal(String.valueOf(totalPrice));
            cartRequest.setQuantity(quantityCount.getValue());
            cartRequest.setNotes(notes.getValue());

        }
        return repository.addToCart(cartRequest);
    }

    public LiveData<BaseResponse> removeCart(String itemKey) {
        String userId = String.valueOf(PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", itemKey);
        hashMap.put("user_id", userId);
        return repository.removeCartItem(hashMap);
    }


    @Deprecated
    public MutableLiveData<Boolean> getGetVariaitionQuantity() {
        getVariaitionQuantity = new MutableLiveData<>();
        return getVariaitionQuantity;
    }

    public LiveData<ProductListResponse> getVariationQuantity(String productId, int variationId) {
        return repository.getVariationForProduct(productId, variationId);
    }
}
