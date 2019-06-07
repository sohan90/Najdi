package com.najdi.android.najdiapp.shippingdetails.view;

import android.os.Bundle;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.databinding.ActivityCheckoutBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class ShippingDetailActivity extends BaseActivity {

    ActivityCheckoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout,
                null);
    }
}
