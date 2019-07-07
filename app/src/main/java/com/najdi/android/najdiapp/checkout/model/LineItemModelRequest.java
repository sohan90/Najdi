package com.najdi.android.najdiapp.checkout.model;

import java.util.HashMap;
import java.util.List;

public class LineItemModelRequest {
    int product_id;
    int variation_id;
    int quantity;
    List<HashMap<String, String>> meta_data;

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setVariation_id(int variation_id) {
        this.variation_id = variation_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVariations(List<HashMap<String, String>> variations) {
        this.meta_data = variations;
    }
}
