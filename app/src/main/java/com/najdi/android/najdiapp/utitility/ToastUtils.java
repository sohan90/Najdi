package com.najdi.android.najdiapp.utitility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by M1034537 on 10/12/2017.
 */

public class ToastUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    @SuppressLint("StaticFieldLeak")
    private static ToastUtils toastUtils;

    private ToastUtils() {

    }

    public static ToastUtils getInstance(Context context) {
        if (toastUtils == null) {
            toastUtils = new ToastUtils();
            sContext = context;
        }
        return toastUtils;
    }

    public void showLongToast(String message) {
        Toast.makeText(sContext, message, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String message) {
        Toast toast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
        TextView messageV = toast.getView().findViewById(android.R.id.message);
        if (messageV != null) messageV.setGravity(Gravity.CENTER);
        toast.show();
    }
}
