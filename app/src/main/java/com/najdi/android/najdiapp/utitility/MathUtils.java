package com.najdi.android.najdiapp.utitility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MathUtils {

    private static String currencySymbol;
    public static final String ORDER_STATUS_DATE_FORMAT = "MMM dd";
    public static final String ORDER_COMPLETE_DATE_FORMAT = "MMM dd yy";

    public static String formateStringDate(String date, String dateFormatWanted) {
        String newDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
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
        currencySymbol = format.getCurrency().getSymbol();
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }
}
