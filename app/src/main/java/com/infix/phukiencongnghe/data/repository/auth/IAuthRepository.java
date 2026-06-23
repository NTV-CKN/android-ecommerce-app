package com.infix.phukiencongnghe.data.repository.auth;

import android.os.Handler;

import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.common.OnLoginGoogleListener;
import com.infix.phukiencongnghe.data.dto.request.ResetPasswordDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;
import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.JwtFromLoginDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;

import retrofit2.Call;

public interface IAuthRepository {
    Call<SuccessBasicDTO> register(UserRegisterDTO user);
    Call<SuccessBasicDTO> verifyEmail(String email);
    Call<SuccessBasicDTO> resetPassword(ResetPasswordDTO resetPasswordDTO);
    Call<SuccessBasicDTO> sendMailResetPassword(String email);
    Call<JwtFromLoginDTO> loginLocal(UserLoginDTO userLoginDTO);
    Call<JwtFromLoginDTO> loginGoogle(UserLoginGoogleDTO userLoginGoogleDTO);

    void loginGoogle(String idToken, OnLoginGoogleListener onLoginGoogleListener);
    void insertUserEntity(UserEntity user, OnCallbackListener onCallbackListener);
}
