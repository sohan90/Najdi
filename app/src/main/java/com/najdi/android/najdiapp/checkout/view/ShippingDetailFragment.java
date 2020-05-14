package com.najdi.android.najdiapp.checkout.view;

import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.checkout.viewmodel.ShippingDetailViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutStep1Binding;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

public class ShippingDetailFragment extends BaseFragment implements OnMapReadyCallback {
    private FragmentCheckoutStep1Binding binding;
    private CheckoutViewModel activityViewModel;
    private ShippingDetailViewModel viewModel;
    private GoogleMap map;

    public static ShippingDetailFragment createInstance() {
        return new ShippingDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_step_1, container,
                false);
        initializeMap();
        initalizeActivityViewModel();
        initializeViewModel();
        bindViewModel();
        initClickListeners();
        updateDetails();
        subscribeForAddress();
        return binding.getRoot();
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) return;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initMapSettings();
    }

    private void initMapSettings() {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(ShippingDetailViewModel.class);
    }

    private void subscribeForAddress() {
        activityViewModel.getAddressMutableLiveData().observe(getViewLifecycleOwner(),
                address -> {
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    navigateMapToCurrentAddress(address);
                });
    }

    private void navigateMapToCurrentAddress(Address address) {
        if (address != null) {
            map.clear();
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            viewModel.updateAddress(address);
        }
    }

    private void initalizeActivityViewModel() {
        if (getActivity() == null) return;
        activityViewModel = new ViewModelProvider(getActivity()).get(CheckoutViewModel.class);
    }

    private void initClickListeners() {
        binding.locationBtn.setOnClickListener(v ->
                activityViewModel.getGetCurrentLocationUpdateLiveData().setValue(true));

        binding.continueTxt.setOnClickListener(v ->
                viewModel.validate().observe(getViewLifecycleOwner(), proceed -> {
                    if (proceed) {
                        BillingAddress billingAddress = viewModel.getBillingObject();
                        activityViewModel.getBillingMutableLiveData().setValue(billingAddress);
                        activityViewModel.getProgressPercentage().setValue(50);
                    } else {
                        DialogUtil.showAlertDialog(getActivity(), getString(R.string.please_fill),
                                (dialog, which) -> dialog.dismiss());
                    }
                }));
    }

    private void updateDetails() {
        if (getActivity() == null) return;
        String name = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_NAME_KEY);
        String phoneNo = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_PHONE_NO_KEY);
        String email = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY);
        viewModel.updatePersonalInfo(name, email, phoneNo);
    }
}
