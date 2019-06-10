package com.najdi.android.najdiapp.utitility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MathUtils {

    public static String formateStringDate(String date) {
        String newDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault());
        try {
            Date dateParse = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM yy", Locale.getDefault());
            newDate = simpleDateFormat1.format(dateParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
}
