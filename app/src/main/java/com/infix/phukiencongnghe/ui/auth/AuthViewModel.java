package com.infix.phukiencongnghe.ui.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.infix.phukiencongnghe.common.OnLoginGoogleListener;
import com.infix.phukiencongnghe.common.TypeAccount;
import com.infix.phukiencongnghe.data.dto.request.ResetPasswordDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginDTO;
import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;
import com.infix.phukiencongnghe.data.dto.request.UserRegisterDTO;
import com.infix.phukiencongnghe.data.dto.response.ExceptionResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.JwtFromLoginDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private final IAuthRepository authRepository;

    //Nếu JwtFromLoginDTO khác null thì AuthActivity lắng nghe và thực hiện logic trước khi chuyển sang MainActivity
    //Chỉ có login đăng nhập như local/google mới đi set giá trị cho thằng này
    private final MutableLiveData<JwtFromLoginDTO> _jwtFromLoginDTO = new MutableLiveData<>();
    public LiveData<JwtFromLoginDTO> jwtFromLoginDTO = _jwtFromLoginDTO;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public AuthViewModel(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private IAuthRepository authRepository;

        public Factory(IAuthRepository authRepository) {
            this.authRepository = authRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AuthViewModel.class))
                return (T) new AuthViewModel(authRepository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }

    /**
     * Chức năng đăng kí người dùng
     * @param email
     * @param password
     */
    public void registerUser(String email, String password) {
        UserRegisterDTO user = new UserRegisterDTO(email, password, TypeAccount.LOCAL.type);
        _isLoading.setValue(true);
        authRepository.register(user).enqueue(new Callback<SuccessBasicDTO>() {
            @Override
            public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                if (response.isSuccessful()) {
                    SuccessBasicDTO succ = response.body();
                    if (succ != null && succ.isSuccess())
                        _notifyMsg.setValue(
                                "Đăng kí tài khoản thành công, Chúng tôi sẽ gửi email để xác thực tài khoản của bạn"
                        );
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                _notifyMsg.setValue(throwable.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    /**
     * Chức năng xác thực mail khi đã đăng kí tài khoản trước đó, set lại trạng thái tài khoản của người dùng để active
     * @param token
     */
    public void verifyMail(String token) {
        if(token == null || token.isEmpty()) return;
        _isLoading.setValue(true);
        authRepository.verifyEmail(token).enqueue(new Callback<SuccessBasicDTO>() {
            @Override
            public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                if (response.isSuccessful()) {
                    SuccessBasicDTO succ = response.body();
                    if (succ != null && succ.isSuccess())
                        _notifyMsg.setValue(
                                "Xác thực thành công, vui lòng đăng nhập"
                        );
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                _notifyMsg.setValue(throwable.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    /**
     * Chức năng đăng nhập dành cho tài khoản được tạo trực tiếp từ hệ thống
     * @param email
     * @param password
     */
    public void loginLocal(String email, String password) {
        _isLoading.setValue(true);

        UserLoginDTO userLoginDTO = new UserLoginDTO(email, password);
        authRepository.loginLocal(userLoginDTO).enqueue(new Callback<JwtFromLoginDTO>() {
            @Override
            public void onResponse(Call<JwtFromLoginDTO> call, Response<JwtFromLoginDTO> response) {
                if (response.isSuccessful()) {
                    JwtFromLoginDTO jwtFromLoginDTO = response.body();
                    if (jwtFromLoginDTO != null && jwtFromLoginDTO.checkAccessAndRefreshValid())
                        _jwtFromLoginDTO.setValue(jwtFromLoginDTO);
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<JwtFromLoginDTO> call, Throwable throwable) {
                _notifyMsg.setValue(throwable.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    /**
     * Chức năng này xử lí token id của người dùng khi nhấn nút và chọn 1 trong các tài khoản,
     * sau khi có token id thì tiến hành gọi logic chứng thực
     * @param idToken
     * @param onLoginGoogleListener
     */
    public void loginWithGoogle(String idToken, OnLoginGoogleListener onLoginGoogleListener) {
        authRepository.loginGoogle(idToken, onLoginGoogleListener);
    }

    /**
     * Chức năng đăng nhập dành cho tài khoản đăng nhập bằng google
     * @param userLoginGoogleDTO
     */
    public void loginGoogle(UserLoginGoogleDTO userLoginGoogleDTO) {
        _isLoading.postValue(true);

        authRepository.loginGoogle(userLoginGoogleDTO).enqueue(new Callback<JwtFromLoginDTO>() {
            @Override
            public void onResponse(Call<JwtFromLoginDTO> call, Response<JwtFromLoginDTO> response) {
                if (response.isSuccessful()) {
                    JwtFromLoginDTO jwtFromLoginDTO = response.body();
                    if (jwtFromLoginDTO != null && jwtFromLoginDTO.checkAccessAndRefreshValid()) {
                        _jwtFromLoginDTO.postValue(jwtFromLoginDTO);
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.postValue("Không thể hiểu lỗi");
                        return;
                    }

                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        _notifyMsg.postValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.postValue(e.getMessage());
                    }
                }
                _isLoading.postValue(false);
            }
            @Override
            public void onFailure(Call<JwtFromLoginDTO> call, Throwable throwable) {
                _notifyMsg.postValue(throwable.getMessage());
                _isLoading.postValue(false);
            }
        });
    }

    public void resetPassword(String email, String password, String token ) {
        ResetPasswordDTO resetPasswordDTO  = new ResetPasswordDTO(
                email,
                password,
                token
        );
        _isLoading.setValue(true);

        authRepository.resetPassword(resetPasswordDTO).enqueue(new Callback<SuccessBasicDTO>() {
            @Override
            public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                if (response.isSuccessful()) {
                    SuccessBasicDTO succ = response.body();
                    if (succ != null && succ.isSuccess())
                        _notifyMsg.setValue(
                                succ.getMessage()
                        );
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void resetStates() {
        _notifyMsg.setValue(null);
        _isLoading.setValue(null);
        _jwtFromLoginDTO.setValue(null);
    }
}
