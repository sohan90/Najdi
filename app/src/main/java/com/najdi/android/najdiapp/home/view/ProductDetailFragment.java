package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentProductDetailBinding;
import com.najdi.android.najdiapp.databinding.ItemDetailBinding;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductDetailViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductListItemModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class ProductDetailFragment extends BaseFragment {
    private static final String EXTRA_PRODUCT_DETAIL_KEY = "product_detail_key";
    private FragmentProductDetailBinding binding;
    private HomeScreenViewModel homeScreeViewModel;
    private ProductListResponse productListResponse;
    private ProductDetailViewModel viewModel;

    public static ProductDetailFragment createInstance(ProductListResponse productListResponse) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT_DETAIL_KEY, productListResponse);
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
        showCartImageInActivityToolBar();
        return binding.getRoot();
    }

    private void showCartImageInActivityToolBar() {
        homeScreeViewModel.showCartImageLiveData().setValue(true);
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
        binding.dec.setOnClickListener(v -> viewModel.decreamentQuantity());
        binding.inc.setOnClickListener(v -> viewModel.increamentQuantity());
    }

    private void setDetailsFromBundle() {
        binding.topLyt.setViewModel(new ProductListItemModel(productListResponse, View.GONE));
    }

    private void inflateViewForProductVariation() {
        if (productListResponse != null && productListResponse.getAttributesList() != null) {
            List<ProductListResponse.Attributes> attributesList = productListResponse.getAttributesList();
            for (ProductListResponse.Attributes attributes : attributesList) {
                List<String> stringList = getListFromMap(attributes.getOptions());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_detail,
                        binding.container, false);
                binding.container.addView(view);
                ItemDetailBinding detailBinding = ItemDetailBinding.bind(view);
                detailBinding.name.setText(attributes.getName());
                detailBinding.options.setOnClickListener((v -> {
                    if (getActivity() != null) {
                        DialogUtil.showPopuwindow(getActivity(), v, stringList, s -> {
                            viewModel.updatePrice(productListResponse, s);
                            detailBinding.options.setText(s);
                        });
                    }
                }));
            }

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
        }
    }

    private void initializeHomeScreenViewModel() {
        if (getActivity() != null) {
            homeScreeViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
        }
    }
}
