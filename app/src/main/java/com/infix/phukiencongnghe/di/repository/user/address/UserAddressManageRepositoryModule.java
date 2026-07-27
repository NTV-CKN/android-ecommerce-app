package com.infix.phukiencongnghe.di.repository.user.address;

import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.address.UserAddressManageRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class UserAddressManageRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IUserAddressManageRepository bindUserAddressManage(UserAddressManageRepositoryImpl userAddressManageRepository);
}
