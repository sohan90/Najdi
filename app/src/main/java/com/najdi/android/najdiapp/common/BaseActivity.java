package com.najdi.android.najdiapp.common;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.launch.model.AppDetailResponse;
import com.najdi.android.najdiapp.repository.Repository;
import com.najdi.android.najdiapp.utitility.AppInfoUtil;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.MathUtils;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_SELECTED_CITY;

public class BaseActivity extends AppCompatActivity {

    protected ResourceProvider resourProvider;
    private CompositeDisposable compositeDisposable;
    private GenericClickListener<Boolean> listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourProvider = NajdiApplication.get(this).getResourceProvider();
        String lang = PreferenceUtils.getLangFromPref(this);
        updateLocale(lang);
    }

    protected void fetchAppInfo(Repository repository, GenericClickListener<Boolean> dismissListener){
        this.listener = dismissListener;
        fetchAppInfo(repository);
    }

    protected void fetchAppInfo(Repository repository) {
        repository.getAppInfo().observe(this, baseResponse -> {
            if (baseResponse != null){
                if (baseResponse.isStatus()){
                    AppDetailResponse appDetailResponse = baseResponse.getDetails();
                    if (!AppInfoUtil.getCurrentVersion(this).
                            equals(appDetailResponse.getVer_no_android())){

                        showUpgradeDialog();
                    }
                }
            }
        });
    }

    private void showUpgradeDialog() {
        DialogUtil.showAlertWithNegativeButton(this, "Upgrade your App", getString(R.string.app_name)
                .concat(" recommends that you upgrade to the latest version"), (d, w) -> {
            if (w == AlertDialog.BUTTON_POSITIVE){
                openPlayStore();
            } else {
                d.dismiss();
                if (listener != null){
                    listener.onClicked(true);
                }
            }
        });
    }

    private void openPlayStore() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                ("market://details?id="+AppInfoUtil.getPackageName(this))));
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

    protected Single<List<String>> getCityNameList(List<CityListModelResponse.City> cityList) {
        return io.reactivex.rxjava3.core.Observable.just(cityList)
                .flatMap(io.reactivex.rxjava3.core.Observable::fromIterable)
                .map(CityListModelResponse.City::getName)
                .toList();
    }

    protected void saveCityId(String name) {
        PreferenceUtils.setValueString(this, USER_SELECTED_CITY, name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
