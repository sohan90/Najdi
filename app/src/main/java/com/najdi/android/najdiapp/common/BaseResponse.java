package com.najdi.android.najdiapp.common;

import android.content.Context;

import com.najdi.android.najdiapp.utitility.ToastUtils;

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


    public void handleError(Context context) {
        if (data != null) {
            switch (data.getStatus()) {
                case 400:
                case 403:
                    ToastUtils.getInstance(context).showLongToast(message);
                    break;
            }
        }
    }

}
