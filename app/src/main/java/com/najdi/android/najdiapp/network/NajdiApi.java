package com.najdi.android.najdiapp.network;

import com.najdi.android.najdiapp.BuildConfig;
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
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NajdiApi {

    @POST("app_signup")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> registerUser(@Body SignupRequestModel signupRequestModel);

    @POST("https://najdisheep.com/temp/livetest/wp-json/jwt-auth/v1/token")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> loginUser(@Query("lang") String lang, @Body LoginRequestModel loginRequestModel);

    @POST("list_products_cities")
    @Headers({"Content-Type:application/json"})
    Call<ProductModelResponse> getCityBasedProducts(@Header("_token")String value,
                                                    @Body  HashMap<String, String> city);

    @POST("list_products_cat")
    @Headers({"Content-Type:application/json"})
    Call<ProductModelResponse> getCategoryBasedProducts(@Header("_token")String value,
                                                        @Body  HashMap<String, String> category);

    @GET("list_products")
    @Headers({"Content-Type:application/json"})
    Call<ProductModelResponse> getProducts(@Query("lang") String value);

    @POST("app_list_cart")
    @Headers({"Content-Type:application/json"})
    Call<CartResponse> getCart(@Header("_token")String value, @Body UserId userId);

    @POST("app_remove_item")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> removeCartItem(@Header("_token")String value,
                                      @Body HashMap<String, String> cartObj);

    @POST("product_details")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getIndividualProduct(@Body ProductId productId);

    @POST("place_order")
    @Headers({"Content-Type:application/json"})
    Call<OrderResponse> createOrder(@Header("_token")String value,
                                    @Body BillingAddress billingAddress);

    @POST("app_my_orders")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getOrderStatus(@Header("_token")String value, @Body UserId userId);

    @POST("app_update_cart_qty")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateItemQuantity(@Header("_token")String value, @Body UpdateCartRequest cartRequest);

    @POST("app_verify")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> verifyOtp(@Body OtpRequestModel requestModel);

    @POST("customapi/resend")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> resendOtp(@Body OtpRequestModel requestModel);

    @POST("customapi/forgot/resend")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> forgotResendOtp(@Body OtpRequestModel requestModel);

    @POST("app_login")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> loginToken(@Body LoginRequestModel loginRequestModel);

    @POST("app_empty_cart")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> clearCart(@Header("_token")String value, @Body UserId userId);

    @GET("app_bank_details")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getBankDetails(@Query("lang")String value);

    @POST("app_contact_form")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> contactUs(@Header("_token")String value, @Body ContactUsRequest request);

    @POST("app_count_cart_items")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getCartCount(@Header("-token")String value, @Body UserId userId);

    @POST("app_forgot_password")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> forgotPassword(@Body ForgotPaswwordRequest request);

    @POST("app_forgot_password_verify")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> forgotVerify(@Body OtpRequestModel request);

    @POST("app_reset_password")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> forgotUpdate(@Body ForgotPaswwordRequest request);

    @POST("customapi/mobile_change")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> mobileChange(@Body ForgotPaswwordRequest request);

    @POST("customapi/mobile_change_verify")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> mobileChangeVerify(@Body ForgotPaswwordRequest request);

    @POST("app_user_change_password")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> changePassword(@Header("_token") String value,
                                      @Body ForgotPaswwordRequest request);


    @GET("app_about_us")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getAboutUs(@Query("lang")String value);

    @GET("app_privacy_policy")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getPrivacyPolicy(@Query("lang")String value);

    @GET("app_terms_conditions")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getTermsCondition(@Query("lang")String value);

    @GET("wc/v2/products/{product_id}/variations/{variation_id}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<ProductListResponse> getVartionForSelectedProduct(@Path("product_id") String productId,
                                                           @Path("variation_id") int variationId);


    @GET("list_cities")
    @Headers({"Content-Type:application/json"})
    Call<CityListModelResponse> getCityList(@Query("lang") String lang);

    @GET("list_categories")
    @Headers({"Content-Type:application/json"})
    Call<CityListModelResponse> getCategoriesList(@Query("lang") String lang);

    @POST("app_user_update_profile")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateProfile(@Header("_token") String value,
                                     @Body UpdateProfileModelRequest profileModelRequest);

    @POST("app_add_to_cart")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> addToCart(@Header("_token") String value, @Body CartRequest cartRequest);
}
