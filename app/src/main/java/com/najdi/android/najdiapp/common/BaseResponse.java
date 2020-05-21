package com.najdi.android.najdiapp.common;

import android.content.Context;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.OrderStatus;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.List;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;

public class BaseResponse {
    String code;
    String message;
    int total_items;
    List<BankResponse> data;

    //new changes
    String temp_id;
    boolean status;
    String user_id;
    String _token;//user token
    String token;
    ProductListResponse product;
    List<OrderStatus> orders;

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

    public class Data {
        int error;
        int status;
        String token;
        String user_id;
        String user_email;
        String user_nicename;
        String user_status;
        String user_url;
        List<BankResponse> data;
        int count;
        String message;


        public int getCount() {
            return count;
        }

        public List<BankResponse> getData() {
            return data;
        }

        public int getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }

        public String getUserId() {
            return user_id;
        }

        public String getUserEmail() {
            return user_email;
        }

        public String getUserNicename() {
            return user_nicename;
        }

        public String getUserStatus() {
            return user_status;
        }

        public String getUserUrl() {
            return user_url;
        }

        public int getStatus() {
            return status;
        }
    }


    public class BankResponse {
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
        if (data != null) {
            switch (Integer.parseInt(code)) {
                case 400:
                case 500:
                case 404:
                    ToastUtils.getInstance(context).showLongToast(message);
                    break;


            }
        }
    }

    public void handleErrorForDialog(Context context) {
        switch (getCode()) {
            case 401:
                DialogUtil.showAlertDialog(context, context.getString(R.string.enter_correct_code),
                        (dialog, which) -> dialog.dismiss());
                break;

            case 403:
                if (message != null) {
                    String message = context.getString(R.string.incorrect_password);
                    if (LocaleUtitlity.getCountryLang().equals(ARABIC_LAN)) {
                        message = context.getString(R.string.incorrect_password_arabic);
                    }
                    DialogUtil.showAlertDialog(context, message,
                            (dialog, which) -> dialog.dismiss());
                }
        }
    }


}
