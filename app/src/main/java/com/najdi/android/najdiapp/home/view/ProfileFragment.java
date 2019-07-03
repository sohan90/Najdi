package com.najdi.android.najdiapp.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.FragmentProfileBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProfileViewModel;
import com.najdi.android.najdiapp.launch.view.ChangePasswordActivity;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import static com.najdi.android.najdiapp.launch.view.ChangePasswordActivity.EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE;

public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private HomeScreenViewModel homeScreenViewModel;

    public static ProfileFragment createInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container,
                false);

        initActivityViewModel();
        initViewModel();
        bindViewModel();
        setData();
        initClickListener();
        return binding.getRoot();
    }

    private void initClickListener() {
        binding.save.setOnClickListener(v -> updateProfile());
        binding.changeMobileNo.setOnClickListener(v -> launchMobileNoActivity());
        binding.changePassword.setOnClickListener(v -> launchChangePasswordActivity());
    }

    private void launchChangePasswordActivity() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        intent.putExtra(EXTRA_CHANGE_PASSWORD_LAUNCH_TYPE, Constants.OtpScreen.CHANGE_PASSWORD_PROFILE);
        startActivity(intent);
    }

    private void launchMobileNoActivity() {
        Intent intent = new Intent(getActivity(), ChangeMobileNoActivity.class);
        startActivity(intent);
    }

    private void updateProfile() {
        if (binding.name.getText() != null && binding.email.getText() != null) {
            PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_NAME_KEY,
                    binding.name.getText().toString());
            PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY,
                    binding.email.getText().toString());
        } else {
            ToastUtils.getInstance(getActivity()).showShortToast(getString(R.string.please_fill));
        }

    }

    private void setData() {
        if (getActivity() == null) return;
        String name = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_NAME_KEY);
        String email = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY);
        binding.name.setText(name);
        binding.email.setText(email);
        binding.name.setSelection(name.length());

    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initActivityViewModel() {
        if (getActivity() == null) return;
        homeScreenViewModel = ViewModelProviders.of(getActivity()).get(HomeScreenViewModel.class);
        homeScreenViewModel.getSetToolBarTitle().setValue(getString(R.string.profile));
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }
}
