package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.ContactUsBinding;
import com.najdi.android.najdiapp.home.viewmodel.ContactUsViewModel;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.utitility.DialogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

public class ContactUsFragment extends BaseFragment implements TextWatcher {

    private ContactUsBinding binding;
    private ContactUsViewModel viewModel;

    public static ContactUsFragment createInstance() {
        return new ContactUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.contact_us, container, false);
        initiazeActivityViewModel();
        initializeViewModel();
        bindViewModel();
        initClickListner();
        return binding.getRoot();
    }

    private void initiazeActivityViewModel() {
        if (getActivity() == null) return;
        HomeScreenViewModel activityViewModel = new ViewModelProvider(getActivity())
                .get(HomeScreenViewModel.class);
        activityViewModel.getSetToolBarTitle().setValue(getString(R.string.contact_us));
    }


    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
    }


    private void initClickListner() {
        binding.message.addTextChangedListener(this);

        binding.send.setOnClickListener(v -> {

            if (getActivity() == null) return;

            String userId = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_ID_KEY);
            String phoneNo = PreferenceUtils.getValueString(getActivity(), PreferenceUtils.USER_PHONE_NO_KEY);
            LiveData<BaseResponse> liveData = viewModel.contactUs(phoneNo, userId);
            if (liveData != null) {
                showProgressDialog();
                liveData.observe(getViewLifecycleOwner(), baseResponse -> {
                    hideProgressDialog();
                    if (baseResponse != null && baseResponse.isStatus()) {
                        viewModel.getMessage().setValue("");
                        DialogUtil.showAlertDialog(getActivity(),
                                getString(R.string.message_success), (dialog, which) ->
                                        dialog.dismiss());
                    }
                });
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            binding.send.setEnabled(true);
        } else {
            binding.send.setEnabled(false);
        }

    }
}
