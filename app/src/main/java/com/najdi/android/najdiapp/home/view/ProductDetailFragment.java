package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.FragmentProductDetailBinding;
import com.najdi.android.najdiapp.databinding.ItemDetailBinding;
import com.najdi.android.najdiapp.home.model.AttributeOptionModel;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductDetailViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductListItemModel;
import com.najdi.android.najdiapp.shoppingcart.model.AttributeCartOptionData;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends BaseFragment {
    private static final String EXTRA_PRODUCT_DETAIL_KEY = "product_detail_key";
    private FragmentProductDetailBinding binding;
    private HomeScreenViewModel homeScreeViewModel;
    private String productId;
    private ProductDetailViewModel viewModel;
    private CartResponse.CartData cartData;
    private ProductListResponse productListResponse;
    private boolean isFromCartScreen;

    public static ProductDetailFragment createInstance(ProductDetailBundleModel model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT_DETAIL_KEY, model);
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        productDetailFragment.setArguments(bundle);
        return productDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressDialog();
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

    @Deprecated
    private void getVariationFromServer() {
        showProgressDialog();
        LiveData<ProductListResponse> liveData = viewModel.
                getVariationQuantity(productListResponse.getId(), viewModel.getVariationId());

        liveData.observe(getViewLifecycleOwner(), productListResponse1 -> {
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
        homeScreeViewModel.getIndividualProduct(productId).observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                this.productListResponse = baseResponse.getProduct();
                setViewDataForIncludeLyt();
                viewModel.setDefaultPrice(productListResponse.getPrice());
                viewModel.setMaxVariationQuantity(Integer.parseInt(productListResponse.getStock()));
                viewModel.setTotalAttributOptSize(productListResponse.getTotalAttributeSize());
                enableOrDisableAddCartButton();
                updateQuantity();
                inflateViewForProductVariation();
            }
        });
    }

    private void updateQuantity() {
        if (cartData == null) {
            viewModel.setDefaultQuantity();
        } else {
            viewModel.setQuantityCount(Integer.parseInt(cartData.getQty()));
        }
    }

    private void enableOrDisableAddCartButton() {
        if (productListResponse.getStock().equals("0")) {
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

    private void initializeHomeScreenViewModel() {
        if (getActivity() != null) {
            homeScreeViewModel = new ViewModelProvider(getActivity()).get(HomeScreenViewModel.class);
        }
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
    }

    private void initializeClickListener() {
        binding.dec.setOnClickListener(v -> viewModel.decrementQuantity());
        binding.inc.setOnClickListener(v -> viewModel.incrementQuantity());
        binding.reset.setOnClickListener(v -> reset());
        binding.proceed.setOnClickListener(v -> moveToAddCartScreen());

        binding.addToCart.setOnClickListener(v -> {
            if (getActivity() == null) return;

            String userId = PreferenceUtils.getValueString(getActivity(),
                    PreferenceUtils.USER_ID_KEY);

            if (!TextUtils.isEmpty(userId)) {
                showProgressDialog();
                if (!isFromCartScreen) {
                    addCart(); // add cart
                } else {
                    updateCart();
                }
            } else {
                launchGuestUserDialog();//gues user flow
            }
        });
    }

    private void updateCart() {
        //update cart :Since api wont support update cart directly so
        // to update cart  first delete product and add with updated product item
        LiveData<BaseResponse> liveData = viewModel.removeCart(cartData.getId());
        liveData.observe(getViewLifecycleOwner(), baseResponse -> {
            if (baseResponse != null && baseResponse.isStatus()) {
                addCart();
            }
        });
    }

    private void addCart() {
        LiveData<BaseResponse> liveData = viewModel.addToCart(productId);
        liveData.observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                viewModel.enableProceed.setValue(true);
                updateNotificationCartCount();
                String message = getString(R.string.product_added_success);
                if (isFromCartScreen) {
                    message = getString(R.string.product_updated_success);
                }
                DialogUtil.showAlertDialog(getActivity(), message,
                        (dialog, which) -> dialog.dismiss());
            }
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

                List<AttributeOptionModel> attributeOptLst = attributes.getProductAttributeOptions();
                List<String> stringList = getListFromMap(attributeOptLst);

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_detail,
                        binding.container, false);
                binding.container.addView(view);

                ItemDetailBinding detailBinding = ItemDetailBinding.bind(view);
                detailBinding.name.setText(attributes.getName());

                updateSelectedOptionValue(detailBinding.options, attributes);
                detailBinding.options.setTag(attributes.getId());
                detailBinding.options.setOnClickListener((v -> {

                    if (getActivity() != null) {

                        String selectedAttributeId = (String) v.getTag();

                        DialogUtil.showPopupWindowSpinner(getActivity(), v, stringList, p -> {

                            AttributeOptionModel attributeOptionModel = attributeOptLst.get(p);
                            String selectedValue = attributeOptionModel.getOptionName();
                            detailBinding.options.setText(selectedValue);
                            viewModel.updatePrice(selectedAttributeId, attributeOptionModel);

                        });
                    }
                }));
            }
        }
    }

    private void updateSelectedOptionValue(TextInputEditText editText,
                                           ProductListResponse.Attributes attribute) {

        if (cartData != null && cartData.getAttributeData() != null) {
            for (AttributeCartOptionData attributeData : cartData.getAttributeData()) {
                if (attributeData == null) continue;

                if (attributeData.getAttr_id().trim().equals(attribute.getId().trim())) {
                    editText.setText(attributeData.getAttr_selected_option());

                    AttributeOptionModel attributeOptionModel = getAttriOptModel(
                            attribute.getProductAttributeOptions(),
                            attributeData.getAttr_selected_option_id());

                    viewModel.updatePrice(attribute.getId(), attributeOptionModel);

                    break;
                }
            }
        }
    }

    private AttributeOptionModel getAttriOptModel(List<AttributeOptionModel> productAttributeOptions,
                                                  String attrSelectedOptionId) {

        AttributeOptionModel attributeOptionModel = null;
        for (AttributeOptionModel productAttributeOption : productAttributeOptions) {
            if (productAttributeOption.getId().trim().equals(attrSelectedOptionId.trim())) {
                attributeOptionModel = productAttributeOption;
                break;
            }
        }
        return attributeOptionModel;
    }

    private void reset() {
        for (int i = 0; i < binding.container.getChildCount(); i++) {
            View view = binding.container.getChildAt(i);
            ItemDetailBinding detailBinding = DataBindingUtil.getBinding(view);
            if (detailBinding == null) return;
            detailBinding.options.setText("");
        }
        viewModel.reset();
    }

    private List<String> getListFromMap(List<AttributeOptionModel> productAttributeOptions) {
        List<String> attributesOption = new ArrayList<>();
        for (AttributeOptionModel productAttributeOption : productAttributeOptions) {
            String optionName = productAttributeOption.getOptionName();// concat option value with the price if it is visible
            if (productAttributeOption.getPriceVisibility().equals("1")) {
                optionName = optionName
                        .concat(" ")
                        .concat("(")
                        .concat(productAttributeOption.getOptionPrice())
                        .concat(")")
                        .concat("+")
                        .concat(" ")
                        .concat(getString(R.string.currency));
            }
            attributesOption.add(optionName);
        }
        return attributesOption;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}
