package com.najdi.android.najdiapp.shoppingcart.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.FragmentCartBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.viewmodel.CartViewModel;
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

public class CartFragment extends BaseFragment {

    FragmentCartBinding binding;
    private HomeScreenViewModel homeScreenViewModel;
    private CartAdapter adapter;
    private CartResponse cartResponse;
    private CartViewModel viewModel;

    public static CartFragment createInstance() {
        return new CartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        initializeHomeScreenViewModel();
        initializeViewModel();
        subscribeForCartResponse();
        setRecyclAdapter();
        subscribeForRemoveItem();
        bindLiveData();
        return binding.getRoot();
    }

    private void bindLiveData() {
        binding.setCartViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void subscribeForRemoveItem() {
        viewModel.getCartItemKeyLiveData().observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                removeItem(s);
            }
        });
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

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);
    }

    private void subscribeForCartResponse() {
        showProgressDialog();
        homeScreenViewModel.getCart().observe(this, cartResponse -> {
            hideProgressDialog();
            this.cartResponse = cartResponse;
            if (cartResponse != null && cartResponse.getData() != null) {
                viewModel.setTotal(cartResponse.getData().getCartdata());
                List<CartResponse.CartData> cartDataList = cartResponse.getData().getCartdata();
                adapter.setData(cartDataList);
            }
        });
    }

    private void setRecyclAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,
                false);
        binding.recyl.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(viewModel, new ArrayList<>());
        binding.recyl.setAdapter(adapter);
    }

    private void initializeHomeScreenViewModel() {
        if (getActivity() == null) return;
        homeScreenViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
    }
}
