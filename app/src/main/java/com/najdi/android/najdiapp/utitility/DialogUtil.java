package com.najdi.android.najdiapp.utitility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.GenericClickListener;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by sohan
 */
public class DialogUtil {

    private static AlertDialog sProgressAlertDialog;
    private static int vectorDrawable = R.drawable.animated_check;

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

    public static void showAlertDialogNegativeVector(Context context,
                                                        String message,
                                                        final DialogInterface.OnClickListener listener){

        vectorDrawable = R.drawable.animated_cross;
        showAlertDialog(context, message, listener);
        vectorDrawable = R.drawable.animated_check;//resting the animation
    }

    public static void showAlertDialog(Context context, String message,
                                       final DialogInterface.OnClickListener listener) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            // set the custom layout
            final View customLayout = inflater.inflate(R.layout.custom_dialog_lyt, null);
            builder.setView(customLayout);

            TextView messageView = customLayout.findViewById(R.id.message);
            messageView.setText(message);

            ImageView tickView = customLayout.findViewById(R.id.imageView3);
            final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) ContextCompat.getDrawable(context,
                    vectorDrawable);
            tickView.setImageDrawable(animatedVectorDrawable);
            animatedVectorDrawable.start();
            animatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    animatedVectorDrawable.start();
                }
            });


            builder.setPositiveButton(R.string.ok, listener);
            AlertDialog dialog = builder.create();
            dialog.show();

           /* CustomDialogLytBinding binding = DataBindingUtil.bind(customLayout);
            binding.ok.setOnClickListener((v ->
                    listener.onClick(dialog, AlertDialog.BUTTON_POSITIVE)));*/
        }
    }

    public static void showAlertWithNegativeButton(Context context, String title, String message,
                                                   final DialogInterface.OnClickListener listener) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setTitle(title).setMessage(message).setNegativeButton(R.string.cancel, listener).
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

    public static void showPopupWindow(Context context, View anchorView, String title, List<String> list,
                                       GenericClickListener<Integer> clickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.item_pop_window, null);
        customView.findViewById(R.id.include_lyt).setVisibility(View.VISIBLE);
        TextView titleView = customView.findViewById(R.id.title);
        titleView.setText(title);
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
        popupWindow.setHeight((int) context.getResources().getDimension(R.dimen.height));
        popupWindow.setWidth((int) context.getResources().getDimension(R.dimen.width));
        popupWindow.setTouchInterceptor((v, motionEvent) -> {
            v.performClick();
            if (motionEvent.getX() < 0 || motionEvent.getX() > popupWindow.getWidth()) return true;
            return motionEvent.getY() < 0 || motionEvent.getY() > popupWindow.getHeight();
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
        listPopupWindow.setWidth((int) context.getResources().getDimension(R.dimen.category_width));
        listPopupWindow.setHorizontalOffset(-(listPopupWindow.getWidth() / 2 + anchorView.getWidth()));
        listPopupWindow.setModal(true);
        listPopupWindow.setOnDismissListener(dismissListener);
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            listPopupWindow.dismiss();
            clickListener.onClicked(position);
        });
        listPopupWindow.show();
    }
}
