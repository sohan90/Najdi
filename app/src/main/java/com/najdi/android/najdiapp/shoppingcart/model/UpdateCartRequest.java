package com.najdi.android.najdiapp.shoppingcart.model;

public class UpdateCartRequest {
    String cart_item_key;
    String quantity;

    public void setCartItemKey(String cartItemKey) {
        this.cart_item_key = cartItemKey;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCart_item_key() {
        return cart_item_key;
    }
}
