package com.example.apple.playmusic.service;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofit {

    public static final int READ_TIME_OUT = 10000;
    public static final int WRITE_TIME_OUT = 10000;
    public static final int CONNECT_TIME_OUT = 10000;
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(String url){
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                                                        .writeTimeout(WRITE_TIME_OUT,TimeUnit.MILLISECONDS)
                                                        .connectTimeout(CONNECT_TIME_OUT,TimeUnit.MILLISECONDS)
                                                        .retryOnConnectionFailure(true)
                                                        .protocols(Arrays.asList(Protocol.HTTP_1_1))
                                                        .build();


        retrofit = new Retrofit.Builder().baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit;
    }
}
