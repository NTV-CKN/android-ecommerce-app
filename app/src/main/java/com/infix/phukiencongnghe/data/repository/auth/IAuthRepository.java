package com.infix.phukiencongnghe.data.repository.auth;

import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;

import retrofit2.Call;

public interface IAuthRepository {
    Call<SuccessBasicDTO> register(UserRegisterDTO user);
    Call<SuccessBasicDTO> verifyEmail(String email);
}
