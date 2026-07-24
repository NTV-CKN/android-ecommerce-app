package com.infix.phukiencongnghe.di.source.local;

import com.infix.phukiencongnghe.data.source.local.source.user.IUserLocalDataSource;
import com.infix.phukiencongnghe.data.source.local.source.user.UserLocalDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class UserLocalDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IUserLocalDataSource bindUserLocalDataSource(UserLocalDataSourceImpl userLocalDataSource);
}
