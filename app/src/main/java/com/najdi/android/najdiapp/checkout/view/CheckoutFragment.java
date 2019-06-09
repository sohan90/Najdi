package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutFragmentViewModel;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutBinding;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutFragment extends BaseFragment {
    FragmentCheckoutBinding binding;
    private CheckoutViewModel activityViewModel;
    private CheckoutAdapter checkoutAdapter;
    private CheckoutFragmentViewModel viewModel;

    public static CheckoutFragment createInstance() {
        return new CheckoutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout,
                container, false);

        initializeViewModel();
        bindViewModel();
        initializeActivityViewModel();
        initializeRecyclerViewAdapter();
        subscribeForCartResponse();
        initializeClickListener();
        return binding.getRoot();
    }

    private void initializeClickListener() {
        binding.includeLyt.placeOrder.setOnClickListener(v ->
                activityViewModel.getCheckoutLiveData().setValue(true));
    }

    private void bindViewModel() {
        binding.includeLyt.setViewModel(viewModel);
        binding.includeLyt.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(CheckoutFragmentViewModel.class);
    }

    private void initializeRecyclerViewAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,
                false);
        binding.recyclView.setLayoutManager(linearLayoutManager);
        checkoutAdapter = new CheckoutAdapter(new ArrayList<>());
        binding.recyclView.setAdapter(checkoutAdapter);
    }

    private void initializeActivityViewModel() {
        if (getActivity() == null) return;
        activityViewModel = ViewModelProviders.of(getActivity()).get(CheckoutViewModel.class);
    }

    private void subscribeForCartResponse() {
        activityViewModel.getCartResponseMutableLiveData().observe(getViewLifecycleOwner(),
                cartResponse -> {
                    if (cartResponse != null) {
                        updateAdapter(cartResponse);
                        viewModel.udpateTotal(cartResponse.getData().getCartdata());
                    }
                });
    }

    private void updateAdapter(CartResponse cartResponse) {
        if (cartResponse.getData() != null && cartResponse.getData().getCartdata() != null) {
            List<CartResponse.CartData> cartDataList = cartResponse.getData().getCartdata();
            checkoutAdapter.setDataList(cartDataList);
        }
    }
}
