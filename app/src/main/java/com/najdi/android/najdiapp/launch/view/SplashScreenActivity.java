package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.appcompat.app.AppCompatActivity;

import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_ID_KEY;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_LOGIIN_TOKEN;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_PHONE_NO_KEY;

public class SplashScreenActivity extends BaseActivity {
    private static final int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocaleLanguage(Constants.ENGLISH_LAN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            String loginToken = PreferenceUtils.getValueString(SplashScreenActivity.this, USER_LOGIIN_TOKEN);
            if (!TextUtils.isEmpty(loginToken)) {
                launchHomeScreen();
            } else {
                launchLoginScreen();
            }
            finish();
        }, DELAY);
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
