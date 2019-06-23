package com.najdi.android.najdiapp.checkout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutFragmentViewModel;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.common.ObservableManager;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutBinding;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.shoppingcart.view.CartAdapter;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutFragment extends BaseFragment {
    FragmentCheckoutBinding binding;
    private CheckoutViewModel activityViewModel;
    private CheckoutAdapter checkoutAdapter;
    private CheckoutFragmentViewModel viewModel;
    private List<CartResponse.CartData> adapterList;

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
        checkoutAdapter = new CheckoutAdapter(new CartAdapter.AdapterClickLisntener() {
            @Override
            public void onRemoveItem(String cartItemKey) {
                removeItem(cartItemKey);
            }

            @Override
            public void onEdit(CartResponse.CartData cartData) {
                if (cartData != null) {
                    ProductDetailBundleModel model = new ProductDetailBundleModel();
                    model.setProductId(cartData.getProductId());
                    model.setT(cartData);
                    model.setFromCartScreen(true);
                    notifyObserver(model);
                }
            }

            @Override
            public void onUpdateQuantity(int adapterPosition, String cartItemKey, int quantity) {
                updateItemQuantity(adapterPosition, cartItemKey, quantity);
            }


        }, new ArrayList<>());

        binding.recyclView.setAdapter(checkoutAdapter);
    }

    private void removeItem(String s) {
        showProgressDialog();
        LiveData<BaseResponse> baseResponseLiveData = viewModel.removeCart(s);
        baseResponseLiveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                String message = baseResponse.getData().getMessage();
                ToastUtils.getInstance(getActivity()).showShortToast(message);
            }
        });
    }

    private void updateItemQuantity(int adapterPosition, String cartItemKey, int quantity) {
        showProgressDialog();

        UpdateCartRequest updateCartRequest = new UpdateCartRequest();
        updateCartRequest.setCartItemKey(cartItemKey);
        updateCartRequest.setQuantity(quantity);

        LiveData<BaseResponse> liveData = viewModel.updateQuantity(updateCartRequest);
        liveData.observe(this, baseResponse -> {

            hideProgressDialog();
            if (baseResponse != null && baseResponse.getData() != null) {

                ToastUtils.getInstance(getActivity()).
                        showShortToast(baseResponse.getData().getMessage());
            } else {
                // failure case
                CartResponse.CartData cartData = adapterList.get(adapterPosition);
                cartData.setQuantity(cartData.getPreviousQuantity());
                checkoutAdapter.setDataList(adapterList);
            }
        });
    }

    private void notifyObserver(ProductDetailBundleModel model) {
        if (getActivity() != null) {
            getActivity().finish();
            Intent intent = new Intent();
            intent.putExtra(Constants.OBSERVER_INTENT_CART_RESPONSE, model);
            ObservableManager.getInstance().notifyData(intent);
        }
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
            adapterList = cartResponse.getData().getCartdata();
            checkoutAdapter.setDataList(adapterList);
        }
    }
}
