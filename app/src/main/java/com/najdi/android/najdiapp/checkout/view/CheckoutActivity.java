package com.najdi.android.najdiapp.checkout.view;

import android.Manifest;
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
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityCheckoutBinding;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.GpsUtils;
import com.najdi.android.najdiapp.utitility.PermissionUtils;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

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
        replaceFragment(STEP_ONE);
        fetchCart();
    }

    private void subscribeForCheckout() {
        viewModel.getCheckoutLiveData().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog();
                int userId = PreferenceUtils.getValueInt(this, PreferenceUtils.USER_ID_KEY);
                createOrder(userId);
            }
        });
    }

    private void createOrder(int userId) {
        LiveData<OrderResponse> orderResponseLiveData =
                viewModel.createOrder(userId, cartResponse.getData().getCartdata(), billing);

        orderResponseLiveData.observe(this, orderResponse -> {
            hideProgressDialog();
            viewModel.orderResponseMutableLiveData().setValue(orderResponse);
            handleProgress(100);
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
            if (cartResponse != null) {
                this.cartResponse = cartResponse;
                updateCartValueTxt();
                viewModel.getCartResponseMutableLiveData().setValue(cartResponse);
            }
        });
    }

    private void updateCartValueTxt() {
        if (cartResponse != null && cartResponse.getData() != null &&
                cartResponse.getData().getCartdata() != null) {

            int size = cartResponse.getData().getCartdata().size();
            binding.toolbar.notificationText.setText(String.valueOf(size));
        }
    }

    private void subscribeForProgress() {
        viewModel.getProgressPercentage().observe(this, this::handleProgress);
    }

    private void handleProgress(Integer progress) {
        animateProgress(progress);
        switch (progress) {
            case 0:
                binding.one.setEnabled(true);
                binding.two.setEnabled(false);
                binding.three.setEnabled(false);
                break;

            case 50:
                binding.two.setEnabled(true);
                binding.three.setEnabled(false);
                replaceFragment(STEP_TWO);
                break;

            case 100:
                binding.three.setEnabled(true);
                replaceFragment(STEP_THREE);
                break;
        }
    }

    private void animateProgress(Integer progress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(binding.progressId,
                "progress", progress);
        animation.setDuration(600);
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

    private void handleLocation(Location location) {
        showProgressDialog();
        AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
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
        viewModel = ViewModelProviders.of(this).get(CheckoutViewModel.class);
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
                        ToastUtils.getInstance(CheckoutActivity.this).showShortToast("" + location.getLongitude());
                    } else {
                        fusedLocationClient.requestLocationUpdates(locationRequest,
                                locationCallback, null);

                    }
                });
    }

    private void initToolBar() {
        binding.toolbar.backArrow.setVisibility(View.VISIBLE);
        binding.toolbar.title.setText(getString(R.string.shipping_details));
        binding.toolbar.cartImageLyt.setVisibility(View.VISIBLE);
        binding.toolbar.backArrow.setOnClickListener(v -> onBackPressed());
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
                fragment = CheckoutFragment.createInstance();
                fragmentTag = Constants.FragmentTags.CHECKOUT;
                break;

            case STEP_THREE:
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
            getSupportFragmentManager().popBackStackImmediate();
            animateProgress(0);
            binding.two.setEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessLastLocation();
                } else {
                    ToastUtils.getInstance(this).showShortToast(getString(R.string.permission_denied));
                }
                break;
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
            if (resultData == null) {
                return;
            }
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
