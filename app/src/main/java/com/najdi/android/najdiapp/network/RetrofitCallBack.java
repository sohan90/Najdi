package com.najdi.android.najdiapp.network;

import com.google.gson.Gson;
import com.najdi.android.najdiapp.common.BaseResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallBack<T> implements Callback<T> {

    private final CustomCallBack<T> mCallback;

    public RetrofitCallBack(CustomCallBack<T> customCallBack) {
        this.mCallback = customCallBack;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            mCallback.onSuccesResponse(call, response.body());
        } else {
            try {
                if (response.errorBody() != null) {
                    String errorJson = response.errorBody().string();
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(errorJson, BaseResponse.class);
                    mCallback.onFailurResponse(call, baseResponse);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mCallback.onFailurResponse(call, new BaseResponse());
    }

    public interface CustomCallBack<T> {
        void onSuccesResponse(Call<T> call, T t);

        void onFailurResponse(Call<T> call, BaseResponse baseResponse);
    }
}
