package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.checkout.viewmodel.ShippingDetailViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutStep1Binding;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ShippingDetailFragment extends BaseFragment {

    FragmentCheckoutStep1Binding binding;
    private CheckoutViewModel activityViewModel;
    private ShippingDetailViewModel viewModel;

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
        initializeViewModel();
        bindViewModel();
        initClickListeners();
        updateDetails();
        subscribeForAddress();
        return binding.getRoot();
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(ShippingDetailViewModel.class);
    }

    private void subscribeForAddress() {
        activityViewModel.getAddressMutableLiveData().observe(this, address -> {
            if (address != null) {
                viewModel.updateAddress(address);
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
            viewModel.validate().observe(getViewLifecycleOwner(), proceed -> {
                if (proceed) {
                    BillingAddress billingAddress = viewModel.getBillingObject();
                    activityViewModel.getBillingMutableLiveData().setValue(billingAddress);
                    activityViewModel.getProgressPercentage().setValue(50);
                } else {
                    ToastUtils.getInstance(getActivity()).showShortToast(getString(R.string.please_fill));
                }
            });
        });
    }

    private void updateDetails() {
        if (getActivity() == null) return;
        String name = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY);
        String phoneNo = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_NAME_KEY);
        viewModel.updatePersonalInfo(name, name, phoneNo);
    }
}
