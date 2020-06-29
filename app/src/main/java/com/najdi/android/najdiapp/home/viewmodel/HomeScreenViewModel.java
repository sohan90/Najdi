package com.najdi.android.najdiapp.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.checkout.model.OrderStatus;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.BaseViewModel;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.model.ProductModelResponse;
import com.najdi.android.najdiapp.repository.Repository;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreenViewModel extends BaseViewModel {

    private MutableLiveData<List<ProductListResponse>> productListLivedata;
    private MutableLiveData<ProductDetailBundleModel> launchProductDetailLiveData;
    private MutableLiveData<Integer> showCartImageLiveData;
    private MutableLiveData<Integer> replaceFragmentLiveData;
    private MutableLiveData<Boolean> setHomeScreenToolBarLiveData;
    private MutableLiveData<String> setToolBarTitle;
    private MutableLiveData<Boolean> launchCheckoutActivity;
    private MutableLiveData<Boolean> cartCountNotification = new MutableLiveData<>();
    private MutableLiveData<String>  name = new MutableLiveData<>();
    int cartSize;

    public HomeScreenViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ProductModelResponse> getProducts() {
        return repository.getProducts();
    }

    public LiveData<ProductModelResponse> getCityBasedProducts(String cityId) {
        return repository.getCityBasedProducts(cityId);
    }

    public LiveData<ProductModelResponse> getCategoryBasedProducts(String lang, String catId) {
        return repository.getCategoryBasedProducts(lang, catId);
    }

    public LiveData<CityListModelResponse> getCityList(String lang) {
        return repository.getCityList(lang);
    }

    public LiveData<CityListModelResponse> getCategoryList(String lang) {
        return repository.getCategory(lang);
    }


    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<List<ProductListResponse>> getProductList() {
        if (productListLivedata == null) {
            productListLivedata = new MutableLiveData<>();
        }
        return productListLivedata;
    }

    public MutableLiveData<ProductDetailBundleModel> getLaunchProductDetailLiveData() {
        if (launchProductDetailLiveData == null) {
            launchProductDetailLiveData = new MutableLiveData<>();
        }
        return launchProductDetailLiveData;
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

    public LiveData<BaseResponse> getOrderStatus(String userId) {
        return repository.getOrderStatus(userId);
    }

    public LiveData<List<OrderStatus.Detail>> getAdapterList(List<OrderStatus> orderStatusList) {
        MutableLiveData<List<OrderStatus.Detail>> liveData = new MutableLiveData<>();
        List<OrderStatus.Detail> detailList = new ArrayList<>();
        for (OrderStatus orderStatus : orderStatusList) {
            for (OrderStatus.Detail detail : orderStatus.getDetails()) {
                detail.orderId = orderStatus.getOrder_id();
                detail.orderStatusLabel = orderStatus.getOrderStatusLabel();
                detail.orderStatus = orderStatus.getOrder_status();
                detail.paymentMethod = orderStatus.getPayment_method();
                detail.totalPrice = orderStatus.getTotal_price();
                detailList.add(detail);
            }
        }
        liveData.setValue(detailList);
        return liveData;
    }


    public LiveData<BaseResponse> getIndividualProduct(String productId) {
        return repository.getIndividualProduct(productId);
    }

    public MutableLiveData<Boolean> getCartCountNotification() {
        return cartCountNotification;
    }

    public LiveData<BaseResponse> getCartCount() {
        return repository.getCartCount();
    }

    public void sortProduct(List<ProductListResponse> list) {

        Comparator<ProductListResponse> COMPARATOR = (o1, o2) -> {
            int totalSale1;
            int totalSale2;
            try {
                totalSale1 = Integer.parseInt(o1.getTotal_sales());
                totalSale2 = Integer.parseInt(o2.getTotal_sales());
            } catch (NumberFormatException e) {
                return 0;
            }
            return totalSale2 - totalSale1;
        };

        Collections.sort(list, COMPARATOR);
    }

    public Repository provideRepo(){
        return repository;
    }

}
