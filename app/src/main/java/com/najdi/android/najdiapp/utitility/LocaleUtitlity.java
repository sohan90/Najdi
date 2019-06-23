package com.najdi.android.najdiapp.utitility;

public class LocaleUtitlity {
    private static String countryLang;

    public static void setCountryLang(String countryLang) {
        LocaleUtitlity.countryLang = countryLang;
    }

    public static String getCountryLang() {
        return countryLang;
    }
}
