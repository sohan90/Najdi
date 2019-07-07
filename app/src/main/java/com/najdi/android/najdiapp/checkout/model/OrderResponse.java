package com.najdi.android.najdiapp.checkout.model;

import android.widget.TextView;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.MathUtils;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import static com.najdi.android.najdiapp.utitility.MathUtils.ORDER_COMPLETE_DATE_FORMAT;
import static com.najdi.android.najdiapp.utitility.MathUtils.ORDER_STATUS_DATE_FORMAT;

public class OrderResponse {
    String id;
    int parent_id;
    String number;
    String order_key;
    String status;
    String currency;
    String date_created;
    String total;
    String payment_method;
    String payment_method_title;


    public String getId() {
        return "#" +String.valueOf(id);
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

    public void setStatus(String status) {
        this.status = status;
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
        return date_created;
    }

    public String getDateForOrderCompleted(){
        return MathUtils.formateStringDate(date_created, ORDER_COMPLETE_DATE_FORMAT);
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

    public String getFormatedData() {
        return MathUtils.formateStringDate(date_created, ORDER_STATUS_DATE_FORMAT);
    }
}
