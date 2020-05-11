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

    private MutableLiveData<Boolean> getVariaitionQuantity;
    private int basePrice;
    private HashMap<String, String> attributHashMap;
    private int variationId;
    private int setMaxvariationQuantity;
    private String attriId;
    private int selectedOptionPrice;
    private int totalAttribute;
    private int totalPrice;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        quantityCount.setValue(0);
    }

    public void setDefaultPrice(String defaultPrice) {
        basePrice = Integer.parseInt(defaultPrice);
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
        if (quantityCount.getValue() != null) {//&& quantityCount.getValue() <= setMaxvariationQuantity) {

            quantityCount.setValue(quantityCount.getValue() + 1);
            updateTotalPrice();
            enableAddCartButton();
        }
    }

    public void decrementQuantity() {
        if (quantityCount.getValue() != null) {//&& quantityCount.getValue() > 1) {
            quantityCount.setValue(quantityCount.getValue() - 1);
        }
        updateTotalPrice();
        enableAddCartButton();
    }

    public void setMaxVariationQuantity(int quantity) {
        this.setMaxvariationQuantity = quantity;
    }

    public int getSetMaxvariationQuantity() {
        return setMaxvariationQuantity;
    }

    public void updatePrice(int totalAttribute, String selectedAttrId, AttributeOptionModel attributeOptionModel) {
        if (attributeOptionModel == null) return;
        this.totalAttribute = totalAttribute;
        int attrOptPrice = Integer.parseInt(attributeOptionModel.getOptionPrice());
        if (TextUtils.isEmpty(attriId) || attriId.equals(selectedAttrId)) {
            selectedOptionPrice = attrOptPrice;
        } else {
            selectedOptionPrice = selectedOptionPrice + attrOptPrice;
        }
        updateTotalPrice();
        attriId = selectedAttrId;
        createAttributeForSelectedValue(selectedAttrId, attributeOptionModel.getId());
    }

    private void getQuantityLimitForSelectedVariation() {
        getVariaitionQuantity.setValue(true);
    }

    public int getVariationId() {
        return variationId;
    }

    private void createAttributeForSelectedValue(String selectedValue,
                                                 String optAttrId) {
        createHashMap();
        attributHashMap.put(selectedValue, optAttrId);
        enableAddCartButton();
    }

    private void enableAddCartButton() {
        if (quantityCount.getValue() != null && quantityCount.getValue() > 0 &&
                attributHashMap != null && attributHashMap.size() == totalAttribute) {
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
            totalPrice = (this.basePrice + selectedOptionPrice) * quantityCount.getValue();
            totalPriceLivdData.setValue(getCurrencyConcatenatedString(String.valueOf(totalPrice)));
        }
    }

    public void reset() {
        if (attributHashMap == null) return;
        attributHashMap.clear();
        quantityCount.setValue(0);
        incrementQuantity();
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
            cartRequest.setAttributes(attributeIdList.toString().replace("[","")
            .replace("]",""));
            cartRequest.setProductAttributeOptions(attributeOptIdList.toString()
                    .replace("[","").replace("]",""));
            cartRequest.setPrice(String.valueOf(basePrice));
            cartRequest.setSubtotal(String.valueOf(totalPrice));
            cartRequest.setQuantity(quantityCount.getValue());

        }
        return repository.addToCart(cartRequest);
    }

    public LiveData<BaseResponse> removeCart(String itemKey) {
        String userId = String.valueOf(PreferenceUtils.getValueInt(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cart_item_key", itemKey);
        hashMap.put("customer", userId);
        return repository.removeCartItem(hashMap);
    }


    public MutableLiveData<Boolean> getGetVariaitionQuantity() {
        getVariaitionQuantity = new MutableLiveData<>();
        return getVariaitionQuantity;
    }

    public LiveData<ProductListResponse> getVariationQuantity(String productId, int variationId) {
        return repository.getVariationForProduct(productId, variationId);
    }
}
