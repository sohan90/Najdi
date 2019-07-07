package com.najdi.android.najdiapp.shoppingcart.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class CartData implements Parcelable {
        private int product_id;
        private int variation_id;
        private HashMap<String, String> variation;
        private int quantity;
        private String tm_epo_product_original_price;
        private String tm_epo_options_prices;
        private String tm_epo_product_price_with_options;
        private String tm_cart_item_key;
        private int line_total;
        private int line_subtotal;
        private int line_tax;
        private int line_subtotal_tax;
        private String post_name;
        private String post_title;
        private String post_image_url;

        //local data
        private int previousQuantity;
        private int previousTotal;


        protected CartData(Parcel in) {
            product_id = in.readInt();
            variation_id = in.readInt();
            quantity = in.readInt();
            tm_epo_product_original_price = in.readString();
            tm_epo_options_prices = in.readString();
            tm_epo_product_price_with_options = in.readString();
            tm_cart_item_key = in.readString();
            line_total = in.readInt();
            line_subtotal = in.readInt();
            line_tax = in.readInt();
            line_subtotal_tax = in.readInt();
            post_name = in.readString();
            post_image_url = in.readString();
            previousQuantity = in.readInt();
            post_title = in.readString();
            previousTotal = in.readInt();
        }

        public static final Creator<CartData> CREATOR = new Creator<CartData>() {
            @Override
            public CartData createFromParcel(Parcel in) {
                return new CartData(in);
            }

            @Override
            public CartData[] newArray(int size) {
                return new CartData[size];
            }
        };

        public void setPreviousTotal(int previousTotal) {
            this.previousTotal = previousTotal;
        }

        public int getPreviousTotal() {
            return previousTotal;
        }

        public String getPost_title() {
            return post_title;
        }

        public HashMap<String, String> getVariation() {
            return variation;
        }

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

        public void setPreviousQuantity(int previousQuantity) {
            this.previousQuantity = previousQuantity;
        }



        public int getPreviousQuantity() {
            return previousQuantity;
        }

        public String getTm_epo_product_original_price() {
            return tm_epo_product_original_price;
        }

        public String getTm_epo_options_prices() {
            return tm_epo_options_prices;
        }

        public String seletedOptionPrice() {
            return tm_epo_product_price_with_options;
        }

        public String getTm_cart_item_key() {
            return tm_cart_item_key;
        }

        public int getLineTotal() {
            return line_total;
        }

        public void setLineTotal(int line_total) {
            this.line_total = line_total;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setLine_subtotal(int line_subtotal) {
            this.line_subtotal = line_subtotal;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(product_id);
            dest.writeInt(variation_id);
            dest.writeInt(quantity);
            dest.writeString(tm_epo_product_original_price);
            dest.writeString(tm_epo_options_prices);
            dest.writeString(tm_epo_product_price_with_options);
            dest.writeString(tm_cart_item_key);
            dest.writeInt(line_total);
            dest.writeInt(line_subtotal);
            dest.writeInt(line_tax);
            dest.writeInt(line_subtotal_tax);
            dest.writeString(post_name);
            dest.writeString(post_image_url);
            dest.writeInt(previousQuantity);
            dest.writeString(post_title);
            dest.writeInt(previousTotal);
        }
    }
}
