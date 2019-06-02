package com.najdi.android.najdiapp.common;

import android.os.Bundle;

import com.najdi.android.najdiapp.utitility.DialogUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showProgressDialog(){
        DialogUtil.showProgressDialog(this, false);
    }

    protected void hideProgressDialog(){
        DialogUtil.hideProgressDialog();
    }

}
