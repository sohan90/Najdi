package com.najdi.android.najdiapp.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(JUnit4.class)
public class ExampleTest {

    @Test
    public void toDate() throws ParseException {
        String date = "2019-06-09T18:49:28";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateParse = simpleDateFormat.parse(date);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM YY");
        String newDate = simpleDateFormat1.format(dateParse);
    }
}
