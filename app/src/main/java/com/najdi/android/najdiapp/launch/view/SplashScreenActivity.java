package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.common.GenericClickListener;
import com.najdi.android.najdiapp.common.NajdiApplication;
import com.najdi.android.najdiapp.databinding.ActivitySplashBinding;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.repository.Repository;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;
import static com.najdi.android.najdiapp.common.Constants.ENGLISH_LAN;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_ID_KEY;

public class SplashScreenActivity extends BaseActivity {
    private static final int DELAY = 2000;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        Repository repository = new Repository(NajdiApplication.get(this).getResourceProvider());
        fetchAppInfo(repository, aBoolean -> launchHomeScreenForLoggedUser());
    }

    private void launchHomeScreenForLoggedUser() {
        String loginToken = PreferenceUtils.getValueString(SplashScreenActivity.this,
                USER_ID_KEY);
        if (!TextUtils.isEmpty(loginToken)) {
            binding.spinnerLyt.setVisibility(View.GONE);
            new Handler().postDelayed(() -> {
                launchHomeScreen();
                finish();
            }, DELAY);

        } else {
            showLanguageSelection();
        }
    }

    private void showLanguageSelection() {
        String[] country = {"Select Language", "English", "عربى"};
        ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                R.layout.spinner_text, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(aa);
        binding.spinner.setSelection(0, false);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (position == 1) {
                        PreferenceUtils.setValueString(SplashScreenActivity.this,
                                PreferenceUtils.LOCALE_LANG, ENGLISH_LAN);
                        setLocaleLanguage(Constants.ENGLISH_LAN);
                    } else {
                        PreferenceUtils.setValueString(SplashScreenActivity.this,
                                PreferenceUtils.LOCALE_LANG, ARABIC_LAN);
                        setLocaleLanguage(Constants.ARABIC_LAN);
                    }
                }
                launchLoginScreen();
                finish();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

    private void launchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
