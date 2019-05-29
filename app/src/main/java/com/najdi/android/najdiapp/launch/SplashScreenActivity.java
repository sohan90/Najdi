package com.najdi.android.najdiapp.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.najdi.android.najdiapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(this::launchLoginScreen, DELAY);
    }

    private void launchLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
