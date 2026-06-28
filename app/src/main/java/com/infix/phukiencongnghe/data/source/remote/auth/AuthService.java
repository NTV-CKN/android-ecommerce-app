package com.infix.phukiencongnghe.data.source.remote.auth;

import com.infix.phukiencongnghe.data.dto.request.RefreshTokenDTO;
import com.infix.phukiencongnghe.data.dto.request.ResetPasswordDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;
import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.JwtFromLoginDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("/api/v1/auth/register")
    Call<SuccessBasicDTO> register(@Body UserRegisterDTO user);

    @GET("/api/v1/auth/verify-mail")
    Call<SuccessBasicDTO> verifyEmail(@Query("token") String token);

    @POST("/api/v1/auth/refresh-token")
    Call<JwtFromLoginDTO> refreshToken(@Body RefreshTokenDTO refreshTokenDTO);

    @POST("/api/v1/auth/reset-password")
    Call<SuccessBasicDTO> resetPassword(@Body ResetPasswordDTO resetPasswordDTO);

    @GET("/api/v1/auth/send-email-reset-password")
    Call<SuccessBasicDTO> sendMailResetPassword(@Query("email") String email);

    @POST("/api/v1/auth/login-local")
    Call<JwtFromLoginDTO> loginLocal(@Body UserLoginDTO userLoginDTO);

    @POST("/api/v1/auth/login-google")
    Call<JwtFromLoginDTO> loginGoogle(@Body UserLoginGoogleDTO userLoginGoogleDTO);

    @POST("/api/v1/auth/check-role-admin")
    Call<SuccessBasicDTO> isUserAdmin();
}
