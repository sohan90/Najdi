package com.najdi.android.najdiapp.checkout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.viewmodel.BankDetailViewModel;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.FragmentBankDetailBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        acitivityViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this).get(BankDetailViewModel.class);
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
        viewModel.getBankDetail().observe(this, baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null) {
                if (baseResponse.getData() != null && baseResponse.getData().getData() != null) {
                    List<BaseResponse.BankResponse> bankResponseList =
                            baseResponse.getData().getData();
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
