package com.najdi.android.najdiapp.shoppingcart.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartResponse {
    private int code;
    private String message;
    private boolean status;
    private List<CartData> cart;
    String total_cart_amount;
    String total_payable_amount;
    String total_attributes_amount;
    String show_tax;
    float tax_amount;
    String coupon_applied;
    String discount;
    String coupon_token;


    public String getCoupon_token() {
        return coupon_token;
    }

    public String getTotal_attributes_amount() {
        return total_attributes_amount;
    }

    public List<CartData> getCart() {
        return cart;
    }

    public String getCouponApplied() {
        return coupon_applied;
    }

    public String getDiscount() {
        return discount;
    }

    public boolean isStatus() {
        return status;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static class CartData implements Parcelable {
        private String product_id;
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

        //new changes
        String id;
        String product_name;
        String qty;
        String details;
        String notes;
        String price;
        String subtotal;
        String image;
        List<AttributeCartOptionData> attribute_data;

        public List<AttributeCartOptionData> getAttributeData() {
            return attribute_data;
        }

        protected CartData(Parcel in) {
            product_id = in.readString();
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
            //
            id = in.readString();
            product_name = in.readString();
            qty = in.readString();
            details = in.readString();
            notes = in.readString();
            price = in.readString();
            subtotal = in.readString();
            image = in.readString();
            attribute_data = new ArrayList<>();
            in.readTypedList(attribute_data, AttributeCartOptionData.CREATOR);
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

        public void setVariation(HashMap<String, String> variation) {
            this.variation = variation;
        }

        public String getProductId() {
            return product_id;
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

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setLine_subtotal(int line_subtotal) {
            this.line_subtotal = line_subtotal;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(product_id);
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

            dest.writeString(id);
            dest.writeString(product_name);
            dest.writeString(qty);
            dest.writeString(details);
            dest.writeString(notes);
            dest.writeString(price);
            dest.writeString(subtotal);
            dest.writeString(image);
            dest.writeTypedList(attribute_data);
        }

        public String getId() {
            return id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getDetails() {
            return details;
        }

        public String getNotes() {
            return notes;
        }

        public String getPrice() {
            return price;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public float priceWithVariation(float totalAmount) {
            float price = totalAmount / quantity;
            return price;
        }

        public String getSubtotalWithQtyPrc() {
            float subTotal = Float.parseFloat(subtotal);
            return String.valueOf(subTotal);
        }

        public String getImage() {
            return image;
        }

    }

    public String getSubTotal() {
        return total_cart_amount;
    }

    public String getTotalAmnt(){
        return total_payable_amount;
    }

    public String getShowTax() {
        return show_tax;
    }

    public float getTaxAmount() {
        return tax_amount;
    }

}
