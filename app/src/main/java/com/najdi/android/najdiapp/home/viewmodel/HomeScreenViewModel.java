package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeScreenViewModel extends BaseViewModel {

    private MutableLiveData<List<ProductListResponse>> productListLivedata;
    private MutableLiveData<ProductListResponse> launchProductDetailLiveData;
    private MutableLiveData<Boolean> showCartImageLiveData;

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

    public MutableLiveData<Boolean> showCartImageLiveData() {
        if (showCartImageLiveData == null) {
            showCartImageLiveData = new MutableLiveData<>();
        }
        return showCartImageLiveData;
    }


}
