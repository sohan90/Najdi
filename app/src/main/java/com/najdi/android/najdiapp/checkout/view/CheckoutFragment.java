package com.najdi.android.najdiapp.checkout.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.CouponRequest;
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
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends BaseFragment {
    private FragmentCheckoutBinding binding;
    private CheckoutViewModel activityViewModel;
    private CheckoutAdapter checkoutAdapter;
    private CheckoutFragmentViewModel viewModel;
    private List<CartResponse.CartData> adapterList;
    private String couponToken;

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

    private void initializeActivityViewModel() {
        if (getActivity() == null) return;
        activityViewModel = new ViewModelProvider(getActivity()).get(CheckoutViewModel.class);
    }

    private void initializeClickListener() {
        binding.applyCoupon.setOnClickListener(v ->
                applyCoupon(binding.couponCode.getText().toString()));

        binding.includeLyt.close.setOnClickListener(v -> {
            removeCoupon();
        });
        binding.includeLyt.placeOrder.setOnClickListener(v -> {
            int checkedId = binding.includeLyt.radiGrp.getCheckedRadioButtonId();
            RadioButton radioButton = binding.includeLyt.radiGrp.findViewById(checkedId);
            activityViewModel.getCheckoutLiveData().setValue((String) radioButton.getTag());
        });
    }

    private void removeCoupon() {
        if (getActivity() != null && !TextUtils.isEmpty(couponToken)) {

            showProgressDialog();
            String userId = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_ID_KEY);
            CouponRequest couponRequest = new CouponRequest();
            couponRequest.setUserId(userId);
            couponRequest.setCouponToken(couponToken);
            couponRequest.setLang(resourceProvider.getCountryLang());

            viewModel.removeCoupon(couponRequest).observe(getViewLifecycleOwner(), baseResponse -> {
                hideProgressDialog();
                if (baseResponse.isStatus()) {
                    binding.couponCode.setText("");// reset
                    binding.includeLyt.couponLyt.setVisibility(View.GONE);
                    DialogUtil.showAlertDialog(getActivity(), baseResponse.getMessage(),
                            (d, w) -> d.dismiss());
                } else {
                    DialogUtil.showAlertDialog(getActivity(), baseResponse.getMessage(),
                            (d, w) -> d.dismiss());
                }
            });
        }
    }

    private void applyCoupon(String couponCode) {
        if (getActivity() == null) return;

        showProgressDialog();
        String userId = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_ID_KEY);
        CouponRequest couponRequest = new CouponRequest();
        couponRequest.setCouponCode(couponCode);
        couponRequest.setLang(resourceProvider.getCountryLang());
        couponRequest.setUserId(userId);

        viewModel.applyCoupon(couponRequest).observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse.isStatus()) {
                couponToken = baseResponse.getCouponToken();
                binding.includeLyt.couponLyt.setVisibility(View.VISIBLE);
                viewModel.updateCoupon(baseResponse.getCouponCode(),
                        baseResponse.getDiscountAmount());
                DialogUtil.showAlertDialog(getActivity(), baseResponse.getMessage(),
                        (d, wh) -> d.dismiss());

            } else {
                DialogUtil.showAlertDialog(getActivity(), baseResponse.getMessage(),
                        (d, w) -> d.dismiss());
            }
        });
    }

    private void bindViewModel() {
        binding.includeLyt.setViewModel(viewModel);
        binding.includeLyt.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(CheckoutFragmentViewModel.class);
    }

    private void initializeRecyclerViewAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        binding.recyclView.setLayoutManager(linearLayoutManager);
        checkoutAdapter = new CheckoutAdapter(new CartAdapter.AdapterClickLisntener() {
            @Override
            public void onRemoveItem(int position, String cartItemKey) {
                DialogUtil.showAlertWithNegativeButton(getActivity(), null,
                        getString(R.string.delete_msg), (dialog, which) -> {
                            dialog.dismiss();
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                removeItem(position, cartItemKey);
                            }
                        });
            }

            @Override
            public void onEdit(CartResponse.CartData cartData) {
                if (cartData != null) {
                    ProductDetailBundleModel model = new ProductDetailBundleModel();
                    model.setProductId(String.valueOf(cartData.getProductId()));
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

    private void updateTotal() {
        viewModel.udpateTotal(adapterList);
    }

    private void removeItem(int position, String s) {
        if (getActivity() == null) return;
        showProgressDialog();
        String userId = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_ID_KEY);
        LiveData<BaseResponse> baseResponseLiveData = viewModel.removeCart(userId, s);
        baseResponseLiveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.isStatus()) {
                    updateAdapterForRemoveItem(position);
                    DialogUtil.showAlertDialog(getActivity(), getString(R.string.item_removed), (dialog, which) -> {
                                dialog.dismiss();
                                activityViewModel.getCartCountNotification().setValue(true);
                            }
                    );
                } else {
                    DialogUtil.showAlertDialog(getActivity(), baseResponse.getMessage(),
                            (dialog, which) ->
                                    dialog.dismiss());
                }
            }
        });
    }


    private void updateAdapterForRemoveItem(int position) {
        adapterList.remove(position);
        checkoutAdapter.notifyItemRemoved(position);
        updateTotal();
    }

    private void updateItemQuantity(int adapterPosition, String cartItemKey, int quantity) {
        showProgressDialog();

        UpdateCartRequest updateCartRequest = new UpdateCartRequest();
        updateCartRequest.setId(cartItemKey);
        updateCartRequest.setQuantity(String.valueOf(quantity));

        LiveData<BaseResponse> liveData = viewModel.updateQuantity(updateCartRequest);
        liveData.observe(this, baseResponse -> {

            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                activityViewModel.updateCart().setValue(true);
                ToastUtils.getInstance(getActivity()).
                        showShortToast(baseResponse.getMessage());
            } else {
                // failure case
                /*CartResponse.CartData cartData = adapterList.get(adapterPosition);
                cartData.setQuantity(cartData.getPreviousQuantity());
                cartData.setSubtotal(String.valueOf(cartData.getPreviousTotal()));
                checkoutAdapter.setDataList(adapterList);*/
            }
            updateTotal();
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

    private void subscribeForCartResponse() {
        showProgressDialog();
        activityViewModel.getCartResponseMutableLiveData().observe(getViewLifecycleOwner(),
                cartResponse -> {
                    hideProgressDialog();
                    if (cartResponse != null && cartResponse.isStatus()) {
                        viewModel.udpateTotal(cartResponse.getCart());
                        viewModel.getVariationDetails(cartResponse.getCart())
                                .observe(getViewLifecycleOwner(), cartData -> {
                                    adapterList = cartData;
                                    updateAdapter(adapterList);
                                });
                    }
                });
    }

    private void updateAdapter(List<CartResponse.CartData> cartDataList) {
        checkoutAdapter.setDataList(cartDataList);
    }
}
