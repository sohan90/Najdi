package com.najdi.android.najdiapp.shoppingcart.model;

import java.util.HashMap;
import java.util.List;

public class CartResponse {
    int code;
    String message;
    Data data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        List<CartData> cartdata;

        public List<CartData> getCartdata() {
            return cartdata;
        }
    }

    public class CartData {
        int product_id;
        int variation_id;
        HashMap<String, String> variation;
        int quantity;
        String tm_epo_product_original_price;
        String tm_epo_options_prices;
        String tm_epo_product_price_with_options;
        String tm_cart_item_key;
        int line_total;
        int line_subtotal;
        int line_tax;
        int line_subtotal_tax;
        String post_name;
        String post_image_url;

        public int getProductId() {
            return product_id;
        }

        public int getVariationId() {
            return variation_id;
        }

        public String getPostName() {
            return post_name;
        }

        public String getPostImageUrl() {
            return post_image_url;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getTm_epo_product_original_price() {
            return tm_epo_product_original_price;
        }

        public String getTm_epo_options_prices() {
            return tm_epo_options_prices;
        }

        public String getTm_epo_product_price_with_options() {
            return tm_epo_product_price_with_options;
        }

        public String getTm_cart_item_key() {
            return tm_cart_item_key;
        }

        public int getLineTotal() {
            return line_total;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getLine_subtotal() {
            return line_subtotal;
        }

        public int getLine_tax() {
            return line_tax;
        }

        public int getLine_subtotal_tax() {
            return line_subtotal_tax;
        }
    }
}
