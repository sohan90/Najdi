package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutStep1Binding;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ShippingDetailFragment extends BaseFragment {

    FragmentCheckoutStep1Binding binding;
    private CheckoutViewModel activityViewModel;

    public static ShippingDetailFragment createInstance() {
        return new ShippingDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_step_1, container,
                false);
        initalizeActivityViewModel();
        initClickListeners();
        updateDetails();
        subscribeForAddress();
        return binding.getRoot();
    }

    private void subscribeForAddress() {
        activityViewModel.getAddressMutableLiveData().observe(this, address -> {
            if (address != null) {
                String building = address.getFeatureName();
                String street = address.getSubLocality();
                String city = address.getLocality();
                String state = address.getAdminArea();
                String postalCode = address.getPostalCode();
                binding.street.setText(street);
                binding.buildingNo.setText(building);
                binding.city.setText(city);
                binding.province.setText(state);
                binding.postalCode.setText(postalCode);
            }
        });
    }

    private void initalizeActivityViewModel() {
        if (getActivity() == null) return;
        activityViewModel = ViewModelProviders.of(getActivity()).get(CheckoutViewModel.class);
    }

    private void initClickListeners() {
        binding.locationBtn.setOnClickListener(v -> {
            activityViewModel.getGetCurrentLocationUpdateLiveData().setValue(true);
        });
        binding.continueTxt.setOnClickListener(v -> {

        });
    }

    private void updateDetails() {
        if (getActivity() == null) return;
        String name = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY);
        String phoneNo = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_NAME_KEY);
        binding.name.setText(name);
        binding.phTxt.setText(phoneNo);
        binding.emTxt.setText(name);
    }
}
