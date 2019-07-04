package com.najdi.android.najdiapp.network;

import com.najdi.android.najdiapp.BuildConfig;
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
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.model.UpdateCartRequest;
import com.najdi.android.najdiapp.utitility.LocaleUtitlity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NajdiApi {

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/signup")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> registerUser(@Body SignupRequestModel signupRequestModel);

    @POST("https://najdisheep.com/temp/livetest/wp-json/jwt-auth/v1/token")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> loginUser(@Query("lang") String lang, @Body LoginRequestModel loginRequestModel);

    @GET(BuildConfig.NAJDI_END_POINTS + "products")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<List<ProductListResponse>> getProducts(@Query("lang") String lang);

    @POST(BuildConfig.NAJDI_END_POINTS + "cart/add")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> addToCart(@Query("lang") String lang, @Header("Authorization") String token, @Body CartRequest cartRequest);

    @GET(BuildConfig.NAJDI_END_POINTS + "cart")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<CartResponse> getCart(@Query("customer") String userId, @Query("lang") String lang );

    @HTTP(method = "DELETE", path = "cart/cart-item", hasBody = true)
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> removeCartItem(@Header("Authorization") String token, @Body HashMap<String, String> cartObj);

    @GET(BuildConfig.NAJDI_END_POINTS + "products/{productId}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<ProductListResponse> getIndividualProduct(@Path("productId") int productId, @Query("lang") String lang);

    @POST(BuildConfig.NAJDI_END_POINTS + "orders")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<OrderResponse> createOrder(@Query("customer") int userId, @Body OrderRequest orderRequest);

    @GET(BuildConfig.NAJDI_END_POINTS + "orders")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<List<OrderResponse>> getOrderStatus(@Query("lang") String lang, @Query("customer") int userId);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/cart/update_item")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateItemQuantity(@Header("Authorization")String token, @Query("lang") String lang, @Query("customer") String userId,
                                          @Body UpdateCartRequest cartRequest);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/verify")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> verifyOtp(@Body OtpRequestModel requestModel);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/resend")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> resendOtp(@Body OtpRequestModel requestModel);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "jwt-auth/v1/token")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> loginToken(@Body LoginRequestModel loginRequestModel);

    @POST(BuildConfig.NAJDI_END_POINTS + "cart/clear")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> clearCart(@Query("lang") String lang, @Query("customer") String userId);

    @GET(BuildConfig.NAJDI_CART_BASE_URL + "customapi/banklist")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> getBankDetails(@Query("lang") String lang);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/contactus")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> contactUs(@Query("lang") String lang, @Body ContactUsRequest request);

    @GET(BuildConfig.NAJDI_CART_BASE_URL + "customapi/count_items")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> getCartCount(@Query("lang") String lang, @Query("customer")String userId);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/forgot")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> forgotPassword(@Body ForgotPaswwordRequest request);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/forgot_verify")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> forgotVerify(@Body OtpRequestModel request);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/forgot_update")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> forgotUpdate(@Body ForgotPaswwordRequest request);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/mobile_change")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> mobileChange(@Body ForgotPaswwordRequest request);

    @POST(BuildConfig.NAJDI_CART_BASE_URL + "customapi/password_change")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<BaseResponse> changePasswormd(@Body ForgotPaswwordRequest request);


    @GET(BuildConfig.NAJDI_CART_BASE_URL + "wp/v2/pages/{page_id}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<HtmlResponseForNajdi> getHtmlTermsAboutUsPrivacyPolicy(@Path("page_id") int pageId);

    @GET(BuildConfig.NAJDI_CART_BASE_URL + "wc/v2/products/{product_id}/variations/{variation_id}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + BuildConfig.BASIC_64_AUTH})
    Call<ProductListResponse> getVartionForSelectedProduct(@Path("product_id")int productId,
                                                            @Path("variation_id") int variationId);

}
