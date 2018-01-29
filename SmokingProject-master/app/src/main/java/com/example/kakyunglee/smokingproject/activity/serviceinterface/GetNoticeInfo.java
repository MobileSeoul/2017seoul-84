package com.example.kakyunglee.smokingproject.activity.serviceinterface;

import com.example.kakyunglee.smokingproject.activity.dto.NoticeDTO;
import com.example.kakyunglee.smokingproject.activity.dto.NoticeListDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Jeongyeji on 2017-10-23.
 */

public interface GetNoticeInfo {
    //요청 메세지에 대한 sub url을 작성한다. base url + sub url 이 되어 요청이 날라단다
    // ex) http://52.78.234.100:3000/designer/{id}/info 이 주소로 요청이 찍힌다.
    // 여기서 {id}는 path parameter를 의미 한다. 값을 치환한다.
    //@Headers() -> 필요하다면 헤더 넣을 수 있음 보통 인증에 대한 정보가 들어감
    @GET("SmokingServer/service/notice/lists")
    //응답으로 온 데이터를 받을 call generic에 dto를 쓴다. (응답으로 오는 데이터가 array타입이면 List로 받아야 한다. ex) List<DesignerDTO>)

    Call<NoticeListDTO> noticeInfo(
            // path 파라미터에 들어갈 데이터를 의미한다. 파라미터로 온 id값에 의해 요청 id값이 바뀐다.
            //@Path("id") String id
            //@Query("sort") string sort; // 쿼리 파라미터가 필요한 경우
            //@QueryMap -> 쿼리 파라미터 복합적으로 사용하는 경우

    );

    @GET("SmokingServer/service/notice/detail/{id}")
    Call<NoticeDTO> noticeDetailInfo(@Path("id") int id);
}
