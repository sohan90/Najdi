package com.najdi.android.najdiapp.utitility;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MathUtils {

    private static String currencySymbol;
    public static final String ORDER_STATUS_DATE_FORMAT = "MMM/dd/yy";
    public static final String ORDER_COMPLETE_DATE_FORMAT = "MMM dd yy";

    public static String formateStringDate(String date, String dateFormatWanted) {
        String newDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        try {
            Date dateParse = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(dateFormatWanted,
                    LocaleUtitlity.getLocale());
            newDate = simpleDateFormat1.format(dateParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static void setCurrencySymbol(Locale locale) {
        java.text.NumberFormat format = java.text.NumberFormat.getInstance(locale);
        currencySymbol = format.getCurrency().getSymbol(locale);
    }

    public static String getCurrencySymbol() {
        return currencySymbol;
    }

    public static int convertDpToPx(Context context, int value) {
        Resources r = context.getResources();
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics()));
        return px;
    }
}
