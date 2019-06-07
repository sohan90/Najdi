package com.najdi.android.najdiapp.common;

import androidx.annotation.IntDef;

public class Constants {

    public final static String BASIC_64_AUTH = "Basic Y2tfMzFmNTIxOTA3ZDJlZTY0ZmE4ZDMzMjAwNjE0ODY4ZTc3ZDlhOGY3Njpjc184NDU5ZjU4NWIwYzM3MjQ0Y2JjZWViZDYzZTE0MTQ4OWRjZDlkMWU4";
    public final static String APPEND_ATTRIBUTE_STR = "attribute_";

    public interface FragmentTags {
        String SIGN_UP_FRAG = "sign_up";
        String PRODUCT_LIST_FRAG = "product_list_frag";

    }

    @IntDef(value = {ScreeNames.PRODUCTS, ScreeNames.SHOPPING_CART,
            ScreeNames.PRODUCT_DETAIL})
    public @interface ScreeNames {
        int PRODUCTS = 0;
        int SHOPPING_CART = 1;
        int PRODUCT_DETAIL = 2;
    }

}
