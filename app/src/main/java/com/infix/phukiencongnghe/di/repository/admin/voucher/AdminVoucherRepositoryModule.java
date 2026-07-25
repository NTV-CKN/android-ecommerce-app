package com.infix.phukiencongnghe.di.repository.admin.voucher;

import com.infix.phukiencongnghe.data.repository.admin.voucher.AdminVoucherRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.admin.voucher.IAdminVoucherRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class AdminVoucherRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IAdminVoucherRepository bindAdminVoucherRepository(AdminVoucherRepositoryImpl adminVoucherRepository);
}
