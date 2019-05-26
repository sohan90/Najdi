package com.najdi.android.najdiapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.najdi.android.najdiapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final long RETROFIT_TIMEOUT = 60;
    private static NajdiApi najdiApi = null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BuildConfig.NAJDI_END_POINTS)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);


    private static OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
            .connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();

                Request additionalRequest = requestBuilder.build();
                return chain.proceed(additionalRequest);
            })
            .addInterceptor(loggingInterceptor);

    private static Retrofit retrofit = retrofitBuilder.client(okHttpBuilder.build()).build();

    public static NajdiApi getInstance() {
        if (najdiApi == null)
            najdiApi = retrofit.create(NajdiApi.class);
        return najdiApi;
    }
}
