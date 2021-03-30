package com.najdi.android.najdiapp.checkout.model;

import com.najdi.android.najdiapp.utitility.MathUtils;

import java.util.List;

import static com.najdi.android.najdiapp.utitility.MathUtils.ORDER_STATUS_DATE_FORMAT;

public class OrderStatus {
    private String order_id;
    private String full_name;
    private String user_id;
    private String payment_method;
    private String total_items;
    private String total_price;
    private String order_status;
    private String order_status_label;
    private String order_date;
    private List<Detail> details;

    public String getOrder_id() {
        return order_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getTotal_items() {
        return total_items;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getOrderStatusLabel() {
        return order_status_label;
    }

    public String getOrder_date() {
        return order_date;
    }

    public List<Detail> getDetails() {
        return details;
    }

   public class Detail {
        String id;
        String order_id;
        String product_id;
        String product_name;
        String qty;
        String price;
        String sub_total;
        String created_at;


       public String getCreated_at() {
           return MathUtils.formateStringDate(created_at, ORDER_STATUS_DATE_FORMAT);
       }

       public String getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public String getQty() {
            return qty;
        }

        public String getPrice() {
            return price;
        }

        public String getSub_total() {
            return sub_total;
        }

        // local need
        public String orderId;
        public String orderStatusLabel;
        public String orderStatus;
        public String paymentMethod;
        public String totalPrice;


        public String getOrderId() {
            return orderId;
        }

        public String getOrderStatusLabel() {
            return orderStatusLabel;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getTotalPrice() {
            return totalPrice;
        }
    }
}
