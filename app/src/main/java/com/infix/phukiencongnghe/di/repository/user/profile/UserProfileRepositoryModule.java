package com.infix.phukiencongnghe.di.repository.user.profile;

import com.infix.phukiencongnghe.data.repository.user_manage.profile.IUserProfileRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.profile.UserProfileRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class UserProfileRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IUserProfileRepository bindUserProfileRepository(UserProfileRepositoryImpl userProfileRepository);
}
