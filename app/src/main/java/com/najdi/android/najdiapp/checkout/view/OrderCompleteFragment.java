package com.najdi.android.najdiapp.checkout.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.ObservableManager;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutStep3Binding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.common.Constants.LAUNCH_PRODUCT;
import static com.najdi.android.najdiapp.common.Constants.LAUNC_BANK_ACCOUNT;

public class OrderCompleteFragment extends BaseFragment {

    FragmentCheckoutStep3Binding binding;
    private CheckoutViewModel activityViewModel;

    public static OrderCompleteFragment createInstance() {
        return new OrderCompleteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_checkout_step_3, container, false);

        initializeActivityViewModel();
        initUi();
        initClickListener();
        subscribeForOrderResponse();
        return binding.getRoot();

    }

    private void initUi() {
        activityViewModel.getHideCart().setValue(true);
    }

    private void initClickListener() {
        binding.continueTxt.setOnClickListener(v -> {
            if (getActivity() == null) return;
            getActivity().finish();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent();
                intent.putExtra(LAUNCH_PRODUCT, true);
                ObservableManager.getInstance().notifyData(intent);
            }, 100);

        });

        binding.bottomTxt.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent();
                    intent.putExtra(LAUNC_BANK_ACCOUNT, true);
                    ObservableManager.getInstance().notifyData(intent);
                }, 100);
            }
        });
    }

    private void subscribeForOrderResponse() {
        activityViewModel.orderResponseMutableLiveData().observe(this, orderResponse -> {
            binding.setViewModel(orderResponse);
            String total = orderResponse.getTotal().concat(" "+getString(R.string.currency));
            binding.totalValue.setText(total);
        });
    }

    private void initializeActivityViewModel() {
        if(getActivity() == null) return;
        activityViewModel = ViewModelProviders.of(getActivity()).get(CheckoutViewModel.class);
    }
}
