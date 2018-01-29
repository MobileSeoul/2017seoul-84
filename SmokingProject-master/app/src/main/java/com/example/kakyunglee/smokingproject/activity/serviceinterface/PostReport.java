package com.example.kakyunglee.smokingproject.activity.serviceinterface;

import com.example.kakyunglee.smokingproject.activity.dto.response.ReportDetailResultDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.ReportResultDTO;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Jeongyeji on 2017-10-23.
 */

public interface PostReport {
    @FormUrlEncoded
    @POST("SmokingServer/service/report")
    Call<ReportResultDTO> postSimpleReport(
            //@FieldMap Map<String,String> params
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @Multipart
    @POST("SmokingServer/service/report/detail")
    Call<ReportDetailResultDTO> postDetailReport(
            @Part("report_category_id") int report_category_id,
            @Part("email") String email,
            @Part("contents") String contents,
            @Part MultipartBody.Part image,
            @Part("report_detail_id") int report_detail_id
    );
}
