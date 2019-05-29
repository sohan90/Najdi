package com.najdi.android.najdiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private FrameLayout containerLyt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        containerLyt = findViewById(R.id.container);
    }

    protected void updateView(int view){
       View view1=  LayoutInflater.from(this).inflate(view, containerLyt, false);
        containerLyt.addView(view1);
    }
}
