package org.intracode.chattting.Fragments;

import org.intracode.chattting.Notification.MyResponse;
import org.intracode.chattting.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAvZPnAzY:APA91bFE_CH9GyBdVhv0BYbNgj_UM1jNVukIFAgNTgpFrJnH9NVv-zuXAmuQtyiWTg6m9_b2QHssyEGvhHrMe0iyEiV9NiSlECh7ext25ZdgwkJDtY7V2YwNi6tSe_UlFtIbWUSCF8OS"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
