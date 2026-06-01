package com.infix.phukiencongnghe.data.repository.auth;

import com.infix.phukiencongnghe.data.dto.request.ResetPasswordDTO;
import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.source.remote.auth.AuthService;

import retrofit2.Call;

public class AuthRepositoryImpl implements IAuthRepository {
    private AuthService authService;

    public AuthRepositoryImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Call<SuccessBasicDTO> register(UserRegisterDTO user) {
        return authService.register(user);
    }

    @Override
    public Call<SuccessBasicDTO> verifyEmail(String email) {
        return authService.verifyEmail(email);
    }

    @Override
    public Call<SuccessBasicDTO> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        return authService.resetPassword(resetPasswordDTO);
    }

    @Override
    public Call<SuccessBasicDTO> sendMailResetPassword(String email) {
        return authService.sendMailResetPassword(email);
    }
}
