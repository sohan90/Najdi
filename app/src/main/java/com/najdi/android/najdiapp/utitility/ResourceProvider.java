package com.najdi.android.najdiapp.utitility;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Locale;

import androidx.core.content.ContextCompat;

public class ResourceProvider {
    private Context mContext;

    public ResourceProvider(Context mContext) {
        this.mContext = mContext;
    }

    public Context getAppContext(){
        return mContext;
    }

    public String getString(int resId) {
        return mContext.getString(resId);
    }

    public int getColor(int color){
        return ContextCompat.getColor(mContext, color);
    }

    public Drawable getDrawable(int drawable){
        return ContextCompat.getDrawable(mContext, drawable);
    }

    public String getString(int resId, String value) {
        return mContext.getString(resId, value);
    }

    public String getCountryLang(){
        Locale locale = mContext.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

}
