package com.najdi.android.najdiapp.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseFragment;
import com.najdi.android.najdiapp.databinding.FragAboutUsBinding;
import com.najdi.android.najdiapp.home.viewmodel.AboutViewModel;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.launch.viewmodel.SignUpViewModel;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.Objects;

import static com.najdi.android.najdiapp.common.Constants.HtmlScreen.ABOUT_US;
import static com.najdi.android.najdiapp.common.Constants.HtmlScreen.TERMS_CONDITION;

/**
 * Class is the entry point for About us, Terms and condition and privacy policy.
 */
public class AboutUsFragment extends BaseFragment {

    private FragAboutUsBinding binding;
    private AboutViewModel viewModel;
    private HomeScreenViewModel activityViewModel;
    private static final String EXTRA_SCREEN_TYPE = "screen_type";
    private int screenType;
    private SignUpViewModel signUpViewModel;

    public static AboutUsFragment createInstance(int screenType) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SCREEN_TYPE, screenType);
        AboutUsFragment fragment = new AboutUsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_about_us, container, false);
        setLocaleLanguage(PreferenceUtils.getLangFromPref(Objects.requireNonNull(getActivity())));
        getScreenType();
        initActivityViewModel();
        initViewModel();
        bindViewModel();
        fetchHtmlContentForScreenType();
        initToolBar();
        return binding.getRoot();
    }

    private void initViewModel() {
        if (getActivity() == null) return;
        viewModel = new ViewModelProvider(this).get(AboutViewModel.class);
    }

    private void initActivityViewModel() {
        if (getActivity() == null) return;

        if (screenType == ABOUT_US) {
            activityViewModel = new ViewModelProvider(getActivity()).get(HomeScreenViewModel.class);
        } else {
            signUpViewModel = new ViewModelProvider(getActivity()).get(SignUpViewModel.class);
        }
    }

    private void initToolBar() {
        if (screenType == ABOUT_US) {
            activityViewModel.getSetToolBarTitle().setValue(getString(R.string.about_us));
        } else {
            if (screenType == TERMS_CONDITION) {
                signUpViewModel.getToolbarTitle().setValue(getString(R.string.terms_and_conditions));
            } else {
                signUpViewModel.getToolbarTitle().setValue(getString(R.string.and_privacy_policy));
            }
        }
    }

    private void fetchHtmlContentForScreenType() {
        if (screenType == ABOUT_US) {
            fetchAboutUsPage();
        } else if (screenType == TERMS_CONDITION) {
            fetchTermsCondition();
        } else {
            fetchPrivacyPolicy();
        }
    }

    private void getScreenType() {
        if (getArguments() == null) return;
        screenType = getArguments().getInt(EXTRA_SCREEN_TYPE);
    }

    private void bindViewModel() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void fetchAboutUsPage() {
        showProgressDialog();
        viewModel.getAboutUs().observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                String htmlcontent = baseResponse.getMessage();
                String imgageTrimmedHtml = htmlcontent.replaceAll("<img.+?>", "");
                binding.webview.loadDataWithBaseURL("", getRtlSupportedHtml(imgageTrimmedHtml),
                        "text/html", "UTF-8", "");
            }
        });
    }

    private void fetchTermsCondition() {
        showProgressDialog();
        viewModel.termsCondition().observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                String htmlcontent = baseResponse.getMessage();
                binding.webview.loadDataWithBaseURL("", getRtlSupportedHtml(htmlcontent),
                        "text/html", "UTF-8", "");
            }
        });
    }

    private void fetchPrivacyPolicy() {
        showProgressDialog();
        viewModel.privacyPolicy().observe(getViewLifecycleOwner(), baseResponse -> {
            hideProgressDialog();
            if (baseResponse != null && baseResponse.isStatus()) {
                String htmlcontent = baseResponse.getMessage();
                binding.webview.loadDataWithBaseURL("", getRtlSupportedHtml(htmlcontent),
                        "text/html", "UTF-8", "");
            }
        });
    }

}
