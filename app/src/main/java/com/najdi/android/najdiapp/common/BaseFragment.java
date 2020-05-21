package com.najdi.android.najdiapp.common;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.MathUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.Locale;

public class BaseFragment extends Fragment {

    protected ResourceProvider resourceProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null) return;
        resourceProvider = NajdiApplication.get(getActivity()).getResourceProvider();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void showProgressDialog() {
        DialogUtil.showProgressDialog(getActivity(), false);
    }

    protected void hideProgressDialog() {
        DialogUtil.hideProgressDialog();
    }


    protected void setLocaleLanguage(String localeLanguage) {
        if (getActivity() == null) return;
        Locale locale = new Locale(localeLanguage);
        Locale.setDefault(locale);
        Configuration config = getActivity().getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        MathUtils.setCurrencySymbol(locale);
    }
}
