package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentOrderStatusBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderStatusFragment extends BaseFragment {
    FragmentOrderStatusBinding binding;
    private HomeScreenViewModel activityViewModel;
    private OrderStatusAdapter adapter;


    public static OrderStatusFragment createInstance() {
        return new OrderStatusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_status,
                container, false);
        initializeActivityViewModel();
        initialzeRecyclerView();
        subscribeForOrderResponse();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        activityViewModel.getSetToolBarTitle().setValue(getString(R.string.order_status));
    }

    private void subscribeForOrderResponse() {
        if (getActivity() == null) return;
        showProgressDialog();
        int userId = PreferenceUtils.getValueInt(getActivity(), PreferenceUtils.USER_ID_KEY);
        activityViewModel.getOrderStatus(userId).observe(this, orderResponses -> {
            hideProgressDialog();
            adapter.setData(orderResponses);
        });
    }


    private void initializeActivityViewModel() {
        if (getActivity() == null) return;
        activityViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
    }

    private void initialzeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        binding.recyclView.setLayoutManager(linearLayoutManager);

        adapter = new OrderStatusAdapter(new ArrayList<>());
        binding.recyclView.setAdapter(adapter);
    }
}
