package com.najdi.android.najdiapp.shoppingcart.model;

public class UpdateCartRequest {
    String id;
    String qty;
    String user_id;
    String lang;

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String cartId) {
        this.id = cartId;
    }

    public void setQuantity(String quantity) {
        this.qty = quantity;
    }

}
