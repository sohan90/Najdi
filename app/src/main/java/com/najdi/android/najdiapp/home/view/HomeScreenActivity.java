package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityHomeScreenBinding;
import com.najdi.android.najdiapp.databinding.NavHeaderHomeScreenBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.view.CartFragment;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.utitility.PreferenceUtils.USER_NAME_KEY;

public class HomeScreenActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeScreenBinding binding;
    private DrawerLayout drawerLayout;
    private HomeScreenViewModel viewModel;
    private TextView toolBarTitle;
    private View cartImageLyt;
    private TextView notificationText;
    private int cartSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        initializeViewModel();
        setNavigationBar();
        replaceFragment(ProductListFragment.createInstance());
        subscribeForLaunchProductDetail();
        subscribeForShowCartImage();
        subscribeReplaceFragment();
        fetchProduct();
        fetchCart();
    }

    private void subscribeForShowCartImage() {
        viewModel.updateNotificationCartCount().observe(this, count -> {
            if (count != -1) {
                cartSize++;
                updateNotificationIconText();
            }
        });
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(HomeScreenViewModel.class);
    }

    private void replaceFragment(Fragment fragment) {
        int containerId = binding.include.containerLyt.container.getId();
        FragmentHelper.replaceFragment(this, fragment, Constants.FragmentTags.PRODUCT_LIST_FRAG,
                true, containerId);
    }

    private void subscribeForLaunchProductDetail() {
        viewModel.getLaunchProductDetailLiveData().observe(this, productListResponse -> {
            if (productListResponse != null) {
                setToolBarTitle(getString(R.string.product_details));
                updateNotificationIconText();
                replaceFragment(ProductDetailFragment.createInstance(productListResponse));
            }
        });
    }

    private void subscribeReplaceFragment() {
        viewModel.getReplaceFragmentLiveData().observe(this, this::replaceFragment);
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
                cartSize = data.getCartdata().size();
            }
        });
    }

    private void updateNotificationIconText() {
        cartImageLyt.setVisibility(View.VISIBLE);
        notificationText.setText(String.valueOf(cartSize));
    }

    private void setNavigationBar() {
        setSupportActionBar(binding.include.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_actiob_bar, null);
        getSupportActionBar().setCustomView(viewActionBar, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER));

        toolBarTitle = viewActionBar.findViewById(R.id.title);
        cartImageLyt = viewActionBar.findViewById(R.id.cartImageLyt);
        notificationText = viewActionBar.findViewById(R.id.notification_text);
        toolBarTitle.setText(getString(R.string.category));
        toolBarTitle.setGravity(Gravity.CENTER);
        drawerLayout = binding.drawerLayout;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, binding.include.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        setNavHeader();
    }

    private void setNavHeader() {
        View view = binding.navView.getHeaderView(0);
        NavHeaderHomeScreenBinding binding = NavHeaderHomeScreenBinding.bind(view);
        binding.name.setText(PreferenceUtils.getValueString(this, USER_NAME_KEY));
    }

    private void setToolBarTitle(String title) {
        toolBarTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.shopping_cart:
                CartFragment cartFragment = CartFragment.createInstance();
                replaceFragment(cartFragment);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
