package com.najdi.android.najdiapp.shoppingcart.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.FragmentCartBinding;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.shoppingcart.viewmodel.CartViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.FragmentHelper;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends BaseFragment {

    private FragmentCartBinding binding;
    private HomeScreenViewModel homeScreenViewModel;
    private CartAdapter adapter;
    private CartViewModel viewModel;
    private List<CartResponse.CartData> adapterList;

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
        bindLiveData();
        setRecyclAdapter();
        subscribeForCartResponse();
        initializeClickListener();
        updateCart();
        return binding.getRoot();
    }

    private void initializeHomeScreenViewModel() {
        if (getActivity() == null) return;
        homeScreenViewModel = new ViewModelProvider(getActivity()).get(HomeScreenViewModel.class);
    }

    private void initializeClickListener() {
        binding.proceedTxt.setOnClickListener(v -> {
            if (getActivity() == null) return;
            homeScreenViewModel.getLaunchCheckoutActivity().setValue(true);

        });
    }

    private void bindLiveData() {
        binding.setCartViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeScreenViewModel.getSetToolBarTitle().setValue(getString(R.string.cart));
    }

    private void removeItem(int position, String cartId) {
        showProgressDialog();
        LiveData<BaseResponse> baseResponseLiveData = viewModel.removeCart(cartId);
        baseResponseLiveData.observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                updateCartSizeForRemoveItem(Integer.parseInt(adapterList.get(position).getQty()));
                updateAdapterForRemoveItem(position);
                DialogUtil.showAlertDialog(getActivity(), getString(R.string.item_removed),
                        (dialog, which) -> dialog.dismiss());
            }
        });
    }

    private void updateAdapterForRemoveItem(int position) {
        adapterList.remove(position);
        adapter.notifyItemRemoved(position);
        updateTotal();
        if (adapterList.size() == 0) {
            showEmptyCartValueTxt();
        }
    }

    private void showEmptyCartValueTxt() {
        binding.recyl.setVisibility(View.GONE);
        binding.proceedTxt.setVisibility(View.GONE);
        binding.bottomLyt.setVisibility(View.GONE);
        binding.placHolderTxt.setVisibility(View.VISIBLE);
    }

    private void updateCartSizeForRemoveItem(int quantity) {
        int cartSize = homeScreenViewModel.getCartSize() - quantity;
        updateCart();
        if (cartSize == 0) {
            showEmptyCartValueTxt();
        }
    }

    private void updateCart() {
        homeScreenViewModel.getCartCountNotification().setValue(true);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    private void subscribeForCartResponse() {
        showProgressDialog();
        homeScreenViewModel.getCart().observe(getViewLifecycleOwner(), cartResponse -> {
            hideProgressDialog();
            if (cartResponse != null && cartResponse.isStatus()) {
                if (cartResponse.getCart() != null && cartResponse.getCart().size() > 0) {
                    viewModel.setTotal(cartResponse.getCart());
                    adapterList = cartResponse.getCart();
                    adapter.setData(adapterList);
                    homeScreenViewModel.getCart().removeObservers(this);
                } else {
                    showEmptyCartValueTxt();
                }

            } else {
                showEmptyCartValueTxt();
            }
        });
    }

    private void setRecyclAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,
                false);
        binding.recyl.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(new CartAdapter.AdapterClickLisntener() {
            @Override
            public void onRemoveItem(int position, String cartItemKey) {
                if (!TextUtils.isEmpty(cartItemKey)) {
                    DialogUtil.showAlertWithNegativeButton(getActivity(),
                            getString(R.string.delete_msg), (dialog, which) -> {
                                dialog.dismiss();
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    removeItem(position, cartItemKey);
                                }
                            });

                }
            }

            @Override
            public void onEdit(CartResponse.CartData cartData) {
                ProductDetailBundleModel model = new ProductDetailBundleModel();
                model.setProductId(String.valueOf(cartData.getProductId()));
                model.setT(cartData);
                model.setFromCartScreen(true);
                if (getActivity() == null) return;
                FragmentHelper.popBackStack(getActivity());
                homeScreenViewModel.getLaunchProductDetailLiveData().setValue(model);
            }

            @Override
            public void onUpdateQuantity(int adapterPosition, String cartId, int quantity) {
                updateTotal();
                updateItemQuantity(adapterPosition, cartId, quantity);
            }
        }, new ArrayList<>());

        binding.recyl.setAdapter(adapter);
    }

    private void updateTotal() {
        viewModel.setTotal(adapterList);
    }

    private void updateItemQuantity(int adapterPosition, String cartItemKey, int quantity) {
        showProgressDialog();

        UpdateCartRequest updateCartRequest = new UpdateCartRequest();
        updateCartRequest.setId(cartItemKey);
        updateCartRequest.setQuantity(String.valueOf(quantity));

        LiveData<BaseResponse> liveData = viewModel.updateQuantity(updateCartRequest);
        liveData.observe(this, baseResponse -> {

            hideProgressDialog();
            updateCart();
            if (baseResponse != null && baseResponse.isStatus()) {
                DialogUtil.showAlertDialog(getActivity(), getString(R.string.quantity_updated),
                        (dialog, which) -> dialog.dismiss());
            } else {
                CartResponse.CartData cartData = adapterList.get(adapterPosition);
                cartData.setQty(String.valueOf(cartData.getPreviousQuantity()));
                adapter.setData(adapterList);
            }
        });
    }

}
