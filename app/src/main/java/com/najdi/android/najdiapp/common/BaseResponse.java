package com.najdi.android.najdiapp.common;

import android.content.Context;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.OrderStatus;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.model.User;
import com.najdi.android.najdiapp.launch.model.AppDetailResponse;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.List;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;

public class BaseResponse {
    public static final int OLD_USER = 0;
    public static final int NEW_USER = 1;

    private String code;
    private String message;
    private int total_items;
    private List<BankResponse> data;

    //new changes
    private String temp_id;
    private boolean status;
    private String user_id;
    private String _token;//user token
    private String token;
    private ProductListResponse product;
    private List<OrderStatus> orders;
    private User user;
    private AppDetailResponse details;
    private int migrate_status;


    public void setCode(String code) {
        this.code = code;
    }


    public int getMigrateStatus() {
        return migrate_status;
    }

    public AppDetailResponse getDetails() {
        return details;
    }

    public User getUser() {
        return user;
    }

    public List<OrderStatus> getOrders() {
        return orders;
    }

    public int getTotalItems() {
        return total_items;
    }

    public ProductListResponse getProduct() {
        return product;
    }

    public String getUserToken() {
        return _token;
    }

    public String getToken() {
        return token;
    }

    public String getTempId() {
        return temp_id;
    }

    public boolean isStatus() {
        return status;
    }

    public String getUserid() {
        return user_id;
    }

    public int getCode() {
        return code == null ? 0 : Integer.parseInt(code);
    }

    public String getMessage() {
        return message;
    }

    public List<BankResponse> getData() {
        return data;
    }

    public static class BankResponse {
        String account_holder_name;
        String account_number;
        String bank_name;
        String sort_code;
        String bank_image;
        String iban_number;
        String bic;


        public String getAccount_name() {
            return account_holder_name;
        }

        public String getAccount_number() {
            return account_number;
        }

        public String getBank_name() {
            return bank_name;
        }

        public String getSort_code() {
            return sort_code;
        }

        public String getBank_logo() {
            return bank_image;
        }

        public String getIban() {
            return iban_number;
        }

        public String getBic() {
            return bic;
        }
    }

    public void handleError(Context context) {
        switch (Integer.parseInt(code)) {
            case 500:
                    if (message == null) return;
                    DialogUtil.showAlertDialog(context, message,
                            (dialog, which) -> dialog.dismiss());
                    break;

            case 400:
                handleErrorForDialog(context);
                break;
            case 404:
                ToastUtils.getInstance(context).showLongToast(message);
                break;


        }
    }

    public void handleErrorForDialog(Context context) {
        switch (getCode()) {
            case 401:
                DialogUtil.showAlertDialogNegativeVector(context, context.getString(R.string.enter_correct_code),
                        (dialog, which) -> dialog.dismiss());
                break;

            case 403:
                if (message != null) {
                    String message = context.getString(R.string.incorrect_password);
                    if (LocaleUtitlity.getCountryLang().equals(ARABIC_LAN)) {
                        message = context.getString(R.string.incorrect_password_arabic);
                    }
                    DialogUtil.showAlertDialogNegativeVector(context, message,
                            (dialog, which) -> dialog.dismiss());
                }
                break;


            case 500:
                ToastUtils.getInstance(context).showLongToast(context.getString(R.string.no_network_msg));
        }
    }


}
