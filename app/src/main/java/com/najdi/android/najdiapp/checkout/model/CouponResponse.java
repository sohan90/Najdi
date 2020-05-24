package com.najdi.android.najdiapp.checkout.model;

import com.najdi.android.najdiapp.common.BaseResponse;

public class CouponResponse extends BaseResponse {
    private String coupon_token;
    private String final_amount;
    private String discount_amount;
    private String coupon_code;

    public String getCouponToken() {
        return coupon_token;
    }

    public String getFinalAmount() {
        return final_amount;
    }

    public String getDiscountAmount() {
        return discount_amount;
    }

    public String getCouponCode() {
        return coupon_code;
    }
}
