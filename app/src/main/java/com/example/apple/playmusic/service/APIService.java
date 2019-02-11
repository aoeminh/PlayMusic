package com.example.apple.playmusic.service;

public class APIService {

    public static String BASE_URL ="https://aoeminh1994.000webhostapp.com/Server/";

    public static DataService getRetrofitClient(){
        return APIRetrofit.getRetrofitClient(BASE_URL).create(DataService.class);
    }
}
