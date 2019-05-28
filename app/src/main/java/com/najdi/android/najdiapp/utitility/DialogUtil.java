package com.najdi.android.najdiapp.utitility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.najdi.android.najdiapp.R;


/**
 * Created by sohan
 */
public class DialogUtil {

    private static AlertDialog sProgressAlertDialog;

    /**
     * Shows Progress Dialog with only Spinner in the center
     *
     * @param context context
     */
    public static void showProgressDialog(Context context, boolean allowCancelable) {
        if (context == null) return;

        if (!isProgressDialogVisible()) {
            AlertDialog.Builder progressBuilder = new AlertDialog.Builder(context, R.style.ProgressDialogTheme);

            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_bar, null);
            progressBuilder.setView(view);
            progressBuilder.setCancelable(allowCancelable);

            sProgressAlertDialog = progressBuilder.create();
            sProgressAlertDialog.setOnDismissListener((dialog) -> {
                if (dialog != null)
                    dialog.dismiss();
                isProgressDialogVisible();
            });
            sProgressAlertDialog.show();
        }
    }

    /**
     * Hides the Progress Dialog if its showing
     */
    public static void hideProgressDialog() {
        if (isProgressDialogVisible()) {
            sProgressAlertDialog.dismiss();
            sProgressAlertDialog = null;
        }
    }

    /**
     * Method to know whether Progress Dialog is visible or not
     *
     * @return Boolean for Progress Dialog visibility
     */
    public static boolean isProgressDialogVisible() {
        return (sProgressAlertDialog != null && sProgressAlertDialog.isShowing());
    }

    public static void showGuestAlertDialog(Context context, String tittle, String message, final DialogInterface.OnClickListener listener,
                                            String posButtonName, String negButtonName, boolean isCancelable) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            if (!TextUtils.isEmpty(posButtonName)) {
                builder.setTitle(tittle).setMessage(message).setPositiveButton(posButtonName, listener);
            }
            if (!TextUtils.isEmpty(negButtonName)) {
                builder.setTitle(tittle).setMessage(message).setNegativeButton(negButtonName, listener);
            }
            AlertDialog dialog = builder.create();
            dialog.setCancelable(isCancelable);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, String message, final DialogInterface.OnClickListener listener) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setTitle(null).setMessage(message).setPositiveButton(R.string.ok, listener);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
