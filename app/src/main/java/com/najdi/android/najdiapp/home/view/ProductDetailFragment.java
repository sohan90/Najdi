package com.najdi.android.najdiapp.home.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.FragmentProductDetailBinding;
import com.najdi.android.najdiapp.databinding.ItemDetailBinding;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductDetailViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductListItemModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.utitility.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.common.Constants.APPEND_ATTRIBUTE_STR;

public class ProductDetailFragment extends BaseFragment {
    private static final String EXTRA_PRODUCT_DETAIL_KEY = "product_detail_key";
    private FragmentProductDetailBinding binding;
    private HomeScreenViewModel homeScreeViewModel;
    private int productId;
    private ProductDetailViewModel viewModel;
    private CartResponse.CartData cartData;
    private ProductListResponse productListResponse;
    private boolean isFromCartScreen;
    private HashMap<String, String> variationMap = new HashMap<>();

    public static ProductDetailFragment createInstance(ProductDetailBundleModel model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT_DETAIL_KEY, model);
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
        updateCartLableButton();
        initializeViewModel();
        bindViewModelWithLiveData();
        initializeHomeScreenViewModel();
        initializeClickListener();
        fetchProductDetail();
        return binding.getRoot();
    }


    private void updateCartLableButton() {
        if (isFromCartScreen) {
            binding.addToCart.setText(getString(R.string.update_cart_label));
        }
    }

    private void fetchProductDetail() {
        showProgressDialog();
        homeScreeViewModel.getIndividualProduct(productId).observe(this, productListResponse1 -> {
            hideProgressDialog();
            if (productListResponse1 != null) {
                this.productListResponse = productListResponse1;
                setViewDataForIncludeLyt();
                viewModel.setDefaultPrice(productListResponse.getPrice());
                inflateViewForProductVariation();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        homeScreeViewModel.getSetToolBarTitle().setValue(getString(R.string.product_details));
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
    }

    private void initializeClickListener() {
        binding.dec.setOnClickListener(v -> viewModel.decrementQuantity());
        binding.inc.setOnClickListener(v -> viewModel.incrementQuantity());
        binding.reset.setOnClickListener(v -> reset());
        binding.proceed.setOnClickListener(v -> this.launchCheckOutActivity());
        binding.addToCart.setOnClickListener(v -> {
            showProgressDialog();
            if (!isFromCartScreen) {
                addCart(homeScreeViewModel.getCartSize() + 1); // add cart
            } else {
                //update cart :Since api wont support update cart directly so
                // to update cart  first delete product and add with updated product item
                LiveData<BaseResponse> liveData = viewModel.removeCart(cartData.getTm_cart_item_key());
                liveData.observe(this, baseResponse -> {
                    if (baseResponse != null) {
                        addCart(homeScreeViewModel.getCartSize());
                    }
                });
            }
        });
    }

    private void addCart(int existingCartSize) {
        LiveData<BaseResponse> liveData = viewModel.addToCart();
        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            homeScreeViewModel.setCartSize(existingCartSize);
            updateNotificationCartCount(homeScreeViewModel.getCartSize());
            moveToAddCartScreen();

        });
    }

    private void launchCheckOutActivity() {
        homeScreeViewModel.getLaunchCheckoutActivity().setValue(true);
    }


    private void moveToAddCartScreen() {
        homeScreeViewModel.getReplaceFragmentLiveData().
                setValue(Constants.ScreeNames.SHOPPING_CART);
    }

    private void setViewDataForIncludeLyt() {
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

                            String selectedValue = getSelectedValueFromMap(s);
                            viewModel.updatePrice(productListResponse, selectedValue, selectedAttributeId);
                            detailBinding.options.setText(s);

                        });
                    }

                }));
            }
        }
    }

    private String getSelectedValueFromMap(String selectedNameKey) {
        String slugValue = "";
        if (variationMap.size() > 0) {
            slugValue = variationMap.get(selectedNameKey);
        }
        return slugValue;
    }

    private void reset() {
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
            String name = stringStringHashMap.get("name");
            String slug = stringStringHashMap.get("slug");
            list.add(name);
            variationMap.put(name, slug);
        }
        return list;
    }

    private void getProductDetailFromBundle() {
        if (getArguments() != null && getArguments().containsKey(EXTRA_PRODUCT_DETAIL_KEY)) {
            ProductDetailBundleModel model = getArguments().getParcelable(EXTRA_PRODUCT_DETAIL_KEY);
            if (model == null) return;
            productId = model.getProductId();
            cartData = (CartResponse.CartData) model.getT();
            isFromCartScreen = model.isFromCartScreen();
        }
    }

    private void initializeHomeScreenViewModel() {
        if (getActivity() != null) {
            homeScreeViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
        }
    }
}
