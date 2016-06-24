package com.ticketmaster.api.discovery.entry.base.music;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Artur Bryzhatiy on 4/21/16.
 */
public class iTunesServiceGenerator {

    public static final String API_BASE_URL = "https://itunes.apple.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());


    public static <T> T createService(Class <T> serviceClass) {
        // Uncomment for view log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        Retrofit retrofit = builder.client(httpClient.build()).build();


        return retrofit.create(serviceClass);
    }

}
