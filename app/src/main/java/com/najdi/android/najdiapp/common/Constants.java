package com.najdi.android.najdiapp.common;

import androidx.annotation.IntDef;

public class Constants {

    public final static String APPEND_ATTRIBUTE_STR = "attribute_";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String OBSERVER_INTENT_CART_RESPONSE = "observe_cart_response";
    public static final String ARABIC_LAN = "ar";
    public static final String ENGLISH_LAN = "en";
    public static final String BEARER = "Bearer ";

    public interface FragmentTags {
        String PRODUCT_DETAIL = "prodcut_detail";
        String SHOPPING_CART = "shopping_cart";
        String ORDER_STATUS = "order_status";
        String SIGN_UP_FRAG = "sign_up";
        String PRODUCT_LIST_FRAG = "product_list_frag";
        String SHIPPING_DETAIL = "Step_one_frag";
        String CHECKOUT = "Step_two_frag";
        String ORDER_COMPLETE = "Step_three_frag";
        String BANK_ACCOUNT = "bank_account";

    }

    @IntDef(value = {ScreeNames.PRODUCTS, ScreeNames.SHOPPING_CART,
            ScreeNames.PRODUCT_DETAIL, ScreeNames.ORDER_STATUS})
    public @interface ScreeNames {
        int PRODUCTS = 0;
        int SHOPPING_CART = 1;
        int PRODUCT_DETAIL = 2;
        int ORDER_STATUS = 3;
        int BANK_ACCOUNTS =4;
    }

}
