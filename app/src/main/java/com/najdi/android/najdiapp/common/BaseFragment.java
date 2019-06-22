package com.najdi.android.najdiapp.common;

import android.content.res.Configuration;

import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.MathUtils;

import java.util.Locale;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected void showProgressDialog(){
        DialogUtil.showProgressDialog(getActivity(), false);
    }

    protected void hideProgressDialog(){
        DialogUtil.hideProgressDialog();
    }

    protected void setLocaleLanguage(String localeLanguage) {
        Locale locale = new Locale(localeLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        MathUtils.setCurrencySymbol(locale);
    }

    protected String getCurrentLocale() {
        Locale locale = getResources().getConfiguration().locale;
        return locale.getLanguage();
    }
}
