package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragmentProductBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListFragment extends BaseFragment {
    private FragmentProductBinding binding;
    private HomeScreenViewModel homeScreeViewModel;
    private ProductListAdapter adapter;

    public static ProductListFragment createInstance() {
        return new ProductListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product,
                container, false);
        initUi();
        initialHomeScreenViewModel();
        subscribeForProductList();
        initializeRecyclerViewAdapter();
        return binding.getRoot();
    }

    private void initialHomeScreenViewModel() {
        if (getActivity() != null) {
            homeScreeViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
        }
    }

    private void subscribeForProductList() {
        homeScreeViewModel.getProductList().observe(this, productListResponses -> {
            adapter.setData(productListResponses);
        });
    }

    private void initializeRecyclerViewAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        binding.recyclView.setLayoutManager(linearLayoutManager);
        adapter = new ProductListAdapter(homeScreeViewModel, new ArrayList<>());
        binding.recyclView.setAdapter(adapter);
    }

    private void initUi() {

    }
}
