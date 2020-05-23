package com.najdi.android.najdiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.home.model.ProductId;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.model.ProductModelResponse;
import com.najdi.android.najdiapp.home.model.UpdateProfileModelRequest;
import com.najdi.android.najdiapp.home.model.UserId;
import com.najdi.android.najdiapp.launch.model.BillingAddress;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;
import com.najdi.android.najdiapp.launch.model.OtpRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.network.RetrofitCallBack;
import com.najdi.android.najdiapp.network.RetrofitClient;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;
import com.najdi.android.najdiapp.utitility.ResourceProvider;

import java.util.HashMap;

import retrofit2.Call;

public class Repository {

    private final ResourceProvider resourceProvider;

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

    public LiveData<ProductModelResponse> getProducts() {
        MutableLiveData<ProductModelResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getProducts(resourceProvider.getCountryLang()).enqueue(
                new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<ProductModelResponse>() {
                    @Override
                    public void onSuccesResponse(Call<ProductModelResponse> call,
                                                 ProductModelResponse productListResponses) {
                        liveData.setValue(productListResponses);
                    }

                    @Override
                    public void onFailurResponse(Call<ProductModelResponse> call, BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            baseResponse.handleError(resourceProvider.getAppContext());
                        }
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }


    public LiveData<ProductModelResponse> getCityBasedProducts(String cityId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("city_id", cityId);
        map.put("lang", resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        MutableLiveData<ProductModelResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getCityBasedProducts(token, map).enqueue(
                new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<ProductModelResponse>() {
                    @Override
                    public void onSuccesResponse(Call<ProductModelResponse> call,
                                                 ProductModelResponse productListResponses) {
                        liveData.setValue(productListResponses);
                    }

                    @Override
                    public void onFailurResponse(Call<ProductModelResponse> call, BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            baseResponse.handleError(resourceProvider.getAppContext());
                        }
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }

    public LiveData<ProductModelResponse> getCategoryBasedProducts(String lang, String catId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cat_id", catId);
        map.put("lang", lang);
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        MutableLiveData<ProductModelResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getCategoryBasedProducts(token, map).enqueue(
                new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<ProductModelResponse>() {
                    @Override
                    public void onSuccesResponse(Call<ProductModelResponse> call,
                                                 ProductModelResponse productListResponses) {
                        liveData.setValue(productListResponses);
                    }

                    @Override
                    public void onFailurResponse(Call<ProductModelResponse> call, BaseResponse baseResponse) {
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
        String userId = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY);
        cartRequest.setUserId(userId);
        cartRequest.setLang(resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().addToCart(token, cartRequest)
                .enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
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
        String userId = String.valueOf(PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY));
        UserId userId1 = new UserId();
        userId1.setUserId(userId);
        userId1.setLang(resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().getCart(token, userId1)
                .enqueue(new RetrofitCallBack<>(new RetrofitCallBack.
                        CustomCallBack<CartResponse>() {
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
        cartItem.put("lang", resourceProvider.getCountryLang());
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().removeCartItem(token, cartItem)
                .enqueue(new RetrofitCallBack<>(new RetrofitCallBack.
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

    public LiveData<BaseResponse> getIndividualProduct(String id) {
        ProductId productId = new ProductId();
        productId.setId(id);
        productId.setLang(resourceProvider.getCountryLang());
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getIndividualProduct(id, resourceProvider.getCountryLang())
                .enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
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

    public LiveData<OrderResponse> createOrder(String userId, BillingAddress billingAddress) {
        MutableLiveData<OrderResponse> liveData = new MutableLiveData<>();
        billingAddress.setUserId(userId);
        billingAddress.setLang(resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().createOrder(token, billingAddress)
                .enqueue(new RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<OrderResponse>() {
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

    public LiveData<BaseResponse> getOrderStatus(String userId) {
        UserId userId1 = new UserId();
        userId1.setUserId(userId);
        userId1.setLang(resourceProvider.getCountryLang());
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().getOrderStatus(token, userId1).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse orderResponses) {
                if (orderResponses != null) {
                    liveData.setValue(orderResponses);
                }
            }

            @Override
            public void onFailurResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }


    public LiveData<BaseResponse> updateItemQuantity(UpdateCartRequest updateCartRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String userId = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY);
        updateCartRequest.setUserId(userId);
        updateCartRequest.setLang(resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().updateItemQuantity(token, updateCartRequest).enqueue(new
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
               /* if (baseResponse != null){
                    baseResponse.handleErrorForDialog(resourceProvider.getActivityContext());
                }*/
                liveData.setValue(baseResponse);
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
                    baseResponse.handleErrorForDialog(resourceProvider.getActivityContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<BaseResponse> clearCart() {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String userId = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY);
        UserId userId1 = new UserId();
        userId1.setUserId(userId);
        userId1.setLang(resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().clearCart(token, userId1).enqueue(new RetrofitCallBack<>(new RetrofitCallBack.
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

    public LiveData<BaseResponse> resendOtpForChangeMobileNo(OtpRequestModel otpRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().resendOtpForChangeMobileNo(token, otpRequestModel).enqueue(new
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

    public LiveData<BaseResponse> forgotresendOtp(OtpRequestModel otpRequestModel) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().forgotResendOtp(token, otpRequestModel).enqueue(new
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
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().contactUs(token, contactUsRequest).enqueue(new
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
        String userId = PreferenceUtils.getValueString(resourceProvider.getAppContext(),
                PreferenceUtils.USER_ID_KEY);
        UserId userId1 = new UserId();
        userId1.setUserId(userId);
        userId1.setLang(resourceProvider.getCountryLang());
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().getCartCount(token, userId1).enqueue(new
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

    public LiveData<BaseResponse> getAboutUs() {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getAboutUs(resourceProvider.getCountryLang()).enqueue(new
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

    public LiveData<BaseResponse> getTermsCondition() {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getTermsCondition(resourceProvider.getCountryLang()).enqueue(new
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

    public LiveData<BaseResponse> getPrivacyPolicy() {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getPrivacyPolicy(resourceProvider.getCountryLang()).enqueue(new
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
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().changePassword(token, forgotPaswwordRequest).enqueue(new
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
                    baseResponse.handleErrorForDialog(resourceProvider.getActivityContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<BaseResponse> mobileChange(ForgotPaswwordRequest forgotPaswwordRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().mobileChange(token, forgotPaswwordRequest).enqueue(new
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

    public LiveData<BaseResponse> mobileChangeVerify(ForgotPaswwordRequest forgotPaswwordRequest) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().mobileChangeVerify(token, forgotPaswwordRequest).enqueue(new
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
                    baseResponse.handleErrorForDialog(resourceProvider.getActivityContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<ProductListResponse> getVariationForProduct(String productId, int variaionId) {
        MutableLiveData<ProductListResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getVartionForSelectedProduct(productId, variaionId).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<ProductListResponse>() {
            @Override
            public void onSuccesResponse(Call<ProductListResponse> call, ProductListResponse baseResponse) {
                if (baseResponse != null) {
                    liveData.setValue(baseResponse);
                }
            }

            @Override
            public void onFailurResponse(Call<ProductListResponse> call, BaseResponse baseResponse) {
                if (baseResponse != null) {
                    baseResponse.handleError(resourceProvider.getAppContext());
                }
                liveData.setValue(null);
            }
        }));
        return liveData;
    }

    public LiveData<CityListModelResponse> getCityList(String lang) {
        MutableLiveData<CityListModelResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getCityList(lang).enqueue(new RetrofitCallBack<>
                (new RetrofitCallBack.CustomCallBack<CityListModelResponse>() {
                    @Override
                    public void onSuccesResponse(Call<CityListModelResponse> call,
                                                 CityListModelResponse cityListModelResponse) {
                        if (cityListModelResponse != null) {
                            liveData.setValue(cityListModelResponse);
                        }

                    }

                    @Override
                    public void onFailurResponse(Call<CityListModelResponse> call, BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            baseResponse.handleError(resourceProvider.getAppContext());
                        }
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }

    public LiveData<CityListModelResponse> getCategory(String lang) {
        MutableLiveData<CityListModelResponse> liveData = new MutableLiveData<>();
        RetrofitClient.getInstance().getCategoriesList(lang).enqueue(new RetrofitCallBack<>
                (new RetrofitCallBack.CustomCallBack<CityListModelResponse>() {
                    @Override
                    public void onSuccesResponse(Call<CityListModelResponse> call,
                                                 CityListModelResponse cityListModelResponse) {
                        if (cityListModelResponse != null) {
                            liveData.setValue(cityListModelResponse);
                        }

                    }

                    @Override
                    public void onFailurResponse(Call<CityListModelResponse> call, BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            baseResponse.handleError(resourceProvider.getAppContext());
                        }
                        liveData.setValue(null);
                    }
                }));
        return liveData;
    }

    public LiveData<BaseResponse> updateProfile(UpdateProfileModelRequest request) {
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().updateProfile(token, request).enqueue(new RetrofitCallBack<>
                (new RetrofitCallBack.CustomCallBack<BaseResponse>() {
                    @Override
                    public void onSuccesResponse(Call<BaseResponse> call,
                                                 BaseResponse cityListModelResponse) {
                        liveData.setValue(cityListModelResponse);

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

    public LiveData<BaseResponse> getUserDetail(String userId) {
        UserId userId1 = new UserId();
        userId1.setUserId(userId);
        userId1.setLang(resourceProvider.getCountryLang());
        MutableLiveData<BaseResponse> liveData = new MutableLiveData<>();
        String token = PreferenceUtils.getValueString(resourceProvider.getActivityContext(),
                PreferenceUtils.USER_LOGIIN_TOKEN);
        RetrofitClient.getInstance().getUserDetail(token, userId1).enqueue(new
                RetrofitCallBack<>(new RetrofitCallBack.CustomCallBack<BaseResponse>() {
            @Override
            public void onSuccesResponse(Call<BaseResponse> call, BaseResponse orderResponses) {
                if (orderResponses != null) {
                    liveData.setValue(orderResponses);
                }
            }

            @Override
            public void onFailurResponse(Call<BaseResponse> call, BaseResponse baseResponse) {
                baseResponse.handleError(resourceProvider.getAppContext());
                liveData.setValue(null);
            }
        }));
        return liveData;
    }
}
