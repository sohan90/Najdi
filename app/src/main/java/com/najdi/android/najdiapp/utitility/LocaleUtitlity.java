package com.najdi.android.najdiapp.utitility;

import java.util.Locale;

public class LocaleUtitlity {
    private static String countryLang;
    private static Locale locale;

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        LocaleUtitlity.locale = locale;
    }

    public static void setCountryLang(String countryLang) {
        LocaleUtitlity.countryLang = countryLang;
    }

    public static String getCountryLang() {
        return countryLang;
    }
}