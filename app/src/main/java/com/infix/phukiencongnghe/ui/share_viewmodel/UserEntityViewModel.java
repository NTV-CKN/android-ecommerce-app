package com.infix.phukiencongnghe.ui.share_viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;

public class UserEntityViewModel extends ViewModel {
    private final IAuthRepository authRepository;

    public UserEntityViewModel(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IAuthRepository authRepository;

        public Factory(IAuthRepository authRepository) {
            this.authRepository = authRepository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UserEntityViewModel.class)) {
                return (T) new UserEntityViewModel(authRepository);
            }
            throw new IllegalArgumentException("Model class illegal or unknown");
        }
    }

    public void insertUser(UserEntity user, OnCallbackListener onCallbackListener) {
        authRepository.insertUserEntity(user, onCallbackListener);
    }
}