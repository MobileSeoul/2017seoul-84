package com.example.kakyunglee.smokingproject.activity.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * For GeoCoding Retrofit class
 */

public class GeoRetrofit {
    private static GeoRetrofit instance = new GeoRetrofit();
    private Retrofit retrofit;
    final String serverURI = "https://maps.googleapis.com/";
    private GeoRetrofit(){
        buildRetrofit();
    }

    private void buildRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(serverURI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static GeoRetrofit getInstance() { return instance;}

    public Retrofit getRetrofit(){return retrofit;}
}
