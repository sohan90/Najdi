package com.najdi.android.najdiapp.repository;

import com.najdi.android.najdiapp.checkout.model.OrderRequest;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.home.model.HtmlResponseForNajdi;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;
import com.najdi.android.najdiapp.launch.model.OtpRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;
import com.najdi.android.najdiapp.launch.view.LoginActivity;
import com.najdi.android.najdiapp.network.RetrofitCallBack;
import com.najdi.android.najdiapp.network.RetrofitClient;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
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

    public LiveData<BaseResponse> registerUser(SignupRequestModel signupRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().registerUser(signupRequestModel).enqueue(new RetrofitCallBack<>
                (new RetrofitCallBack.CustomCallBack<BaseResponse>() {
                    @Override
                    public void onSuccesResponse(Call<BaseResponse> call, BaseResponse signupResponseModel) {
                        if (signupResponseModel != null) {
                            liveData.setValue(signupResponseModel);
                        }
                    }

                    @Override
                    public void onFailurResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                        if (baseResponse == null) return;
                        baseResponse.handleError(resourceProvider.getAppContext());
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }

    public LiveData<BaseResponse> loginUser(LoginRequestModel loginRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().loginUser(resourceProvider.getCountryLang(),
                loginRequestModel).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
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
        RetrofitClient.getInstance().getProducts(resourceProvider.getCountryLang()).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<List<ProductListResponse>>() {
            @Override
            public void onSuccesResponse(Call<List<ProductListResponse>> call, List<ProductListResponse> productListResponses) {
                liveData.setValue(productListResponses);
            }

            @Override
            public void onFailurResponse(Call<List<ProductListResponse>> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    baseResponse.handleError(resourceProvider.getAppContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }


    public LiveData<BaseResponse> addToCart(CartRequest cartRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getAppContext(), PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().addToCart(resourceProvider.getCountryLang(), Constants.BEARER + token, cartRequest).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
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
        String token = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        String userId = String.valueOf(PreferenceUtils.getValueInt(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        RetrofitClient.getInstance().getCart(userId,
                resourceProvider.getCountryLang()).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<CartResponse>() {
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
        String token = PreferenceUtils.getValueString(resourceProvider.getAppContext(), PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().removeCartItem(Constants.BEARER + token, cartItem).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.
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
        RetrofitClient.getInstance().getIndividualProduct(productId, resourceProvider.getCountryLang()).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<ProductListResponse>() {
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

    public LiveData<OrderResponse> createOrder(int userId, OrderRequest orderRequest) {
        MutableLiveData<OrderResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().createOrder(userId, orderRequest).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<OrderResponse>() {
            @Override
            public void onSuccesResponse(Call<OrderResponse> call, OrderResponse orderResponse) {
                if (orderResponse != null) {
                    liveData.setValue(orderResponse);
                }
            }

            @Override
            public void onFailurResponse(Call<OrderResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<List<OrderResponse>> getOrderStatus(int userId) {
        MutableLiveData<List<OrderResponse>> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getOrderStatus(resourceProvider.getCountryLang(), userId).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<List<OrderResponse>>() {
            @Override
            public void onSuccesResponse(Call<List<OrderResponse>> call, List<OrderResponse> orderResponses) {
                if (orderResponses != null) {
                    liveData.setValue(orderResponses);
                }
            }

            @Override
            public void onFailurResponse(Call<List<OrderResponse>> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }


    public LiveData<BaseResponse> updateItemQuantity(UpdateCartRequest updateCartRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        String userId = String.valueOf(PreferenceUtils.getValueInt(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        RetrofitClient.getInstance().updateItemQuantity(Constants.BEARER + token,
                resourceProvider.getCountryLang(),
                userId, updateCartRequest).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> loginToken(LoginRequestModel loginRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().loginToken(loginRequestModel).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> verifyOtp(OtpRequestModel otpRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().verifyOtp(otpRequestModel).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> clearCart() {
        String loginToken = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);

        String userId = String.valueOf(PreferenceUtils.getValueInt(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().clearCart(resourceProvider.getCountryLang(),
                userId).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.
                CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> resendOtp(OtpRequestModel otpRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().resendOtp(otpRequestModel).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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


    public LiveData<BaseResponse> getBankDetails() {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getBankDetails(resourceProvider.getCountryLang()).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> contactUs(ContactUsRequest contactUsRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().contactUs(resourceProvider.getCountryLang(), contactUsRequest).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> getCartCount() {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String userId = String.valueOf(PreferenceUtils.getValueInt(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        String token = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);

        RetrofitClient.getInstance().getCartCount(resourceProvider.getCountryLang(), userId).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<HtmlResponseForNajdi> getHtmlConent(int pageId) {
        MutableLiveData<HtmlResponseForNajdi> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getHtmlTermsAboutUsPrivacyPolicy(pageId).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<HtmlResponseForNajdi>() {
            @Override
            public void onSuccesResponse(Call<HtmlResponseForNajdi> call, HtmlResponseForNajdi htmlResponseForNajdi) {
                if (htmlResponseForNajdi != null) {
                    liveData.setValue(htmlResponseForNajdi);
                }
            }

            @Override
            public void onFailurResponse(Call<HtmlResponseForNajdi> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    baseResponse.handleError(resourceProvider.getAppContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<BaseResponse> forgotPasswordRequest(ForgotPaswwordRequest forgotPaswwordRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().forgotPassword(forgotPaswwordRequest).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> forgotUpdate(ForgotPaswwordRequest forgotPaswwordRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().forgotUpdate(forgotPaswwordRequest).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> changePassword(ForgotPaswwordRequest forgotPaswwordRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().changePasswormd(forgotPaswwordRequest).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> verifyForgotOtp(OtpRequestModel otpRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().forgotVerify(otpRequestModel).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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

    public LiveData<BaseResponse> mobileChange(ForgotPaswwordRequest forgotPaswwordRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().mobileChange(forgotPaswwordRequest).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
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
}
