package com.najdi.android.najdiapp.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.databinding.FragmentProfileBinding;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProfileViewModel;
import com.najdi.android.najdiapp.launch.view.ChangePasswordActivity;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

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
        observeNameField();
        observeEmailField();
        return binding.getRoot();
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initActivityViewModel() {
        if (getActivity() == null) return;
        homeScreenViewModel = new ViewModelProvider(getActivity()).get(HomeScreenViewModel.class);
        homeScreenViewModel.getSetToolBarTitle().setValue(getString(R.string.profile));
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    private void observeEmailField() {
        viewModel.email.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY,
                        viewModel.email.getValue());
            }
        });
    }

    private void observeNameField() {
        viewModel.name.observe(getViewLifecycleOwner(), s -> {
            if (!TextUtils.isEmpty(s)) {
                PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_NAME_KEY,
                        viewModel.name.getValue());
            }
        });
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
        if (getActivity() == null) return;
        if (viewModel.validate()) {
            showProgressDialog();
            String userId = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_ID_KEY);
            viewModel.updateProfile(userId).observe(getViewLifecycleOwner(), baseResponse -> {
                hideProgressDialog();
                if (baseResponse != null && baseResponse.isStatus()) {
                    saveUserDetails();
                    DialogUtil.showAlertDialog(getActivity(), getString(R.string.profile_succes_msg),
                            (dialog, which) -> dialog.dismiss());
                } else {
                    DialogUtil.showAlertDialogNegativeVector(getActivity(), getString(R.string.something_went_wrong),
                            (dialog, which) -> dialog.dismiss());
                }
            });
        } else {
            DialogUtil.showAlertDialogNegativeVector(getActivity(), getString(R.string.please_fill),
                    (dialog, which) -> dialog.dismiss());
        }

    }

    private void saveUserDetails() {
        PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_NAME_KEY,
                viewModel.name.getValue());
        PreferenceUtils.setValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY,
                viewModel.email.getValue());
        homeScreenViewModel.getName().setValue(viewModel.name.getValue());
    }

    private void setData() {
        if (getActivity() == null) return;
        String name = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_NAME_KEY);
        String email = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_EMAIL_KEY);
        viewModel.setName(name);
        viewModel.setEmail(email);
        if (TextUtils.isEmpty(name)) return;
        // binding.name.setSelection(name.length());

    }

}
