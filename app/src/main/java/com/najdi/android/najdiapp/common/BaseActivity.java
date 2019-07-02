package com.najdi.android.najdiapp.common;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.MathUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected ResourceProvider resourProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourProvider = NajdiApplication.get(this).getResourceProvider();
    }

    private void updateLocale() {
        setLocaleLanguage(getCurrentLocale().getLanguage());
        resourProvider.setActivityContext(this);
        resourProvider.setCurrentLocale(getCurrentLocale());
        LocaleUtitlity.setCurrentLocale(getCurrentLocale());
    }

    protected void showProgressDialog() {
        DialogUtil.showProgressDialog(this, false);
    }

    protected void hideProgressDialog() {
        DialogUtil.hideProgressDialog();
    }

    protected void setLocaleLanguage(String localeLanguage) {
        Locale locale = new Locale(localeLanguage);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        MathUtils.setCurrencySymbol(locale);
    }

    protected String getCurrentLocaleLanguage() {
        Locale locale;
        locale = getCurrentLocale();
        return locale.getLanguage();
    }

    private Locale getCurrentLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            locale = getResources().getConfiguration().locale;
        }
        return locale;
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateLocale();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}
