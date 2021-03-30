package com.najdi.android.najdiapp.checkout.model;

public class CouponRequest {
    private String user_id;
    private String coupon_code;
    private String lang;
    private String coupon_token;


    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setCouponCode(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public void setCouponToken(String coupon_token) {
        this.coupon_token = coupon_token;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
