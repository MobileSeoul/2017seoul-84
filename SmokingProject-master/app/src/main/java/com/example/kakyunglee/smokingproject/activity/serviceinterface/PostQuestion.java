package com.example.kakyunglee.smokingproject.activity.serviceinterface;


import com.example.kakyunglee.smokingproject.activity.dto.response.QuestionResponseDTO;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Jeongyeji on 2017-10-24.
 */

public interface PostQuestion {
    @Multipart
    @POST("SmokingServer/service/question")
    Call<QuestionResponseDTO> postQuestion(@Part("title") String title,
                                           @Part("report_category_id") int report_category_id,
                                           @Part("email") String email,
                                           @Part("contents") String contents,
                                           @Part MultipartBody.Part image);

}
