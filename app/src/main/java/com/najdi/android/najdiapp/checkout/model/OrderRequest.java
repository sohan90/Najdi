package com.najdi.android.najdiapp.checkout.model;

import com.najdi.android.najdiapp.launch.model.BillingAddress;

public class OrderRequest {
    String payment_method;
    String payment_method_title;
    String customer_id;
    BillingAddress billing;
    BillingAddress shipping;
    boolean set_paid;


    public void setSet_paid(boolean set_paid) {
        this.set_paid = set_paid;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setPayment_method_title(String payment_method_title) {
        this.payment_method_title = payment_method_title;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setBilling(BillingAddress billing) {
        this.billing = billing;
    }

    public void setShipping(BillingAddress shipping) {
        this.shipping = shipping;
    }

}
