package com.najdi.android.najdiapp.common;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.MathUtils;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.Locale;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class BaseActivity extends AppCompatActivity {

    protected ResourceProvider resourProvider;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourProvider = NajdiApplication.get(this).getResourceProvider();
        String lang = PreferenceUtils.getLangFromPref(this);
        updateLocale(lang);
    }

    private void updateLocale(String lang) {
        setLocaleLanguage(lang);
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

    protected void addDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    protected void dispose() {
        getCompositeDisposable().dispose();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
