package com.najdi.android.najdiapp.home.model;

public class CartRequest {
    private String product_id;
    private int qty;
    //new changes
    private String user_id;
    private String attributes;
    private String product_attribute_options;
    private String price;
    private String subtotal;
    private String lang;

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public void setProductAttributeOptions(String product_attribute_options) {
        this.product_attribute_options = product_attribute_options;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public void setProductId(String product_id) {
        this.product_id = product_id;
    }


    public void setQuantity(int quantity) {
        this.qty = quantity;
    }


}
