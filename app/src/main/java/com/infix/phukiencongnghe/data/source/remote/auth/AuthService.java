package com.infix.phukiencongnghe.data.source.remote.auth;

import com.infix.phukiencongnghe.data.dto.request.ResetPasswordDTO;
import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("/api/v1/auth/register")
    Call<SuccessBasicDTO> register(@Body UserRegisterDTO user);

    @GET("/api/v1/auth/verify-email")
    Call<SuccessBasicDTO> verifyEmail(@Query("email") String email);

    @POST("/api/v1/auth/reset-password")
    Call<SuccessBasicDTO> resetPassword(@Body ResetPasswordDTO resetPasswordDTO);

    @GET("/api/v1/auth/send-email-reset-password")
    Call<SuccessBasicDTO> sendMailResetPassword(@Query("email") String email);
}
