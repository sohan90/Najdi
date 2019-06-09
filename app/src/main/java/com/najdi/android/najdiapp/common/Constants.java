package com.najdi.android.najdiapp.common;

import androidx.annotation.IntDef;

public class Constants {

    public final static String BASIC_64_AUTH = "Basic Y2tfMzFmNTIxOTA3ZDJlZTY0ZmE4ZDMzMjAwNjE0ODY4ZTc3ZDlhOGY3Njpjc184NDU5ZjU4NWIwYzM3MjQ0Y2JjZWViZDYzZTE0MTQ4OWRjZDlkMWU4";
    public final static String APPEND_ATTRIBUTE_STR = "attribute_";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    public interface FragmentTags {
        String SIGN_UP_FRAG = "sign_up";
        String PRODUCT_LIST_FRAG = "product_list_frag";
        String SHIPPING_DETAIL = "Step_one_frag";
        String CHECKOUT = "Step_two_frag";
        String ORDER_COMPLETE = "Step_three_frag";

    }

    @IntDef(value = {ScreeNames.PRODUCTS, ScreeNames.SHOPPING_CART,
            ScreeNames.PRODUCT_DETAIL})
    public @interface ScreeNames {
        int PRODUCTS = 0;
        int SHOPPING_CART = 1;
        int PRODUCT_DETAIL = 2;
    }

}
