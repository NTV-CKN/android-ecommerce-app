package com.infix.phukiencongnghe.ui.share_viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserEntityViewModel extends ViewModel {
    private final IAuthRepository authRepository;

    @Inject
    public UserEntityViewModel(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void insertUser(UserEntity user, OnCallbackListener onCallbackListener) {
        authRepository.insertUserEntity(user, onCallbackListener);
    }
}