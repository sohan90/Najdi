package com.najdi.android.najdiapp.home.model;

import java.util.List;

public class ProductModelResponse  {
    boolean status;
    String message;
    List<ProductListResponse> products;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ProductListResponse> getProductList() {
        return products;
    }
}
