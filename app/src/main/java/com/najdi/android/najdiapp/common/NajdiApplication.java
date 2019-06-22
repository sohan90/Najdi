package com.najdi.android.najdiapp.common;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;

import com.najdi.android.najdiapp.utitility.MathUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.Locale;

public class NajdiApplication extends Application {
    private ResourceProvider mResourceProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        setLocaleLanguage("ar");
    }

    public ResourceProvider getResourceProvider() {
        if (mResourceProvider == null)
            mResourceProvider = new ResourceProvider(this);

        return mResourceProvider;
    }

    public static NajdiApplication get(Activity activity) {
        return (NajdiApplication) activity.getApplication();
    }

    protected void setLocaleLanguage(String localeLanguage) {
        Locale locale = new Locale(localeLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().
                getResources().getDisplayMetrics());
        MathUtils.setCurrencySymbol(locale);
    }
}
