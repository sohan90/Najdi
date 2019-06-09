package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeScreenViewModel extends BaseViewModel {

    private MutableLiveData<List<ProductListResponse>> productListLivedata;
    private MutableLiveData<ProductListResponse> launchProductDetailLiveData;
    private MutableLiveData<Integer> showCartImageLiveData;
    private MutableLiveData<Integer> replaceFragmentLiveData;
    private MutableLiveData<HashMap<String, String>> selectedVariationLiveData;
    private MutableLiveData<Boolean> setHomeScreenToolBarLiveData;
    private MutableLiveData<String> setToolBarTitle;
    private MutableLiveData<Boolean> launchCheckoutActivity;
    int cartSize;

    public HomeScreenViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ProductListResponse>> getProducts() {
        return repository.getProducts();
    }

    public MutableLiveData<List<ProductListResponse>> getProductList() {
        if (productListLivedata == null) {
            productListLivedata = new MutableLiveData<>();
        }
        return productListLivedata;
    }

    public MutableLiveData<ProductListResponse> getLaunchProductDetailLiveData() {
        if (launchProductDetailLiveData == null) {
            launchProductDetailLiveData = new MutableLiveData<>();
        }
        return launchProductDetailLiveData;
    }

    public MutableLiveData<HashMap<String, String>> getSelecteVariationOptionLiveData() {
        if (selectedVariationLiveData == null) {
            selectedVariationLiveData = new MutableLiveData<>();
        }
        return selectedVariationLiveData;
    }

    public MutableLiveData<Integer> updateNotificationCartCount() {
        if (showCartImageLiveData == null) {
            showCartImageLiveData = new MutableLiveData<>();
        }
        return showCartImageLiveData;
    }

    public MutableLiveData<Integer> getReplaceFragmentLiveData() {
        if (replaceFragmentLiveData == null) {
            replaceFragmentLiveData = new MutableLiveData<>();
        }
        return replaceFragmentLiveData;
    }

    public MutableLiveData<String> getSetToolBarTitle() {
        if (setToolBarTitle == null) {
            setToolBarTitle = new MutableLiveData<>();
        }
        return setToolBarTitle;
    }

    public MutableLiveData<Boolean> getSetHomeScreenToolBarLiveData() {
        if (setHomeScreenToolBarLiveData == null) {
            setHomeScreenToolBarLiveData = new MutableLiveData<>();
        }
        return setHomeScreenToolBarLiveData;
    }

    public MutableLiveData<Boolean> getLaunchCheckoutActivity() {
        if (launchCheckoutActivity == null) {
            launchCheckoutActivity = new MutableLiveData<>();
        }
        return launchCheckoutActivity;
    }

    public LiveData<CartResponse> getCart() {
        return repository.getCart();
    }

    public void setCartSize(int cartSize) {
        this.cartSize = cartSize;
    }

    public int getCartSize() {
        return cartSize;
    }
}
