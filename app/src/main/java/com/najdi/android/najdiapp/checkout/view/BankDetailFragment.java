package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.BankDetailViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.FragmentBankDetailBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;

import java.util.ArrayList;
import java.util.List;

public class BankDetailFragment extends BaseFragment {

    private FragmentBankDetailBinding binding;
    private BankDetailAdapter adapter;
    private BankDetailViewModel viewModel;
    private HomeScreenViewModel acitivityViewModel;

    public static BankDetailFragment createInstance() {
        return new BankDetailFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bank_detail,
                container, false);

        initRecyclerView();
        intializeActivityViewModel();
        initializeViewModel();
        initToolBar();
        fetchBankDetails();
        return binding.getRoot();
    }

    private void initToolBar() {
        acitivityViewModel.getSetToolBarTitle().setValue(getString(R.string.our_bank_accounts_drawer));
    }

    private void intializeActivityViewModel() {
        if (getActivity() == null) return;
        acitivityViewModel = new ViewModelProvider(getActivity()).get(HomeScreenViewModel.class);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(BankDetailViewModel.class);
    }

    private void initRecyclerView() {
        adapter = new BankDetailAdapter(new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,
                false);
        binding.recyclView.setLayoutManager(linearLayoutManager);
        binding.recyclView.setAdapter(adapter);
    }

    private void fetchBankDetails() {
        showProgressDialog();
        viewModel.getBankDetail().observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                if (baseResponse.getData() != null && baseResponse.getData().size() > 0) {
                    List<BaseResponse.BankResponse> bankResponseList = baseResponse.getData();
                    adapter.setData(bankResponseList);
                }
            }
        });
    }

    @BindingAdapter("setBankImageUrl")
    public static void setBankImageUrl(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
