package com.infix.phukiencongnghe.data.repository.auth;

import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.common.OnLoginGoogleListener;
import com.infix.phukiencongnghe.data.dto.request.ResetPasswordDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;
import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.JwtFromLoginDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;
import com.infix.phukiencongnghe.data.source.local.source.user.IUserLocalDataSource;
import com.infix.phukiencongnghe.data.source.remote.auth.AuthService;
import com.infix.phukiencongnghe.data.source.remote.auth.IAuthRemoteDataSource;
import com.infix.phukiencongnghe.utils.AppExecutors;

import retrofit2.Call;

public class AuthRepositoryImpl implements IAuthRepository {
    private final AuthService authService;
    private final IUserLocalDataSource userLocalDataSource;
    private final IAuthRemoteDataSource authRemoteDataSource;

    public AuthRepositoryImpl(
            AuthService authService,
            IUserLocalDataSource userLocalDataSource,
            IAuthRemoteDataSource authRemoteDataSource
    ) {
        this.authService = authService;
        this.userLocalDataSource = userLocalDataSource;
        this.authRemoteDataSource = authRemoteDataSource;
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

    @Override
    public Call<JwtFromLoginDTO> loginLocal(UserLoginDTO userLoginDTO) {
        return authService.loginLocal(userLoginDTO);
    }

    @Override
    public Call<JwtFromLoginDTO> loginGoogle(UserLoginGoogleDTO userLoginGoogleDTO) {
        return authService.loginGoogle(userLoginGoogleDTO);
    }

    @Override
    public Call<SuccessBasicDTO> isUserAdmin() {
        return authService.isUserAdmin();
    }

    @Override
    public void loginGoogle(String idToken, OnLoginGoogleListener onLoginGoogleListener) {
        authRemoteDataSource.onLoginGoogle(idToken, onLoginGoogleListener);
    }

    @Override
    public void insertUserEntity(UserEntity user, OnCallbackListener onCallbackListener) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            userLocalDataSource.insert(user);
            AppExecutors.getInstance().mainThread().execute(onCallbackListener::onRevoke);
        });
    }
}
