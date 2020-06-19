package com.najdi.android.najdiapp.checkout.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.FetchAddressIntentService;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.AnimationEndListener;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.common.ObservableManager;
import com.najdi.android.najdiapp.databinding.ActivityCheckoutBinding;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.GpsUtils;
import com.najdi.android.najdiapp.utitility.PermissionUtils;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

import static com.najdi.android.najdiapp.common.Constants.LAUNCH_CART;
import static com.najdi.android.najdiapp.common.Constants.LAUNCH_PRODUCT;
import static com.najdi.android.najdiapp.utitility.GpsUtils.GPS_REQUEST;
import static com.najdi.android.najdiapp.utitility.PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE;

public class CheckoutActivity extends BaseActivity {
    ActivityCheckoutBinding binding;
    private static final int STEP_ONE = 1;
    private static final int STEP_TWO = 2;
    private static final int STEP_THREE = 3;
    private CheckoutViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private boolean isGPS;
    private LocationCallback locationCallback;
    private CartResponse cartResponse;
    private BillingAddress billing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);
        initializeViewModel();
        initToolBar();
        intializeLocationReq();
        initializeFusedLocation();
        initializeConnectionCallBack();
        subscribeGetCurrentLocation();
        subscribeForProgress();
        subscribeForBillingData();
        subscribeForCheckout();
        subscribeupdateCartFromCheckoutScreen();
        subscribeForCartCountNotification();
        subscribeForHideCart();
        replaceFragment(STEP_ONE);
        fetchCart();
        fetchCartCount();
    }

    private void subscribeForHideCart() {
        viewModel.getHideCart().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.toolbar.backArrow.setVisibility(View.GONE);
                binding.toolbar.cartImageLyt.setVisibility(View.GONE);
            }
        });
    }

    private void subscribeForCartCountNotification() {
        viewModel.getCartCountNotification().observe(this, aBoolean -> {
            if (aBoolean) {
                fetchCartCount();
                fetchCart();
            }
        });
    }

    private void fetchCartCount() {
        viewModel.getCartCount().observe(this, baseResponse -> {
            if (baseResponse != null && baseResponse.isStatus()) {
                if (baseResponse.getTotalItems() == 0) {
                    finish();
                    addDisposable(Observable.timer(100, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(l -> launchHomeScreen()));
                } else {
                    updateCartValueTxt(baseResponse.getTotalItems());
                }
            }
        });
    }

    private void launchHomeScreen() {
        Intent intent = new Intent();
        intent.putExtra(LAUNCH_PRODUCT, true);
        ObservableManager.getInstance().notifyData(intent);
    }

    private void subscribeupdateCartFromCheckoutScreen() {
        viewModel.updateCart().observe(this, shouldUpdate -> {
            if (shouldUpdate) {
                fetchCartCount();
                fetchCart();
            }
        });
    }


    private void subscribeForCheckout() {
        viewModel.getCheckoutLiveData().observe(this, paymentMode -> {
            if (paymentMode != null) {
                showProgressDialog();
                String userId = PreferenceUtils.getValueString(this, PreferenceUtils.USER_ID_KEY);
                createOrder(userId, paymentMode.equalsIgnoreCase("cod") ?  0 : 1);
            }
        });
    }

    private void createOrder(String userId, int paymentMode) {
        billing.setPayment_method(paymentMode);
        LiveData<OrderResponse> orderResponseLiveData = viewModel.createOrder(userId, billing);

        orderResponseLiveData.observe(this, orderResponse -> {
            hideProgressDialog();
            if (orderResponse != null) {
                if (orderResponse.isStatus()) {
                    viewModel.orderResponseMutableLiveData().setValue(orderResponse);
                    handleProgress(100);
                    clearCart();
                } else {
                    ToastUtils.getInstance(this).showLongToast(orderResponse.getMessage());
                }
            }
        });
    }

    private void clearCart() {
        viewModel.clearCart().observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                binding.toolbar.notificationText.setText("0");
            }
        });
    }

    private void subscribeForBillingData() {
        viewModel.getBillingMutableLiveData().observe(this, billing -> {
            if (billing != null) {
                this.billing = billing;
            }
        });
    }

    private void fetchCart() {
        viewModel.fetchCart().observe(this, cartResponse -> {
            if (cartResponse != null && cartResponse.isStatus()) {
                this.cartResponse = cartResponse;
                viewModel.getCartResponseMutableLiveData().setValue(cartResponse);
            }
        });
    }


    private void updateCartValueTxt(int cartSize) {
        binding.toolbar.notificationText.setText(String.valueOf(cartSize));

    }

    private void subscribeForProgress() {
        viewModel.getProgressPercentage().observe(this, this::handleProgress);
    }

    private void handleProgress(Integer progress) {
        if (progress == 50) {
            replaceFragment(STEP_TWO);
        } else if (progress == 100) {
            replaceFragment(STEP_THREE);
        }
        animateProgress(progress);
    }

    private void enableButton(Integer progress) {
        switch (progress) {
            case 0:
                binding.one.setEnabled(true);
                binding.two.setEnabled(false);
                binding.three.setEnabled(false);
                break;

            case 50:
                binding.two.setEnabled(true);
                binding.three.setEnabled(false);
                break;

            case 100:
                binding.three.setEnabled(true);
                break;
        }
    }

    private void animateProgress(Integer progress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(binding.progressId,
                "progress", progress);
        animation.setDuration(600);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                enableButton(progress);
            }
        });
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
    }

    private void initializeConnectionCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                handleLocation(location);
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }

    public void handleLocation(Location location) {
        //showProgressDialog();
        AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        FetchAddressIntentService.enqueueWork(this, intent);
    }

    private void intializeLocationReq() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
    }

    private void initializeFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
    }

    private void subscribeGetCurrentLocation() {
        viewModel.getGetCurrentLocationUpdateLiveData().observe(this, hasToGetLocation -> {
            if (hasToGetLocation) {
                checkGps();
            }
        });
    }

    private void checkGps() {
        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            isGPS = isGPSEnable;
            if (isGPS) {
                getLastLocation();
            }
        });
    }

    private void getLastLocation() {
        if (!PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && !PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionUtils.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            accessLastLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void accessLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        handleLocation(location);
                    } else {
                        fusedLocationClient.requestLocationUpdates(locationRequest,
                                locationCallback, null);

                    }
                });
    }

    private void initToolBar() {
        binding.toolbar.backArrow.setVisibility(View.VISIBLE);
        binding.toolbar.filter.setVisibility(View.GONE);
        binding.toolbar.title.setText(getString(R.string.shipping_details));
        binding.toolbar.cartImageLyt.setVisibility(View.VISIBLE);
        binding.toolbar.backArrow.setOnClickListener(v -> onBackPressed());
        binding.toolbar.cartImageLyt.setOnClickListener(v -> {
            finish();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent();
                intent.putExtra(LAUNCH_CART, true);
                ObservableManager.getInstance().notifyData(intent);
            }, 100);

        });
    }

    private void setTitle(String title) {
        binding.toolbar.title.setText(title);
    }

    private void replaceFragment(int step) {
        Fragment fragment = null;
        String fragmentTag = null;
        switch (step) {
            case STEP_ONE:
                fragment = ShippingDetailFragment.createInstance();
                fragmentTag = Constants.FragmentTags.SHIPPING_DETAIL;
                break;

            case STEP_TWO:
                setTitle(getString(R.string.checkout));
                fragment = CheckoutFragment.createInstance();
                fragmentTag = Constants.FragmentTags.CHECKOUT;
                break;

            case STEP_THREE:
                setTitle(getString(R.string.order_complete));
                fragment = OrderCompleteFragment.createInstance();
                fragmentTag = Constants.FragmentTags.ORDER_COMPLETE;
                break;
        }
        FragmentHelper.replaceFragmentWithAnim(this, fragment, fragmentTag, true,
                binding.fragmentContainer.getId());
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 1 || backStackCount == 3) {
            finish();
        } else if (backStackCount == 2) {
            setTitle(getString(R.string.shipping_details));
            getSupportFragmentManager().popBackStackImmediate();
            animateProgress(0);
            binding.two.setEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessLastLocation();
            } else {
                ToastUtils.getInstance(this).showShortToast(getString(R.string.permission_denied));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
                getLastLocation();
            }
        }
    }

    public class AddressResultReceiver extends ResultReceiver {
        private AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) return;
            handleAddress(resultData);
        }
    }

    private void handleAddress(Bundle resultData) {
        hideProgressDialog();
        Address address = resultData.getParcelable(Constants.RESULT_DATA_KEY);
        if (address != null) {
            viewModel.getAddressMutableLiveData().setValue(address);
        } else {
            ToastUtils.getInstance(this).showShortToast(getString(R.string.no_address_found));
        }
    }


}
