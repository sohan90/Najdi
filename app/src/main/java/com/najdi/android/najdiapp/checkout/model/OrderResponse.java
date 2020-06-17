package com.najdi.android.najdiapp.checkout.model;

import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.utitility.MathUtils;

import static com.najdi.android.najdiapp.utitility.MathUtils.ORDER_STATUS_DATE_FORMAT;

public class OrderResponse {
    private String order_id;
    private int parent_id;
    private String number;
    private String order_key;
    private boolean status;
    private String currency;
    private String order_date;
    private String total_amount;
    private String payment_method;
    private String message;
    private String order_status_label;
    private String order_status;

    public String getOrderStatus() {
        return order_status;
    }

    public String getOrderStatusLabel() {
        return order_status_label;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return "#" + order_id;
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

    public boolean isStatus() {
        return status;
    }

    @BindingAdapter("textColor")
    public static void textColor(TextView textView, String status) {
        int color;
        if (status.equalsIgnoreCase("pending")) {
            color = ContextCompat.getColor(textView.getContext(), R.color.grey_text);
        } else {
            color = ContextCompat.getColor(textView.getContext(), R.color.green_default);
        }
        textView.setTextColor(color);
    }

    public String getCurrency() {
        return currency;
    }

    public String getDate_created() {
        return order_date;
    }

    public String getDateForOrderCompleted(){
        return order_date;
    }

    public String getTotal() {
        return total_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getPayment_method_title() {
        return payment_method;
    }

    public String getFormatedData() {
        return MathUtils.formateStringDate(order_date, ORDER_STATUS_DATE_FORMAT);
    }
}
