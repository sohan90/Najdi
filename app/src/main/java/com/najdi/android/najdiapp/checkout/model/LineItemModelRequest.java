package com.najdi.android.najdiapp.checkout.model;

import java.util.HashMap;

public class LineItemModelRequest {
    int product_id;
    int quantity;
    HashMap<String, String> variations;

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVariations(HashMap<String, String> variations) {
        this.variations = variations;
    }
}
