package com.example.kakyunglee.smokingproject.activity.serviceinterface;

import com.example.kakyunglee.smokingproject.activity.dto.AreaNoneSmokingDTO;
import com.example.kakyunglee.smokingproject.activity.dto.AreaSmokingDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chakh on 2017-10-28.
 */

public interface SmokingArea {
    @GET("SmokingServer/service/area/smoking")
    Call<List<AreaSmokingDTO>> getSmokingArea(
            @Query("latitude") String latitude,
            @Query("longtitude") String longitude
    );
}
