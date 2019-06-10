package com.najdi.android.najdiapp.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.checkout.view.CheckoutActivity;
import com.najdi.android.najdiapp.checkout.view.OrderStatusFragment;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityHomeScreenBinding;
import com.najdi.android.najdiapp.databinding.NavHeaderHomeScreenBinding;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.view.CartFragment;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.common.Constants.ScreeNames.ORDER_STATUS;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.PRODUCTS;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.PRODUCT_DETAIL;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.SHOPPING_CART;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_ID_KEY;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_NAME_KEY;

public class HomeScreenActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeScreenBinding binding;
    private DrawerLayout drawerLayout;
    private HomeScreenViewModel viewModel;
    private TextView toolBarTitle;
    private View cartImageLyt;
    private TextView notificationText;
    private ActionBarDrawerToggle toggle;
    private ProductListResponse productListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        initializeViewModel();
        setNavigationBar();
        replaceFragment(PRODUCTS);
        subscribeForLaunchProductDetail();
        subscribeForShowCartImage();
        subscribeReplaceFragment();
        subscribeToolBarTitle();
        subscribeForHomeScreenToolBar();
        subscribeForLaunchCheckoutScreen();
        fetchProduct();
        fetchCart();
    }

    private void subscribeForLaunchCheckoutScreen() {
        viewModel.getLaunchCheckoutActivity().observe(this, aBoolean -> {
            if (aBoolean) {
                launchCheckOutActivity();
            }
        });
    }

    private void launchCheckOutActivity() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    private void subscribeForHomeScreenToolBar() {
        viewModel.getSetHomeScreenToolBarLiveData().observe(this, aBoolean -> {
            if (aBoolean) {
                setHomeScreeToolBar();
            }
        });
    }

    private void subscribeToolBarTitle() {
        viewModel.getSetToolBarTitle().observe(this, this::setToolBarTitle);
    }


    private void subscribeForShowCartImage() {
        viewModel.updateNotificationCartCount().observe(this, count -> {
            if (count != -1) {
                updateNotificationIconText(count);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(HomeScreenViewModel.class);
    }

    private void replaceFragment(int screenName) {
        int containerId = binding.include.containerLyt.container.getId();
        Fragment fragment;
        switch (screenName) {
            case PRODUCTS:
                fragment = ProductListFragment.createInstance();
                break;

            case PRODUCT_DETAIL:
                fragment = ProductDetailFragment.createInstance(productListResponse);
                break;

            case SHOPPING_CART:
                fragment = CartFragment.createInstance();
                break;

            case ORDER_STATUS:
                fragment = OrderStatusFragment.createInstance();
                break;

            default:
                fragment = ProductListFragment.createInstance();
                break;

        }
        FragmentHelper.replaceFragment(this, fragment,
                Constants.FragmentTags.PRODUCT_LIST_FRAG, true, containerId);
    }

    private void subscribeForLaunchProductDetail() {
        viewModel.getLaunchProductDetailLiveData().observe(this, productListResponse -> {
            if (productListResponse != null) {
                this.productListResponse = productListResponse;
                setToolBarTitle(getString(R.string.product_details));
                updateNotificationIconText(viewModel.getCartSize());
                replaceFragment(PRODUCT_DETAIL);
            }
        });
    }

    private void subscribeReplaceFragment() {
        viewModel.getReplaceFragmentLiveData().observe(this, screenName ->
                replaceFragment(screenName));
    }

    private void fetchProduct() {
        showProgressDialog();
        viewModel.getProducts().observe(this, productListResponses -> {
            if (productListResponses != null && productListResponses.size() > 0) {
                hideProgressDialog();
                viewModel.getProductList().setValue(productListResponses);
            }
        });
    }

    private void fetchCart() {
        viewModel.getCart().observe(this, cartResponse -> {
            if (cartResponse != null) {
                CartResponse.Data data = cartResponse.getData();
                int cartSize = data.getCartdata().size();
                viewModel.setCartSize(cartSize);
            }
        });
    }

    private void updateNotificationIconText(int count) {
        cartImageLyt.setVisibility(View.VISIBLE);
        notificationText.setText(String.valueOf(count));
    }

    private void setNavigationBar() {
        setSupportActionBar(binding.include.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_actiob_bar, null);
        getSupportActionBar().setCustomView(viewActionBar, getLayoutParams());

        toolBarTitle = viewActionBar.findViewById(R.id.title);
        cartImageLyt = viewActionBar.findViewById(R.id.cartImageLyt);
        notificationText = viewActionBar.findViewById(R.id.notification_text);
        toolBarTitle.setText(getString(R.string.category));
        drawerLayout = binding.drawerLayout;

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, binding.include.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        setNavHeader();
    }

    private ActionBar.LayoutParams getLayoutParams() {
        return new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
    }

    private void setNavHeader() {
        View view = binding.navView.getHeaderView(0);
        NavHeaderHomeScreenBinding binding = NavHeaderHomeScreenBinding.bind(view);
        binding.name.setText(PreferenceUtils.getValueString(this, USER_NAME_KEY));
    }

    private void setToolBarTitle(String title) {
        toolBarTitle.setText(title);
        // Show back button
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Remove hamburger
        //toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(v -> onBackPressed());
        toggle.syncState();
    }


    private void setHomeScreeToolBar() {
        toolBarTitle.setText(getString(R.string.category));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackCount == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStackImmediate();
                Fragment fragment = FragmentHelper.
                        getFragmentById(this, binding.include.containerLyt.container.getId());

                if (fragment instanceof ProductListFragment) {
                    cartImageLyt.setVisibility(View.INVISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.setDrawerIndicatorEnabled(true);
                    toggle.syncState();

                } else if (fragment instanceof ProductDetailFragment) {
                    setToolBarTitle(getString(R.string.product_details));
                    cartImageLyt.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.shopping_cart:
                replaceFragment(SHOPPING_CART);
                break;

            case R.id.products:
                replaceFragment(PRODUCTS);
                break;

            case R.id.about_us:
                break;

            case R.id.history:
                replaceFragment(ORDER_STATUS);
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
