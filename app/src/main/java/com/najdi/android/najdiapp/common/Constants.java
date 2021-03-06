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
    public static final String LAUNCH_CART = "launch_cart ";
    public static final String LAUNCH_PRODUCT = "launch_product ";
    public static final String LAUNC_BANK_ACCOUNT = "launch_bank_account";
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final int OTP_TIME = 60;
    public static final int PASSWORD_LENGTH = 6;
    public static final int MOBILE_NO_LENGTH = 9;

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
        String ABOUT_US = "about_us";
        String CONTACT_US = "contact_us";
        String FORGOT_PASSWORD = "forgot_password";
        String PROFILE = "profile_tag";

    }

    @IntDef(value = {ScreeNames.PRODUCTS, ScreeNames.SHOPPING_CART,
            ScreeNames.PRODUCT_DETAIL, ScreeNames.ORDER_STATUS})
    public @interface ScreeNames {
        int PRODUCTS = 0;
        int SHOPPING_CART = 1;
        int PRODUCT_DETAIL = 2;
        int ORDER_STATUS = 3;
        int BANK_ACCOUNTS = 4;
        int ABOUT_US = 5;
        int CONTACT_US = 6;
        int PROFILE = 7;
    }

    @IntDef(value = {HtmlScreen.ABOUT_US, HtmlScreen.TERMS_CONDITION, HtmlScreen.PRIVACY_POLICY})
    public @interface HtmlScreen {
        int ABOUT_US = 0;
        int TERMS_CONDITION = 1;
        int PRIVACY_POLICY = 2;
    }

    @IntDef(value = {OtpScreen.SIGN_UP_SCREEN, OtpScreen.FORGOT_PASSWORD_SCREEN,
            OtpScreen.RESET_PASSWORD_OTP_SCREEN, OtpScreen.CHANGE_PASSWORD_PROFILE, OtpScreen.OLD_USER_FLOW})
    public @interface OtpScreen {
        int SIGN_UP_SCREEN = 0;
        int FORGOT_PASSWORD_SCREEN = 1;
        int RESET_PASSWORD_OTP_SCREEN = 2;
        int CHANGE_PASSWORD_PROFILE = 3;
        int CHANGE_MOBILE_VERIFY = 4;
        int OLD_USER_FLOW = 5;
    }
}
