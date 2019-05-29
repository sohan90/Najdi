package com.najdi.android.najdiapp.launch;

import android.content.Intent;
import android.os.Bundle;

import com.najdi.android.najdiapp.BaseActivity;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ActivityLoginBinding;
import com.najdi.android.najdiapp.launch.viewmodel.LoginViewModel;

import androidx.databinding.DataBindingUtil;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(new LoginViewModel());
        initClickListener();
    }

    private void initClickListener() {
        binding.signUp.setOnClickListener((v) -> launchSignupActivity());
    }

    private void launchSignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
