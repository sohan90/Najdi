package com.najdi.android.najdiapp.repository;

import com.najdi.android.najdiapp.ErrorResponse;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;
import com.najdi.android.najdiapp.network.RetrofitCallBack;
import com.najdi.android.najdiapp.network.RetrofitClient;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;

public class ApiRequest {

    public LiveData<SignupResponseModel> registerUser(ResourceProvider resourceProvider, SignupRequestModel signupRequestModel) {
        MutableLiveData<SignupResponseModel> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().registerUser(signupRequestModel).enqueue(new RetrofitCallBack<>
                (new RetrofitCallBack.CustomCallBack<SignupResponseModel>() {
                    @Override
                    public void onSuccesResponse(Call<SignupResponseModel> call, SignupResponseModel signupResponseModel) {
                        liveData.setValue(signupResponseModel);
                    }

                    @Override
                    public void onFailurResponse(Call<SignupResponseModel> call, ErrorResponse errorResponse) {
                        errorResponse.handleError(resourceProvider.getAppContext());
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }

}
