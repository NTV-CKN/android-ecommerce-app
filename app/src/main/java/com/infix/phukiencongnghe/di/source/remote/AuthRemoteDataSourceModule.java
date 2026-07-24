package com.infix.phukiencongnghe.di.source.remote;

import com.infix.phukiencongnghe.data.source.remote.auth.AuthRemoteDataSourceImpl;
import com.infix.phukiencongnghe.data.source.remote.auth.IAuthRemoteDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class AuthRemoteDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IAuthRemoteDataSource bindAuthRemoteDataSource(AuthRemoteDataSourceImpl authRemoteDataSource);
}
