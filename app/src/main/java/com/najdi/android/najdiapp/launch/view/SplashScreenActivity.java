package com.najdi.android.najdiapp.launch.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.home.view.HomeScreenActivity;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.appcompat.app.AppCompatActivity;

import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_ID_KEY;

public class SplashScreenActivity extends BaseActivity {
    private static final int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            int userId = PreferenceUtils.getValueInt(SplashScreenActivity.this, USER_ID_KEY);
            if (userId != 0) {
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
