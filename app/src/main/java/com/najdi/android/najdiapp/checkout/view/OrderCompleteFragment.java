package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutStep3Binding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class OrderCompleteFragment extends BaseFragment {

    FragmentCheckoutStep3Binding binding;
    private CheckoutViewModel activityViewModel;

    public static OrderCompleteFragment createInstance(){
        return new OrderCompleteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_checkout_step_3, container, false);

        initializeActivityViewModel();
        subscribeForOrderResponse();
        return binding.getRoot();

    }

    private void subscribeForOrderResponse() {
        activityViewModel.orderResponseMutableLiveData().observe(this, orderResponse -> {
            binding.setViewModel(orderResponse);
        });
    }

    private void initializeActivityViewModel() {
        activityViewModel = ViewModelProviders.of(getActivity()).get(CheckoutViewModel.class);
    }
}
