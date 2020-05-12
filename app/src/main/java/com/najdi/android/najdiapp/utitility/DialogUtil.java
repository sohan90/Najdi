package com.najdi.android.najdiapp.utitility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.GenericClickListener;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


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
    private static boolean isProgressDialogVisible() {
        return (sProgressAlertDialog != null && sProgressAlertDialog.isShowing());
    }

    public static void showGuestAlertDialog(Context context, String tittle, String message,
                                            final DialogInterface.OnClickListener listener,
                                            String posButtonName, String negButtonName,
                                            boolean isCancelable) {
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

    public static void showAlertDialog(Context context, String message,
                                       final DialogInterface.OnClickListener listener) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setTitle(null).setMessage(message).setPositiveButton(R.string.ok, listener);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static void showAlertWithNegativeButton(Context context, String message,
                                                   final DialogInterface.OnClickListener listener) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setTitle(null).setMessage(message).setNegativeButton(R.string.cancel, listener).
                    setPositiveButton(R.string.ok, listener);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static void showPopupWindowSpinner(Context context, View anchorView, List<String> list,
                                      GenericClickListener<Integer> clickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.item_pop_window, null);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, list);
        ListView listView = customView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        PopupWindow popupWindow = new PopupWindow(customView, anchorView.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(20);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(anchorView, 0, 0);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            clickListener.onClicked(position);
            popupWindow.dismiss();
        });

    }
    public static void showPopupWindow(Context context, View anchorView, List<String> list,
                                       GenericClickListener<Integer> clickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.item_pop_window, null);
        customView.findViewById(R.id.include_lyt).setVisibility(View.VISIBLE);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, list);
        ListView listView = customView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        PopupWindow popupWindow = new PopupWindow(customView, anchorView.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setElevation(20);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setHeight(1000);
        popupWindow.setWidth(700);
        popupWindow.setTouchInterceptor((v, motionEvent) -> {
            v.performClick();
            if (motionEvent.getX() < 0 || motionEvent.getX() > popupWindow.getWidth()) return true;
            if (motionEvent.getY() < 0 || motionEvent.getY() > popupWindow.getHeight()) return true;

            return false;
        });
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            clickListener.onClicked(position);
            popupWindow.dismiss();
        });

    }

    public static void showListPopupWindow(Context context, View anchorView, List<String> list,
                                           GenericClickListener<Integer> clickListener,
                                           PopupWindow.OnDismissListener dismissListener) {
        if (list == null) return;
        ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.setWidth(400);
        listPopupWindow.setHorizontalOffset(-(listPopupWindow.getWidth()/ 2 + anchorView.getWidth()));
        listPopupWindow.setModal(true);
        listPopupWindow.setOnDismissListener(dismissListener);
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            listPopupWindow.dismiss();
            clickListener.onClicked(position);
        });
        listPopupWindow.show();
    }
}
