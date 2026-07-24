package com.infix.phukiencongnghe.ui.share_viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class RoleUserViewModel extends ViewModel {
    private final IAuthRepository authRepository;

    private MutableLiveData<Boolean> _isAdmin = new MutableLiveData<>();
    public LiveData<Boolean> isAdmin = _isAdmin;

    @Inject
    public RoleUserViewModel(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void isUserAdmin() {
        authRepository.isUserAdmin().enqueue(new Callback<SuccessBasicDTO>() {
            @Override
            public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                if(response.isSuccessful()){
                    SuccessBasicDTO succ = response.body();
                    _isAdmin.setValue(succ.isSuccess());
                }
            }

            @Override
            public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {

            }
        });
    }
}