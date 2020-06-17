package com.example.goalfitness.Fragments;

import com.example.goalfitness.Notification.MyResponse;
import com.example.goalfitness.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAgwnYWrA:APA91bFe0mPkpnGon3fI3eBeNj_T--iAmtuf9TrTjw4LVFM7oVmaLriJ_Uan9xt6rNvSKjPHHsBa3XUteqcWPcsfsz0c7QQ6HWc4auL5tRYHwY5sjX8M0m_u8G-L9cbgqVDhRlnoI5IB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotidication(@Body Sender body);
}
