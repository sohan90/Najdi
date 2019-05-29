package com.najdi.android.najdiapp;

import android.app.Activity;
import android.app.Application;

import com.najdi.android.najdiapp.utitility.ResourceProvider;

public class NajdiApplication extends Application {
    private ResourceProvider mResourceProvider;

    @Override
    public void onCreate() {
        super.onCreate();
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
