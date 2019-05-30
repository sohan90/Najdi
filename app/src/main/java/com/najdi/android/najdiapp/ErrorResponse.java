package com.najdi.android.najdiapp;

import android.content.Context;

import com.najdi.android.najdiapp.utitility.ToastUtils;

public class ErrorResponse {
    String code;
    String message;
    Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        int status;
        int getStatus() {
            return status;
        }
    }


    public void handleError(Context context) {
        switch (data.getStatus()) {
            case 400:
                ToastUtils.getInstance(context).showLongToast(code);
                break;
        }
    }

}
