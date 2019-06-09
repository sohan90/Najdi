package com.najdi.android.najdiapp.checkout.model;

public class OrderResponse {
    int id;
    int parent_id;
    String number;
    String order_key;
    String status;
    String currency;
    String date_created;
    String total;
    String payment_method;
    String payment_method_title;


    public int getId() {
        return id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public String getNumber() {
        return number;
    }

    public String getOrder_key() {
        return order_key;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getTotal() {
        return total;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getPayment_method_title() {
        return payment_method_title;
    }
}
