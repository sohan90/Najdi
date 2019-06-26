package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragAboutUsBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class AboutUsFragment extends BaseFragment {

    FragAboutUsBinding binding;
    private HomeScreenViewModel viewModel;

    public static AboutUsFragment createInstance() {
        return new AboutUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_about_us, container, false);
        initActivityViewModel();
        initToolBar();
        return binding.getRoot();
    }

    private void initActivityViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
    }

    private void initToolBar() {
        viewModel.getSetToolBarTitle().setValue(getString(R.string.about_us));
    }
}
