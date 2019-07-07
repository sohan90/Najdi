package com.najdi.android.najdiapp.shoppingcart.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static com.najdi.android.najdiapp.common.Constants.FragmentTags.PRODUCT_LIST_FRAG;

public class CartFragment extends BaseFragment {

    FragmentCartBinding binding;
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

    private void removeItem(int position, String s) {
        showProgressDialog();
        LiveData<BaseResponse> baseResponseLiveData = viewModel.removeCart(s);
        baseResponseLiveData.observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                updateCartSizeForRemoveItem(adapterList.get(position).getQuantity());
                updateAdapterForRemoveItem(position);
                String message = baseResponse.getData().getMessage();
                ToastUtils.getInstance(getActivity()).showLongToast(message);
            }
        });
    }

    private void updateAdapterForRemoveItem(int position) {
        adapterList.remove(position);
        adapter.notifyItemRemoved(position);
        updateTotal();
        if (adapterList.size() == 0){
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
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);
    }

    private void subscribeForCartResponse() {
        showProgressDialog();
        homeScreenViewModel.getCart().observe(this, cartResponse -> {
            hideProgressDialog();
            if (cartResponse != null) {
                if (cartResponse.getData() != null) {
                    if (cartResponse.getData().getCartdata().size() > 0) {
                        viewModel.setTotal(cartResponse.getData().getCartdata());
                        adapterList = cartResponse.getData().getCartdata();
                        adapter.setData(adapterList);
                        homeScreenViewModel.getCart().removeObservers(this);
                    } else {
                        showEmptyCartValueTxt();
                    }
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
                    //updateTotal();
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
                model.setProductId(cartData.getProductId());
                model.setT(cartData);
                model.setFromCartScreen(true);
                if (getActivity() == null) return;
                FragmentHelper.popBackStack(getActivity());
                homeScreenViewModel.getLaunchProductDetailLiveData().setValue(model);
            }

            @Override
            public void onUpdateQuantity(int adapterPosition, String cartItemKey, int quantity) {
                updateTotal();
                updateItemQuantity(adapterPosition, cartItemKey, quantity);
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
        updateCartRequest.setCartItemKey(cartItemKey);
        updateCartRequest.setQuantity(String.valueOf(quantity));

        LiveData<BaseResponse> liveData = viewModel.updateQuantity(updateCartRequest);
        liveData.observe(this, baseResponse -> {

            hideProgressDialog();
            updateCart();
            if (baseResponse != null && baseResponse.getData() != null) {
                ToastUtils.getInstance(getActivity()).
                        showShortToast(baseResponse.getData().getMessage());
            } else {
                CartResponse.CartData cartData = adapterList.get(adapterPosition);
                cartData.setQuantity(cartData.getPreviousQuantity());
                adapter.setData(adapterList);
            }
        });
    }

    private void initializeHomeScreenViewModel() {
        if (getActivity() == null) return;
        homeScreenViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
    }
}
