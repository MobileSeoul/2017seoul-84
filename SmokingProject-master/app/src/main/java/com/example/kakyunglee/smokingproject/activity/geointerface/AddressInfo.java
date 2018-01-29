package com.example.kakyunglee.smokingproject.activity.geointerface;

import com.example.kakyunglee.smokingproject.activity.dto.response.GeoCodeResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chakh on 2017-10-27.
 */

public interface AddressInfo {
    @GET("maps/api/geocode/json")
    Call<GeoCodeResult> reverseGeoResult(
       @Query("latlng") String latlng,
       @Query("language") String language,
       @Query("key") String key
    );

    @GET("maps/api/geocode/json")
    Call<GeoCodeResult> geoResult(
            @Query("address") String address,
            @Query("language") String language,
            @Query("key") String key
    );
}

