package com.infix.phukiencongnghe.di.repository.auth;

import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class AuthRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IAuthRepository bindAuthRepository(AuthRepositoryImpl authRepository);
}
