package com.najdi.android.najdiapp.checkout.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.FetchAddressIntentService;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.common.BaseActivity;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.ActivityCheckoutBinding;
import com.najdi.android.najdiapp.utitility.FragmentHelper;
import com.najdi.android.najdiapp.utitility.GpsUtils;
import com.najdi.android.najdiapp.utitility.LogUtil;
import com.najdi.android.najdiapp.utitility.PermissionUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
        replaceFragment(STEP_ONE);
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
    }

    private void replaceFragment(int step) {
        Fragment fragment = null;
        String fragmentTag = null;
        switch (step) {
            case STEP_ONE:
                fragment = ShippingDetailFragment.createInstance();
                fragmentTag = "Step_one_frag";
                break;
        }
        FragmentHelper.replaceFragment(this, fragment, fragmentTag, true,
                binding.fragmentContainer.getId());
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 1) {
            finish();
        } else {
            super.onBackPressed();
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
