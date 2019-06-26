package com.najdi.android.najdiapp.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.view.BankDetailFragment;
import com.najdi.android.najdiapp.checkout.view.CheckoutActivity;
import com.najdi.android.najdiapp.checkout.view.OrderStatusFragment;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.common.ObservableManager;
import com.najdi.android.najdiapp.databinding.ActivityHomeScreenBinding;
import com.najdi.android.najdiapp.databinding.MenuLanSelcBinding;
import com.najdi.android.najdiapp.databinding.NavHeaderHomeScreenBinding;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.launch.view.LoginActivity;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.view.CartFragment;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.Observable;
import java.util.Observer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;
import static com.najdi.android.najdiapp.common.Constants.ENGLISH_LAN;
import static com.najdi.android.najdiapp.common.Constants.FragmentTags.PRODUCT_LIST_FRAG;
import static com.najdi.android.najdiapp.common.Constants.LAUNCH_CART;
import static com.najdi.android.najdiapp.common.Constants.OBSERVER_INTENT_CART_RESPONSE;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.ABOUT_US;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.BANK_ACCOUNTS;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.ORDER_STATUS;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.PRODUCTS;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.PRODUCT_DETAIL;
import static com.najdi.android.najdiapp.common.Constants.ScreeNames.SHOPPING_CART;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_ID_KEY;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_LOGIIN_TOKEN;
import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_NAME_KEY;

public class HomeScreenActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {

    private ActivityHomeScreenBinding binding;
    private DrawerLayout drawerLayout;
    private HomeScreenViewModel viewModel;
    private TextView toolBarTitle;
    private View cartImageLyt;
    private TextView notificationText;
    private ActionBarDrawerToggle toggle;
    private ProductDetailBundleModel productDetailBundleModel;

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
        observeForProductDetailScreenFromCheckout();

        fetchProduct();
        fetchCart();
    }

    private void observeForProductDetailScreenFromCheckout() {
        ObservableManager.getInstance().addObserver(this);
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
        String fragmentTag = null;
        switch (screenName) {
            case PRODUCTS:
                fragment = ProductListFragment.createInstance();
                fragmentTag = Constants.FragmentTags.PRODUCT_LIST_FRAG;
                unlockDrawer();
                break;

            case PRODUCT_DETAIL:
                fragment = ProductDetailFragment.createInstance(productDetailBundleModel);
                fragmentTag = Constants.FragmentTags.PRODUCT_DETAIL;
                lockDrawer();
                break;

            case SHOPPING_CART:
                fragment = CartFragment.createInstance();
                fragmentTag = Constants.FragmentTags.SHOPPING_CART;
                lockDrawer();
                break;

            case ORDER_STATUS:
                fragment = OrderStatusFragment.createInstance();
                fragmentTag = Constants.FragmentTags.ORDER_STATUS;
                lockDrawer();
                break;

            case BANK_ACCOUNTS:
                fragment = BankDetailFragment.createInstance();
                fragmentTag = Constants.FragmentTags.BANK_ACCOUNT;
                lockDrawer();
                break;

            case ABOUT_US:
                fragment = AboutUsFragment.createInstance();
                fragmentTag = Constants.FragmentTags.ABOUT_US;
                lockDrawer();
                break;


            default:
                fragment = ProductListFragment.createInstance();
                unlockDrawer();
                break;

        }
        FragmentHelper.replaceFragment(this, fragment, fragmentTag,
                true, containerId);
    }

    private void subscribeForLaunchProductDetail() {
        viewModel.getLaunchProductDetailLiveData().observe(this, model -> {
            if (model != null) {
                this.productDetailBundleModel = model;
                setToolBarTitle(getString(R.string.product_details));
                updateNotificationIconText(viewModel.getCartSize());
                replaceFragment(PRODUCT_DETAIL);
            }
        });
    }

    private void subscribeReplaceFragment() {
        viewModel.getReplaceFragmentLiveData().observe(this, this::replaceFragment);
    }

    private void fetchProduct() {
        showProgressDialog();
        viewModel.getProducts().observe(this, productListResponses -> {
            hideProgressDialog();
            if (productListResponses != null && productListResponses.size() > 0) {
                viewModel.getProductList().setValue(productListResponses);
            } else {
                ToastUtils.getInstance(this).showShortToast("something went wrong");
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
        cartImageLyt.setOnClickListener(v -> launchCartScreen());
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
        setNavSubItemClicklistener();
    }

    private void launchCartScreen() {
        replaceFragment(SHOPPING_CART);
    }

    private void setNavSubItemClicklistener() {
        View view = binding.navView.getMenu().findItem(R.id.language).getActionView();
        MenuLanSelcBinding menuLanSelcBinding = MenuLanSelcBinding.bind(view);
        updateLanguageToggleBut(menuLanSelcBinding);
        menuLanSelcBinding.checkbox.setOnClickListener(v -> {
            SwitchCompat switchCompat = (SwitchCompat) v;
            if (switchCompat.isChecked()) {
                setLocaleLanguage(ARABIC_LAN);
            } else {
                setLocaleLanguage(ENGLISH_LAN);
            }
            FragmentHelper.popBackStack(this, null);
            Intent intent = new Intent(this, HomeScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //recreate();
        });
    }

    private void updateLanguageToggleBut(MenuLanSelcBinding menuLanSelcBinding) {
        if (getCurrentLocale().equals(ARABIC_LAN)) {
            menuLanSelcBinding.checkbox.setChecked(true);
        } else {
            menuLanSelcBinding.checkbox.setChecked(false);
        }
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
                    updateNavigationMenuHighlight(0);
                    unlockDrawer();
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

    public void updateNavigationMenuHighlight(int index) {
        binding.navView.getMenu().getItem(index).setChecked(true);
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
                FragmentHelper.popBackStack(this, PRODUCT_LIST_FRAG);
                replaceFragment(PRODUCTS);
                break;

            case R.id.about_us:
                replaceFragment(ABOUT_US);
                break;

            case R.id.bank_account:
                replaceFragment(BANK_ACCOUNTS);
                break;

            case R.id.history:
                replaceFragment(ORDER_STATUS);

                break;

            case R.id.log_out:
                clearCredentialandFinish();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearCredentialandFinish() {
        showProgressDialog();
        new Handler().postDelayed(() -> {
            hideProgressDialog();
            PreferenceUtils.setValueInt(this, USER_ID_KEY, 0);
            PreferenceUtils.setValueString(this, USER_LOGIIN_TOKEN, null);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 1000);

    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Intent) {
            Intent intent = (Intent) arg;
            if (intent.hasExtra(LAUNCH_CART)) {
                replaceFragment(SHOPPING_CART);
            } else {
                ProductDetailBundleModel productDetailBundleModel = intent.
                        getParcelableExtra(OBSERVER_INTENT_CART_RESPONSE);
                viewModel.getLaunchProductDetailLiveData().setValue(productDetailBundleModel);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObservableManager.getInstance().deleteObserver(this);
    }

    private void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
