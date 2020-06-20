package com.najdi.android.najdiapp.common;

import android.app.Activity;
import android.app.Application;

import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

public class NajdiApplication extends Application {
    private ResourceProvider mResourceProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        LocaleUtitlity.setLocaleLanguage(this, Constants.ARABIC_LAN);
    }

    public ResourceProvider getResourceProvider() {
        if (mResourceProvider == null)
            mResourceProvider = new ResourceProvider(this);

        return mResourceProvider;
    }

    public static NajdiApplication get(Activity activity) {
        return (NajdiApplication) activity.getApplication();
    }
}
