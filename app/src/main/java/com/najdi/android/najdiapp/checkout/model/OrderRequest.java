package com.najdi.android.najdiapp.checkout.model;

import com.najdi.android.najdiapp.launch.model.BillingAddress;

import java.util.List;

public class OrderRequest {
    String payment_method;
    String payment_method_title;
    int customer_id;
    BillingAddress billing;
    List<LineItemModelRequest>  line_items;

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setPayment_method_title(String payment_method_title) {
        this.payment_method_title = payment_method_title;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setBilling(BillingAddress billing) {
        this.billing = billing;
    }

    public void setLine_items(List<LineItemModelRequest> line_items) {
        this.line_items = line_items;
    }
}
