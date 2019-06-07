package com.najdi.android.najdiapp.repository;

import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;
import com.najdi.android.najdiapp.network.RetrofitCallBack;
import com.najdi.android.najdiapp.network.RetrofitClient;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;

public class Repository {

    private final ResourceProvider resourceProvider;
    private MutableLiveData<CartResponse> cartResponseLiveData;

    public Repository(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public LiveData<SignupResponseModel> registerUser(SignupRequestModel signupRequestModel) {
        MutableLiveData<SignupResponseModel> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().registerUser(signupRequestModel).enqueue(new RetrofitCallBack<>
                (new RetrofitCallBack.CustomCallBack<SignupResponseModel>() {
                    @Override
                    public void onSuccesResponse(Call<SignupResponseModel> call, SignupResponseModel signupResponseModel) {
                        liveData.setValue(signupResponseModel);
                    }

                    @Override
                    public void onFailurResponse(Call<SignupResponseModel> call, BaseResponse baseResponse) {
                        baseResponse.handleError(resourceProvider.getAppContext());
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }

    public LiveData<BaseResponse> loginUser(LoginRequestModel loginRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().loginUser(loginRequestModel).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                liveData.setValue(baseResponse);
            }

            @Override
            public void onFailurResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<List<ProductListResponse>> getProducts() {
        MutableLiveData<List<ProductListResponse>> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getProducts().enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<List<ProductListResponse>>() {
            @Override
            public void onSuccesResponse(Call<List<ProductListResponse>> call, List<ProductListResponse> productListResponses) {
                liveData.setValue(productListResponses);
            }

            @Override
            public void onFailurResponse(Call<List<ProductListResponse>> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }


    public LiveData<BaseResponse> addToCart(CartRequest cartRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().addToCart(cartRequest).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                liveData.setValue(baseResponse);
            }

            @Override
            public void onFailurResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    baseResponse.handleError(resourceProvider.getAppContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<CartResponse> getCart() {
        MutableLiveData<CartResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getCart().enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<CartResponse>() {
            @Override
            public void onSuccesResponse(Call<CartResponse> call, CartResponse cartResponse) {
                liveData.setValue(cartResponse);
            }

            @Override
            public void onFailurResponse(Call<CartResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<BaseResponse> removeCartItem(HashMap<String, String> cartItem) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().removeCartItem(cartItem).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.
                CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                liveData.setValue(baseResponse);
            }

            @Override
            public void onFailurResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<ProductListResponse> getIndividualProduct(int productId) {
        MutableLiveData<ProductListResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getIndividualProduct(productId).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<ProductListResponse>() {
            @Override
            public void onSuccesResponse(Call<ProductListResponse> call, ProductListResponse productListResponse) {
                liveData.setValue(productListResponse);
            }

            @Override
            public void onFailurResponse(Call<ProductListResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;

    }
}
