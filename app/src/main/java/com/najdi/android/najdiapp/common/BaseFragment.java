package com.najdi.android.najdiapp.common;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.launch.view.LoginActivity;
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

    protected void launchGuestUserDialog() {
        if (getActivity() == null) return;
        DialogUtil.showGuestAlertDialog(getActivity(), getString(R.string.guest_user_title),
                getString(R.string.login_to_continue),
                (dialog, which) -> {
                    dialog.dismiss();
                    if (which == Dialog.BUTTON_POSITIVE) {
                        launchLoginScreen();
                        getActivity().finish();
                    }
                }, getString(R.string.log_in), getString(R.string.cancel), false);
    }

    private void launchLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    protected String getRtlSupportedHtml(String html){
        String rtlHtml = html;
        if (resourceProvider.getCountryLang().equals(Constants.ARABIC_LAN)){
            rtlHtml =  "<html dir=\"rtl\" lang=\"\"><body>" + html + "</body></html>";
        }
      return  rtlHtml;
    }
}
