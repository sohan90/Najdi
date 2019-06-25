package com.najdi.android.najdiapp.common;

import android.content.Context;

import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.List;

public class BaseResponse {
    String code;
    String message;
    Data data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        int error;
        int status;
        String message;
        String token;
        String user_id;
        String user_email;
        String user_nicename;
        String user_status;
        String user_url;
        List<BankResponse> data;


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

        int getStatus() {
            return status;
        }
    }


    public class BankResponse {
        String account_name;
        String account_number;
        String bank_name;
        String sort_code;
        String bank_logo;
        String iban;
        String bic;


        public String getAccount_name() {
            return account_name;
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
            return bank_logo;
        }

        public String getIban() {
            return iban;
        }

        public String getBic() {
            return bic;
        }
    }

    public void handleError(Context context) {
        if (data != null) {
            switch (data.getStatus()) {
                case 400:
                case 401:
                case 403:
                case 500:
                    ToastUtils.getInstance(context).showLongToast(message);
                    break;
            }
        }
    }

}
