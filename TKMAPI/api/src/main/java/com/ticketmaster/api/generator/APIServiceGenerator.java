package com.ticketmaster.api.generator;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Artur Bryzhatiy on 4/18/16.
 */
public class APIServiceGenerator {

    public static final String API_BASE_URL = "https://app.ticketmaster.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());


    public static <T> T createService(Class <T> serviceClass, Context context) {
         //Uncomment for view log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
//        Cache cache = new Cache(httpCacheDirectory, cacheSize);
//        httpClient.cache(cache);
        Retrofit retrofit = builder.client(httpClient.build()).build();


        return retrofit.create(serviceClass);
    }
    public static <T> T createService(Class <T> serviceClass) {
        // Uncomment for view log
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.interceptors().add(interceptor);

        Retrofit retrofit = builder.client(httpClient.build()).build();


        return retrofit.create(serviceClass);
    }
}
