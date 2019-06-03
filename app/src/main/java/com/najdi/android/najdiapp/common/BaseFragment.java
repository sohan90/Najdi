package com.najdi.android.najdiapp.common;

import com.najdi.android.najdiapp.utitility.DialogUtil;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected void showProgressDialog(){
        DialogUtil.showProgressDialog(getActivity(), false);
    }

    protected void hideProgressDialog(){
        DialogUtil.hideProgressDialog();
    }
}
