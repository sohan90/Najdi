package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.FragmentProductDetailBinding;
import com.najdi.android.najdiapp.databinding.ItemDetailBinding;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductDetailViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductListItemModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.view.CartFragment;
import com.najdi.android.najdiapp.utitility.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.common.Constants.APPEND_ATTRIBUTE_STR;

public class ProductDetailFragment extends BaseFragment {
    private static final String EXTRA_PRODUCT_DETAIL_KEY = "product_detail_key";
    private static final String EXTR_PRODUCT_SELECTED_VARIATION = "selected_variation";
    private FragmentProductDetailBinding binding;
    private HomeScreenViewModel homeScreeViewModel;
    private ProductListResponse productListResponse;
    private ProductDetailViewModel viewModel;
    private CartResponse.CartData cartData;

    public static ProductDetailFragment createInstance(ProductListResponse productListResponse,
                                                       HashMap<String, String> selectedVartionHashMap) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT_DETAIL_KEY, productListResponse);
        bundle.putSerializable(EXTR_PRODUCT_SELECTED_VARIATION, selectedVartionHashMap);
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        productDetailFragment.setArguments(bundle);
        return productDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container,
                false);
        getProductDetailFromBundle();
        setDetailsFromBundle();
        initializeViewModel();
        initializeHomeScreenViewModel();
        inflateViewForProductVariation();
        initializeClickListener();
        bindViewModelWithLiveData();
        return binding.getRoot();
    }


    private void updateNotificationCartCount(int count) {
        homeScreeViewModel.updateNotificationCartCount().setValue(count);
    }

    private void bindViewModelWithLiveData() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
        viewModel.setDefaultPrice(productListResponse.getPrice());
    }

    private void initializeClickListener() {
        binding.dec.setOnClickListener(v -> viewModel.decrementQuantity());
        binding.inc.setOnClickListener(v -> viewModel.incrementQuantity());
        binding.reset.setOnClickListener(v -> reset());
        binding.addToCart.setOnClickListener(v -> {
            showProgressDialog();
            LiveData<BaseResponse> liveData = viewModel.addToCart();
            liveData.observe(this, baseResponse -> {
                hideProgressDialog();
                if (baseResponse != null && baseResponse.getCode().equalsIgnoreCase("200")) {
                    homeScreeViewModel.setCartSize(homeScreeViewModel.getCartSize() + 1);
                    updateNotificationCartCount(homeScreeViewModel.getCartSize());
                    moveToAddCartScreen();
                }
            });
        });
    }


    private void moveToAddCartScreen() {
        CartFragment cartFragment = CartFragment.createInstance();
        homeScreeViewModel.getReplaceFragmentLiveData().setValue(cartFragment);
    }

    private void setDetailsFromBundle() {
        binding.topLyt.setViewModel(new ProductListItemModel(productListResponse, View.GONE));
    }

    private void inflateViewForProductVariation() {
        binding.container.removeAllViews();
        if (productListResponse != null && productListResponse.getAttributesList() != null) {
            List<ProductListResponse.Attributes> attributesList = productListResponse.getAttributesList();
            for (ProductListResponse.Attributes attributes : attributesList) {
                List<String> stringList = getListFromMap(attributes.getOptions());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_detail,
                        binding.container, false);
                binding.container.addView(view);
                ItemDetailBinding detailBinding = ItemDetailBinding.bind(view);
                detailBinding.name.setText(attributes.getName());
                setDataForSelectedValue(detailBinding.options, attributes.getId(),
                        attributes.getSlug());
                detailBinding.options.setTag(attributes.getId());
                detailBinding.options.setOnClickListener((v -> {
                    if (getActivity() != null) {
                        int selectedAttributeId = (int) v.getTag();
                        DialogUtil.showPopuwindow(getActivity(), v, stringList, s -> {
                            viewModel.updatePrice(productListResponse, s, selectedAttributeId);
                            detailBinding.options.setText(s);
                        });
                    }
                }));
            }

        }
    }

    private void reset(){
        for (int i = 0; i < binding.container.getChildCount(); i++) {
            View view = binding.container.getChildAt(i);
            ItemDetailBinding detailBinding = DataBindingUtil.getBinding(view);
            detailBinding.options.setText("");
        }
        viewModel.reset();
    }

    private void setDataForSelectedValue(TextInputEditText optionText, int id, String key) {
        if (cartData != null && cartData.getVariation().size() > 0) {
            String appendedKey = APPEND_ATTRIBUTE_STR.concat(key);
            String selectedValue = cartData.getVariation().get(appendedKey);
            optionText.setText(selectedValue);
            viewModel.setTotalPrice(String.valueOf(cartData.getLineTotal()));
            viewModel.setQuantityCount(cartData.getQuantity());
            viewModel.updatePrice(productListResponse, selectedValue, id);
        }
    }

    private List<String> getListFromMap(List<HashMap<String, String>> hashMapList) {
        List<String> list = new ArrayList<>();
        for (HashMap<String, String> stringStringHashMap : hashMapList) {
            list.add(stringStringHashMap.get("slug"));// for english
        }
        return list;
    }

    private void getProductDetailFromBundle() {
        if (getArguments() != null && getArguments().containsKey(EXTRA_PRODUCT_DETAIL_KEY)) {
            productListResponse = getArguments().getParcelable(EXTRA_PRODUCT_DETAIL_KEY);
            if (productListResponse != null) {
                cartData = productListResponse.getCartData();
            }
        }
    }

    private void initializeHomeScreenViewModel() {
        if (getActivity() != null) {
            homeScreeViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
        }
    }
}
