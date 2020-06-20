package com.najdi.android.najdiapp.utitility;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleUtitlity {
    private static String countryLang;
    private static Locale locale;

    public static Locale getLocale() {
        return locale;
    }

    public static void setCurrentLocale(Locale locale) {
        LocaleUtitlity.locale = locale;
        if (locale == null)return;
        setCountryLang(locale.getLanguage());
    }

    public static void setCountryLang(String countryLang) {
        LocaleUtitlity.countryLang = countryLang;
    }

    public static String getCountryLang() {
        return countryLang;
    }

    public static void setLocaleLanguage(Application context, String localeLanguage) {
        if (context == null) return;
        Locale locale = new Locale(localeLanguage);
        Locale.setDefault(locale);
        Configuration config = context.getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        MathUtils.setCurrencySymbol(locale);
    }
}
