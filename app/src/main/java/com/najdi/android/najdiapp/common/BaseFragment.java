package com.najdi.android.najdiapp.common;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.MathUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected ResourceProvider resourceProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceProvider = NajdiApplication.get(getActivity()).getResourceProvider();
        resourceProvider.setCurrentLocale(getCurrentLocale());
        LocaleUtitlity.setCurrentLocale(getCurrentLocale());

    }

    protected void showProgressDialog() {
        DialogUtil.showProgressDialog(getActivity(), false);
    }

    protected void hideProgressDialog() {
        DialogUtil.hideProgressDialog();
    }

    protected void setLocaleLanguage(String localeLanguage) {
        Locale locale = new Locale(localeLanguage);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        if (getContext() == null) return;
        getContext().createConfigurationContext(config);
        MathUtils.setCurrencySymbol(locale);
        resourceProvider.setCurrentLocale(locale);
        LocaleUtitlity.setCountryLang(localeLanguage);
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
}
