package com.najdi.android.najdiapp.home.model;

import org.json.JSONObject;

import java.util.HashMap;

public class CartRequest {
    int product_id;
    int variation_id;
    int quantity;
    HashMap<String, String> variation;

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    public void setVariationId(int variation_id) {
        this.variation_id = variation_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVariation(HashMap<String, String> variation) {
        this.variation = variation;
    }
}
