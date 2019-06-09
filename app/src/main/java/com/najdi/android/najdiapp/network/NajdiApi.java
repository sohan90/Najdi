package com.najdi.android.najdiapp.network;

import com.najdi.android.najdiapp.checkout.model.OrderRequest;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.BuildConfig;
import com.najdi.android.najdiapp.common.Constants;
import com.najdi.android.najdiapp.home.model.CartRequest;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.launch.model.LoginRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupRequestModel;
import com.najdi.android.najdiapp.launch.model.SignupResponseModel;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NajdiApi {

    @POST(BuildConfig.NAJDI_END_POINTS + "customers")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<SignupResponseModel> registerUser(@Body SignupRequestModel signupRequestModel);

    @POST("https://najdisheep.com/temp/livetest/wp-json/jwt-auth/v1/token")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<BaseResponse> loginUser(@Body LoginRequestModel loginRequestModel);

    @GET(BuildConfig.NAJDI_END_POINTS + "products")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<List<ProductListResponse>> getProducts();

    @POST(BuildConfig.NAJDI_END_POINTS + "cart/add")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<BaseResponse> addToCart(@Body CartRequest cartRequest);

    @GET(BuildConfig.NAJDI_END_POINTS + "cart")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<CartResponse> getCart();

    @HTTP(method = "DELETE", path = "cart/cart-item", hasBody = true)
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<BaseResponse> removeCartItem(@Body HashMap<String, String> cartObj);

    @GET(BuildConfig.NAJDI_END_POINTS + "products/{productId}")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<ProductListResponse> getIndividualProduct(@Path("productId") int productId);

    @POST(BuildConfig.NAJDI_END_POINTS + "orders")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<OrderResponse> createOrder(@Query("customer") int userId, @Body OrderRequest orderRequest);
}
