package com.najdi.android.najdiapp.utitility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;

public class ResourceProvider {
    private Context mContext;
    private Context activityContext;
    private Locale locale;

    public ResourceProvider(Context mContext) {
        this.mContext = mContext;
    }

    public Context getAppContext() {
        return mContext;
    }

    public String getString(int resId) {
        Context context;
        if (activityContext == null) {
            context = mContext;
        } else {
            context = activityContext;
        }
        return context.getString(resId);
    }

    public int getColor(int color) {
        return ContextCompat.getColor(mContext, color);
    }

    public Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(mContext, drawable);
    }

    public String getString(int resId, String value) {
        return mContext.getString(resId, value);
    }

    public String getCountryLang() {
        return this.getLocale().getLanguage();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setCurrentLocale(Locale locale) {
        this.locale = locale;

    }

    public void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }
}
