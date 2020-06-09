package com.najdi.android.najdiapp.checkout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.CheckoutViewModel;
import com.najdi.android.najdiapp.checkout.viewmodel.ShippingDetailViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentCheckoutStep1Binding;
import com.najdi.android.najdiapp.home.model.User;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

public class ShippingDetailFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener {

    private static final int DEFAULT_ZOOM_LEVEL = 14;
    private FragmentCheckoutStep1Binding binding;
    private CheckoutViewModel activityViewModel;
    private ShippingDetailViewModel viewModel;
    private GoogleMap map;
    private CheckoutActivity activity;
    private boolean isDragging;
    private Marker draggedMarker;
    private int initCheck;

    public static ShippingDetailFragment createInstance() {
        return new ShippingDetailFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CheckoutActivity) {
            activity = (CheckoutActivity) context;
        }
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
        navMaptoUserFavPlace();
        fetchUserDetail();
        return binding.getRoot();
    }

    private void navMaptoUserFavPlace() {
        if (getActivity() == null) return;
        String latlng = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_LAT_LONG);
        if (!TextUtils.isEmpty(latlng)) {
            String lat = latlng.split(" ")[0];
            String lng = latlng.split(" ")[1];
            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            isDragging = false;
            getAddressFrmLatLng(latLng);
        }
    }

    private void fetchUserDetail() {
        if (getActivity() == null) return;
        String userId = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_ID_KEY);
        viewModel.getUserDetail(userId).observe(getViewLifecycleOwner(), baseResponse -> {
            if (baseResponse.isStatus()) {
                User user = baseResponse.getUser();
                viewModel.setUser(user);
            }
        });
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) return;
        mapFragment.getMapAsync(this);
    }

    private void initalizeActivityViewModel() {
        if (getActivity() == null) return;
        activityViewModel = new ViewModelProvider(getActivity()).get(CheckoutViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initMapSettings();
        initMapClickListener();
    }

    private void initMapClickListener() {
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                draggedMarker = marker;
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),
                        DEFAULT_ZOOM_LEVEL));
                getAddressFrmLatLng(marker.getPosition());
            }
        });
    }

    private void getAddressFrmLatLng(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        activity.handleLocation(location);
    }

    private void initMapSettings() {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        map.setOnMyLocationButtonClickListener(() -> {
            if (draggedMarker == null) return false;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(draggedMarker.getPosition(),
                    DEFAULT_ZOOM_LEVEL));
            return true;
        });
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
                this::navigateMapToCurrentAddress);
    }

    @SuppressLint("MissingPermission")
    private void navigateMapToCurrentAddress(Address address) {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (address != null) {
            if (!isDragging) {
                map.clear();
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //map.addMarker(markerOptions).setDraggable(true);
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL));
                map.setOnCameraIdleListener(this);
            }
            isDragging = false;
            viewModel.updateAddress(address);
        }
    }

    private void initClickListeners() {
        binding.saveFav.setOnClickListener(v -> {
            if (map.getCameraPosition() != null && map.getCameraPosition().target != null) {
                String lat = String.valueOf(map.getCameraPosition().target.latitude);
                String lng = String.valueOf(map.getCameraPosition().target.longitude);
                String latLng = lat.concat(" ").concat(lng);
                PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_LAT_LONG, latLng);
                DialogUtil.showAlertDialog(getActivity(), getString(R.string.location_saved_msg),
                        (d, w) -> d.dismiss());
            }
        });

        binding.locationBtn.setOnClickListener(v ->
                activityViewModel.getGetCurrentLocationUpdateLiveData().setValue(true));

        binding.continueTxt.setOnClickListener(v ->
                viewModel.validate().observe(getViewLifecycleOwner(), proceed -> {
                    if (proceed) {
                        saveDetails();
                        BillingAddress billingAddress = viewModel.getBillingObject();
                        activityViewModel.getBillingMutableLiveData().setValue(billingAddress);
                        activityViewModel.getProgressPercentage().setValue(50);
                    } else {
                        DialogUtil.showAlertDialogNegativeVector(getActivity(), getString(R.string.please_fill),
                                (dialog, which) -> dialog.dismiss());
                    }
                }));
    }

    private void saveDetails() {
        PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_NAME_KEY,
                viewModel.getName().getValue());
        PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY,
                viewModel.getEmail().getValue());
        PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_PHONE_NO_KEY,
                viewModel.getPhoneNo().getValue());
    }

    private void updateDetails() {
        if (getActivity() == null) return;
        String name = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_NAME_KEY);
        String phoneNo = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_PHONE_NO_KEY);
        String email = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY);
        viewModel.updatePersonalInfo(name, email, phoneNo);
    }

    @Override
    public void onCameraIdle() {
        if (++initCheck > 1) {
            LatLng latLng = map.getCameraPosition().target;
            isDragging = true;
            getAddressFrmLatLng(latLng);
        }
    }
}
