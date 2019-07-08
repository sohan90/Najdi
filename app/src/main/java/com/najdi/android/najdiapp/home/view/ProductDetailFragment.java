package com.najdi.android.najdiapp.home.view;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
        updateNotificationCartCount();
        subscribeForVariationQuantity();
        fetchProductDetail();
        return binding.getRoot();
    }

    private void subscribeForVariationQuantity() {
        viewModel.getGetVariaitionQuantity().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                getVariationFromServer();
            }
        });
    }

    private void getVariationFromServer() {
        showProgressDialog();
        LiveData<ProductListResponse> liveData = viewModel.getVariationQuantity(productListResponse.getId(),
                viewModel.getVariationId());

        liveData.observe(this, productListResponse1 -> {
            hideProgressDialog();
            if (productListResponse1 != null) {
                viewModel.setMaxVariationQuantity(productListResponse1.getStock_quantity());
            }
        });
    }


    private void updateNotificationCartCount() {
        homeScreeViewModel.getCartCountNotification().setValue(true);
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
                enableOrDisableAddCartButton();
                if (cartData == null) {
                    viewModel.setDefaultQuantity();
                }
            }
        });
    }

    private void enableOrDisableAddCartButton() {
        if (!productListResponse.isIn_stock()) {
            binding.dec.setEnabled(false);
            binding.inc.setEnabled(false);
            changeSmileIcon();
            binding.addToCart.setEnabled(true);
        }
    }

    private void changeSmileIcon() {
        if (getActivity() == null) return;
        binding.smileImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_sad));
        binding.availableTxt.setText(getString(R.string.not_available));
    }


    @Override
    public void onResume() {
        super.onResume();
        homeScreeViewModel.getSetToolBarTitle().setValue(getString(R.string.product_details));
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
        binding.proceed.setOnClickListener(v -> moveToAddCartScreen());

        binding.addToCart.setOnClickListener(v -> {
            showProgressDialog();
            if (!isFromCartScreen) {
                addCart(); // add cart
            } else {
                updateCart();
            }
        });
    }

    private void updateCart() {
        //update cart :Since api wont support update cart directly so
        // to update cart  first delete product and add with updated product item
        LiveData<BaseResponse> liveData = viewModel.removeCart(cartData.getTm_cart_item_key());
        liveData.observe(this, baseResponse -> {
            if (baseResponse != null) {
                addCart();
            }
        });
    }

    private void addCart() {
        LiveData<BaseResponse> liveData = viewModel.addToCart();
        liveData.observe(this, baseResponse -> {
            hideProgressDialog();
            binding.proceed.setEnabled(true);
            updateNotificationCartCount();
            String message = getString(R.string.product_added_success);
            if (isFromCartScreen) {
                message = getString(R.string.product_updated_success);
            }
            DialogUtil.showAlertDialog(getActivity(), message,
                    (dialog, which) -> dialog.dismiss());
        });
    }

    private void moveToAddCartScreen() {
        homeScreeViewModel.getReplaceFragmentLiveData().
                setValue(Constants.ScreeNames.SHOPPING_CART);
    }

    private void setViewDataForIncludeLyt() {
        binding.topLyt.desc.setMaxLines(Integer.MAX_VALUE);
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
            String attribute = APPEND_ATTRIBUTE_STR.concat(key);
            String appendedSlugKey = attribute.concat("_slug");
            String selectedSlugKeyValue = cartData.getVariation().get(appendedSlugKey);
            viewModel.setTotalPrice(String.valueOf(cartData.getLine_subtotal()));
            viewModel.setQuantityCount(cartData.getQuantity());
            optionText.setText(cartData.getVariation().get(attribute));
            viewModel.updatePrice(productListResponse, selectedSlugKeyValue, id);
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
