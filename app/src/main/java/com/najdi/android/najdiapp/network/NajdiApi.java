package com.najdi.android.najdiapp.network;

import com.najdi.android.najdiapp.BuildConfig;
import com.najdi.android.najdiapp.checkout.model.OrderRequest;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.CityListModelResponse;
import com.najdi.android.najdiapp.home.model.ContactUsRequest;
import com.najdi.android.najdiapp.home.model.ForgotPaswwordRequest;
import com.najdi.android.najdiapp.home.model.HtmlResponseForNajdi;
import com.najdi.android.najdiapp.home.model.ProductId;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.model.ProductModelResponse;
import com.najdi.android.najdiapp.home.model.UpdateProfileModelRequest;
import com.najdi.android.najdiapp.home.model.UserId;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;
import com.najdi.android.najdiapp.launch.model.OtpRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @GET("list_products")
    @Headers({"Content-Type:application/json"})
    Call<ProductModelResponse> getProducts();

    @POST("app_list_cart")
    @Headers({"Content-Type:application/json"})
    Call<CartResponse> getCart(@Body UserId userId);

    @POST("app_remove_item")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> removeCartItem(@Body HashMap<String, String> cartObj);

    @POST("product_details")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> getIndividualProduct(@Body ProductId productId);

    @POST("orders")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<OrderResponse> createOrder(@Query("lang") String lang, @Query("customer") int userId, @Body OrderRequest orderRequest);

    @GET("orders")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<List<OrderResponse>> getOrderStatus(@Query("lang") String lang, @Query("customer") int userId);

    @POST("app_update_cart_qty")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateItemQuantity(@Body UpdateCartRequest cartRequest);

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

    @POST("cart/clear")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> clearCart(@Query("lang") String lang, @Query("customer") String userId);

    @GET("customapi/banklist")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> getBankDetails(@Query("lang") String lang);

    @POST("customapi/contactus")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> contactUs(@Query("lang") String lang, @Body ContactUsRequest request);

    @GET("customapi/count_items")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> getCartCount(@Query("lang") String lang, @Query("customer") String userId);

    @POST("customapi/forgot")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> forgotPassword(@Body ForgotPaswwordRequest request);

    @POST("customapi/forgot_verify")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
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
    Call<BaseResponse> changePassword(@Body ForgotPaswwordRequest request);


    @GET("wp/v2/pages/{page_id}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<HtmlResponseForNajdi> getHtmlTermsAboutUsPrivacyPolicy(@Path("page_id") int pageId);

    @GET("wc/v2/products/{product_id}/variations/{variation_id}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<ProductListResponse> getVartionForSelectedProduct(@Path("product_id") String productId,
                                                           @Path("variation_id") int variationId);


    @GET("list_cities")
    @Headers({"Content-Type:application/json"})
    Call<CityListModelResponse> getCityList();

    @GET("list_categories")
    @Headers({"Content-Type:application/json"})
    Call<CityListModelResponse> getCategoriesList();

    @POST("app_user_update_profile")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateProfile(@Body UpdateProfileModelRequest profileModelRequest);

    @POST("app_add_to_cart")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> addToCart(@Body CartRequest cartRequest);
}
